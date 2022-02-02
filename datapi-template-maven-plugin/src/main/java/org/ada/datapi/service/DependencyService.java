package org.ada.datapi.service;

import org.ada.datapi.data.StoredJavaType;
import org.ada.datapi.domain.Dependency;
import org.ada.datapi.exception.EnumSearchException;
import org.ada.datapi.exception.DatapiException;
import org.ada.datapi.util.PathUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static java.util.Objects.nonNull;

@AllArgsConstructor
public class DependencyService {

    private final SearchService searchService;

    /**
     * Fetches the dependency for the given class name, if one is required.
     * Supports only single, non-typed classes. So Class will fetch correctly,
     * Class<Other> will not. This is not a case sensitive search.
     *
     * @param className
     *          name of class to find a dependency for.
     * @return the dependency of the class, null if none
     */
    public Dependency fetchDependency(String className) {
        if (StringUtils.isBlank(className)) {
            return null;
        }

        StoredJavaType storedJavaType = null;
        try {
            storedJavaType = StoredJavaType.getStoredJavaType(className);
        } catch (EnumSearchException e) {
            // This really isn't an error in this case. The type
            // isn't guaranteed to be a stored type here
        }

        // We are making a safe assumptions here:
        //      We can use primitives regardless of configuration here.
        //      Since we are getting the dependencies, there are no
        //      objects related to primitives that need to be imported
        return nonNull(storedJavaType)
                ? storedJavaType.getType(true).getDependency() : this.fetchCustomDependency(className);
    }

    /**
     * Finds a dependency that is not part of the java language. This searches
     * for any classes that have been defined in the project.
     *
     * @param className
     *          class for which to find a dependency
     * @return the dependency for the class, null if not found
     */
    private Dependency fetchCustomDependency(String className) {
        List<String> matchingClassPaths = this.searchService.findClassesWithName(className);

        if (matchingClassPaths.size() > 1) {
            String possibleChoices = StringUtils.join(matchingClassPaths, ",\n\t");
            throw new DatapiException("Could not decide which type to import. Options: " + possibleChoices);
        } else if (matchingClassPaths.isEmpty()) {
            throw new DatapiException("Could not find custom type: " + className);
        }

        String extensionlessClassPath = FilenameUtils.removeExtension(matchingClassPaths.get(0));
        return new Dependency(PathUtil.pathToPackage(extensionlessClassPath));
    }

}
