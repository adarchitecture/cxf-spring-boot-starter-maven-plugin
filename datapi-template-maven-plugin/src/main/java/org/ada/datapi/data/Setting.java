package org.ada.datapi.data;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Defines all the keys for the settings that
 * can be configured for this application
 */
public enum Setting {

    /**
     * name:type string defining how to generate the id
     *
     * This is determined via inspecting the plaster.yml file and command line arg
     *
     * Example:
     *
     *      id:string
     */
    KEY("property.key", String.class, "id:int"),

    /**
     * boolean string which signifies whether or not to use primitive types when possible.
     * If enabled, Integer
     *
     * This is determined by inspecting the plaster.yml file
     */
    SHOULD_USE_PRIMITIVES("property.enablePrimitives", Boolean.class, false),

    /**
     * boolean string. If true, tests will be generated for the generated classes.
     *
     * This is determined via inspecting the pom and the plaster.yml file
     */
    IS_TESTING_ENABLED("property.enableTesting", Boolean.class, true),

    /**
     * boolean string. If true, will not generate getters and setters, and will rather
     * annotate model class with lombok.
     *
     * This is determined via inspecting the pom and the plaster.yml file
     */
    IS_LOMBOK_ENABLED("lombok.enable", Boolean.class, false,
            Lists.newArrayList("org.projectlombok")),

    /**
     * string defining where the src directory is from the root of the repository.
     * Most commonly and empty string.
     *
     * This is not configurable yet.
     */
    PROJECT_PATH(null, String.class, ""),

    /**
     * string defining the path to get to the app's code.
     *
     * This can be configured via plaster.yml file
     *
     * Example:
     *
     *      src/main/java
     */
    BASE_PATH("directory.base", String.class, "src/main/java"),

    /**
     * string defining the qualified app path. Will be the maven group id in most cases
     *
     * This is determined by inspecting the pom and plaster.yml
     *
     * Example:
     *
     *      com/example/app
     */
    APP_PATH("directory.app", String.class, null),

    /**
     * string defining a path to append to the generation relative paths for the current generation.
     * Meaning, if provided, generation will not occur at rel_*_package, but rather rel_*_package/sub_dir_path
     *
     * This is determined by inspecting command line args
     *
     * Example:
     *
     *      somewhere/different
     */
    SUB_DIR_PATH(null, String.class, ""),

    /**
     * string defining the maven group id for the project.
     *
     * This is determined by inspecting the pom
     *
     * Example:
     *
     *      com.example.app
     */
    MAVEN_GROUP_ID(null, String.class, null),

    /**
     * string defining the name of the project.
     *
     * This is determined by inspection the pom
     *
     * Example:
     *
     *      plaster
     */
    APP_NAME(null, String.class, null),

    /**
     * string defining custom package, from the {@link Setting#APP_PATH} to generate models
     *
     * This is determined by inspecting the plaster.yml file
     *
     * Example:
     *
     *      somewhere/different
     */
    REL_MODEL_PACKAGE("directory.model", String.class, "model"),

    /**
     * string defining custom package, from the {@link Setting#APP_PATH} to generate repositories
     *
     * This is determined by inspecting the plaster.yml file
     *
     * Example:
     *
     *      somewhere/different
     */
    REL_REPOSITORY_PACKAGE("directory.repository", String.class, "repository"),

    /**
     * string defining custom package, from the {@link Setting#APP_PATH} to generate services
     *
     * This is determined by inspecting the plaster.yml file
     *
     * Example:
     *
     *      somewhere/different
     */
    REL_SERVICE_PACKAGE("directory.service", String.class, "service"),

    /**
     * string defining custom package, from the {@link Setting#APP_PATH} to generate controllers
     *
     * This is determined by inspecting the plaster.yml file
     *
     * Example:
     *
     *      somewhere/different
     */
    REL_CONTROLLER_PACKAGE("directory.controller", String.class, "controller");

    public final String compositePath;
    public final Class<?> type;
    public final String defaultVal;
    public final List<String> dependencyFlags;

    <T> Setting(String compositePath, Class<T> type, T defaultVal) {
        this(compositePath, type, defaultVal, Lists.newArrayList());
    }

    <T> Setting(String compositePath, Class<T> type, T defaultVal, List<String> dependencyFlags) {
        this.compositePath = compositePath;
        this.type = type;
        this.defaultVal = nonNull(defaultVal) ? defaultVal.toString() : null;
        this.dependencyFlags = dependencyFlags;
    }

    /**
     * Gets all the settings that enable themselves when a dependency
     * is found in the pom examination
     *
     * @return the configurable settings
     */
    public static List<Setting> getDependentSettings() {
        return Arrays.stream(Setting.values())
                .filter(v -> isNotEmpty(v.dependencyFlags))
                .collect(Collectors.toList());
    }

    /**
     * Gets all the settings that are configurable through the plaster.yml file
     *
     * @return the configurable settings
     */
    public static List<Setting> getConfigurableSettings() {
        return Arrays.stream(Setting.values())
                .filter(v -> nonNull(v.compositePath))
                .collect(Collectors.toList());
    }

    /**
     * Gets all the settings that are defaulted
     *
     * @return the defaulted settings
     */
    public static List<Setting> getDefaultedSettings() {
        return Arrays.stream(Setting.values())
                .filter(v -> nonNull(v.defaultVal))
                .collect(Collectors.toList());
    }
}
