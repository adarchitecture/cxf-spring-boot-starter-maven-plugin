package org.ada.datapi.util;

import com.google.common.collect.Lists;
import org.ada.datapi.domain.Type;
import org.ada.datapi.exception.DatapiException;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class TypeUtil {

    private static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    /**
     * Converts potentially weirdly-formed, yet valid type declarations into something
     * standard.
     *
     * Example:
     *
     *      "map<  string,Integer>" -> "Map<String, Integer>"
     *
     * @param typeString
     *          string to format
     * @return the formatted string
     */
    public static String normalizeTypeString(String typeString) {
        if (StringUtils.isBlank(typeString)) {
            return "";
        }

        return streamTypesWithSplitChars(typeString)
                .map(TypeUtil::normalizeSingleType)
                .collect(Collectors.joining()).replaceAll(",", ", ");
    }

    /**
     * This transforms the text given into a variable name that follows standard Java practices
     *
     * Example:
     *
     *      someVar -> someVar
     *      some_var -> someVar
     *      SomeVar -> someVar
     *      Some_Var -> someVar
     *
     * @param variableName
     *          name to change
     * @return java variable name
     */
    public static String normalizeVariableName(String variableName) {
        if (StringUtils.isBlank(variableName)) {
            return "";
        }

        String varName = Arrays.stream(variableName.split("[_ ]"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(""));

        return StringUtils.uncapitalize(varName).replaceAll(" ", "");
    }

    /**
     * Takes in a type string and returns the types found withing
     *
     * Example:
     *
     *  Map<String, Integer> -> [Map, String, Integer]
     *
     * @param typeString
     *          string from which to extract type strings
     * @return the list of types found within the given string
     */
    public static List<String> splitToIndividualTypes(String typeString) {
        if (StringUtils.isBlank(typeString)) {
            return Lists.newArrayList();
        }

        // Map<String, List<Something>> -> Map String List Something
        return Arrays.asList(typeString
                .replaceAll("[<>,]", " ")
                .replaceAll(" +", " ")
                .trim()
                .split(" "));
    }

    /**
     * Best way to describe this is by example:
     *
     * typeString = "Map<int, str>
     * types = [Type("Map"), Type("Integer"), Type("String")]
     * return = Map<Integer, String>
     *
     * @param typeString
     *          string with potentially wrong names
     * @param types
     *          list of types containing proper type names
     * @return correct type string
     */
    public static String mergeTypeStringAndListOfTypes(String typeString, List<Type> types) {
        if (StringUtils.isBlank(typeString)) {
            return "";
        } else if (CollectionUtils.isEmpty(types)) {
            throw new DatapiException(String.format("Cannot merge ['%s'} with empty list", typeString));
        }

        List<String> splitStrings = streamTypesWithSplitChars(typeString).collect(Collectors.toList());

        int typeIndex = 0;
        StringBuilder finalString = new StringBuilder();
        for (String splitString : splitStrings) {
            // if the string is a splitting character, we can't replace it with
            // something from type, it is fine, just put it back into the final string
            String strToAdd = splitString.matches("[<,>]") ? splitString : types.get(typeIndex++).getClassName();
            finalString.append(strToAdd);
        }

        return normalizeTypeString(finalString.toString());
    }

    private static String normalizeSingleType(String type) {
        if (type.matches("[<,>]")) {
            return type;
        }
        return StringUtils.capitalize(normalizeVariableName(type));
    }

    private static Stream<String> streamTypesWithSplitChars(String typeString) {
        String noSpacesString = typeString.replaceAll(" ", "");

        return Arrays.stream(noSpacesString.split(String.format(WITH_DELIMITER, "<")))
                .map(s -> s.split(String.format(WITH_DELIMITER, ",")))
                .flatMap(Arrays::stream)
                .map(s -> s.split(String.format(WITH_DELIMITER, ">")))
                .flatMap(Arrays::stream);
    }

}
