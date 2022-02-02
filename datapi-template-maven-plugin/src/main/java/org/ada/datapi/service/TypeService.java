package org.ada.datapi.service;

import org.ada.datapi.data.Regex;
import org.ada.datapi.data.StoredJavaType;
import org.ada.datapi.domain.Type;
import org.ada.datapi.domain.TypeDeclaration;
import org.ada.datapi.exception.EnumSearchException;
import org.ada.datapi.exception.DatapiException;
import org.ada.datapi.util.TypeUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Service for converting strings into full fledged {@link Type}s
 */
@AllArgsConstructor
public class TypeService {

    private static final Pattern POP_PATTERN = Pattern.compile("[>,]");

    private final SearchService searchService;

    private final DependencyService dependencyService;

    /**
     * Validates that the string is correctly formatted for a Java type
     *
     * @param typeString
     *          string to validate
     * @return whether the string is valid
     */
    public boolean validateType(String typeString) {
        String normalizedTypeString = TypeUtil.normalizeTypeString(typeString).replaceAll(" ", "");

        return typeString.contains("<")
                ? this.validateComplexType(normalizedTypeString, new Stack<>())
                : this.validateClassName(normalizedTypeString, true);
    }

    /**
     * Converts the given string into a fully populated {@link TypeDeclaration}
     *
     * @param typeString
     *          string to convert
     * @return the converted declaration
     */
    public TypeDeclaration convertToTypeDeclaration(String typeString) {
        String normalizedTypeString = TypeUtil.normalizeTypeString(typeString);

        if (StringUtils.isBlank(normalizedTypeString) || !this.validateType(normalizedTypeString)) {
            throw new DatapiException("Malformed type provided: " + typeString);
        }

        List<Type> types = TypeUtil.splitToIndividualTypes(normalizedTypeString).stream()
                .map(t -> this.convertToType(t, false))
                .collect(Collectors.toList());

        String mergedTypeString = TypeUtil.mergeTypeStringAndListOfTypes(typeString, types);

        return new TypeDeclaration(mergedTypeString, types);
    }

    /**
     * Transforms a string into an object which describes the type and
     * its dependencies if it can find a matching class.
     *
     * @param typeString
     *          string to convert
     * @param shouldUsePrimitive
     * @return the converted type
     */
    public Type convertToType(String typeString, boolean shouldUsePrimitive) {
        String normalizedTypeString = TypeUtil.normalizeTypeString(typeString);

        if (!validateType(normalizedTypeString)) {
            throw new DatapiException("Malformed type provided: " + typeString);
        }

        StoredJavaType storedJavaType = null;
        try {
            storedJavaType = StoredJavaType.getStoredJavaType(normalizedTypeString);
        } catch (EnumSearchException e) {
            // This really isn't an error in this case. The type
            // isn't guaranteed to be a stored type here
        }

        return nonNull(storedJavaType)
                ? storedJavaType.getType(shouldUsePrimitive) : this.fetchCustomType(normalizedTypeString);
    }

    private Type fetchCustomType(String typeString) {
        List<String> matchingClassPaths = this.searchService.findClassesWithName(typeString);

        if (matchingClassPaths.size() > 1) {
            String possibleChoices = StringUtils.join(matchingClassPaths, ",\n\t");
            throw new DatapiException("Could not decide which type to use. Options: " + possibleChoices);
        } else if (matchingClassPaths.isEmpty()) {
            throw new DatapiException("Could not find custom type: " + typeString);
        }

        String matchingClass = FilenameUtils.getBaseName(matchingClassPaths.get(0));

        return new Type(matchingClass, this.dependencyService.fetchDependency(matchingClass));
    }

    /**
     * Recursive function to determine whether or not the given string is a valid
     * Java type. This examines the parameterized types to ensure everything is properly
     * formatted
     *
     * @param typeString
     *          string to check
     * @param stack
     *          the stack of typed parameters so far
     * @return whether the string is a properly formatted java type
     */
    private boolean validateComplexType(String typeString, Stack<String> stack) {
        if (typeString.isEmpty()) {
            // Since there is nothing left to parse, there should be
            // exactly one item on the stack left to verify, if there
            // are more or less, it means the string is not valid
            return stack.size() == 1 && validateClassName(stack.pop(), false);
        } else if (POP_PATTERN.matcher(typeString.substring(0, 1)).matches()) {
            // If we've hit a closing tag '>' or a ',', it
            // signifies we should pop an element of the stack
            // Thus we validate that item, and remove the pop indicator
            //
            // The typeString will look like ",Something>" or ">..."
            String newTypeString = typeString.substring(1);
            return validateClassName(stack.pop(), false)
                    && validateComplexType(newTypeString, stack);
        } else {
            // This means we are adding something needs to be added to the stack
            // because we are not done, and we are not removing anything from the stack
            //
            // The typeString will look like "Something..."
            String strToAddToStack;
            int leftIndex = !typeString.contains("<") ? Integer.MAX_VALUE : typeString.indexOf("<");
            int rightIndex = !typeString.contains(">") ? Integer.MAX_VALUE : typeString.indexOf(">");
            int commaIndex = !typeString.contains(",") ? Integer.MAX_VALUE : typeString.indexOf(",");

            // We just need to get the text between the next indicator,
            // whether that's an "<", ">", or "," doesn't matter now,
            // we will be adding something to the stack regardless
            if (leftIndex < commaIndex && leftIndex < rightIndex) {
                // Since "<" isn't a pop indicator, we don't want to keep it
                strToAddToStack = typeString.substring(0, leftIndex);
                typeString = typeString.substring(leftIndex + 1);
            } else if (rightIndex < commaIndex) {
                // Since ">" is a pop indicator, we want to keep it
                strToAddToStack = typeString.substring(0, rightIndex);
                typeString = typeString.substring(rightIndex);
            } else if (commaIndex != Integer.MAX_VALUE){
                // Since "," is a pop indicator, we want to keep it
                strToAddToStack = typeString.substring(0, commaIndex);
                typeString = typeString.substring(commaIndex);
            } else {
                // Since we are dealing with typed parameters in this function,
                // there will never be an instance for a valid string where
                // the string isn't empty and there are no "<", ">", or ","s left
                return false;
            }

            // Add the new string and recurse
            stack.add(strToAddToStack);
            return validateComplexType(typeString, stack);
        }
    }

    /**
     * Validates that the string provided is a proper class. This only works
     * for non-parameterized types.
     *
     * Examples:
     * <pre>
     *     Integer, false ->                true
     *     integer, false ->                false
     *     int, true ->                     true
     *     int, false ->                    false
     *     Something<Something>, false      false -> all parameterized types will be false
     * </pre>
     *
     * @param str
     *          string to check
     * @param allowForPrimitives
     *          whether to allow primitives such as "int", "float", etc.
     * @return whether the string is a valid class name
     */
    private boolean validateClassName(String str, boolean allowForPrimitives) {
        return StringUtils.isNotBlank(str)
                && allowForPrimitives
                    ? Regex.JAVA_CLASS_NAME_WITH_PRIMITIVES.matcher(str).matches()
                    : Regex.JAVA_CLASS_NAME.matcher(str).matches();
    }
}
