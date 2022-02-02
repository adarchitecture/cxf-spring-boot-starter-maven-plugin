package org.ada.datapi.data;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class Regex {

    private static final String className = "([A-Z+][a-zA-Z0-9]*)";

    private static final String primitives = "(int|float|double|long|short|byte|char|boolean)";

    public static final Pattern JAVA_CLASS_NAME = Pattern.compile(className);

    public static final Pattern JAVA_CLASS_NAME_WITH_PRIMITIVES = Pattern.compile(className + "|" + primitives);

}
