package org.ada.datapi.template;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class Template {

    private static final String GETTER_TEMPLATE =
                    "\tpublic %s get%s() {\n" +
                    "\t\treturn this.%s;\n" +
                    "\t}\n";

    private static final String SETTER_TEMPLATE =
                    "\tpublic void set%s(%s %s) {\n" +
                    "\t\tthis.%s = %s;\n" +
                    "\t}\n";

    public static String getGetterTemplate(String className, String varName) {
        return String.format(GETTER_TEMPLATE, className, StringUtils.capitalize(varName), varName);
    }

    public static String getSetterTemplate(String className, String varName) {
        return String.format(SETTER_TEMPLATE, StringUtils.capitalize(varName), className, varName, varName, varName);
    }
}
