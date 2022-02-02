package org.ada.datapi.util;

import org.ada.datapi.exception.DatapiException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class PathUtil {

    /**
     * Creates a path with the given separator as the separator and removes any
     * leading/trailing separators.
     *
     * Example
     *      "/src/main/Something.java", "." ->      src.main.Something.java
     *      "/src/main/", "/" ->                    src/main
     *
     * @param path
     *          string to normalize
     * @param separator
     *          separator to use
     * @return the normalized path
     */
    public static String normalize(String path, String separator) {
        return FilenameUtils.separatorsToUnix(path)
                .replaceAll("[/]", " ")
                .replaceAll(" +", " ")
                .trim()
                .replaceAll(" ", separator);
    }

    public static String joinPath(String... paths) {
        return normalize(StringUtils.join(paths, "/"), "/");
    }

    /**
     * Converts a fully qualified path to a java package.
     *
     * Example
     *
     *      "somewhere/src/main/java/com/example/Something.java -> com.example.Something.java
     *
     * @param systemPath
     *          path on system
     * @param basePath
     *          path which signifies all after it is what is needed by java to qualify package
     * @return path that Java can use to find resource
     */
    public static String pathToPackage(String systemPath, String basePath) {
        if (StringUtils.isEmpty(systemPath)) {
            return systemPath;
        }

        // The following code takes a full system path
        //      ie "project/module/src/main/java/com/example/Something.java"
        // and converts it into the portion needed for an import
        //      ie "com/example/Something.java
        // also converts all paths to unix style
        String unixSystemPath = FilenameUtils.removeExtension(FilenameUtils.separatorsToUnix(systemPath));
        String unixBasePath = FilenameUtils.separatorsToUnix(basePath);
        String[] parts = unixSystemPath.split(unixBasePath);
        if (parts.length == 1) {
            throw new DatapiException(
                    "Could not convert path ['" + systemPath + "'] into Java path. Does not contain project root");
        }

        return pathToPackage(parts[1]);
    }

    public static String pathToPackage(String systemPath) {
        return normalize(systemPath, ".");
    }

}
