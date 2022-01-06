package io.github.codingspeedup.execdoc.toolbox.utilities;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class NamingUtility {

    public static String toTypeLabel(Class<?> typeClass) {
        String simpleName = typeClass.getSimpleName();
        return simpleName.substring(0, simpleName.lastIndexOf(CellMarkers.TYPE_SUFFIX));
    }

    public static String toUpperUnderscore(String name) {
        if (name != null) {
            name = name.trim();
            String uppercaseName = name.toUpperCase(Locale.ROOT);
            if (!name.equals(uppercaseName)) {
                if (name.contains("_")) {
                    name = uppercaseName;
                } else {
                    name = StringUtils.capitalize(name);
                    name = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);
                }
            }
        }
        return name;
    }

    public static String toLowerUnderscore(String name) {
        if (name != null) {
            name = name.trim();
            String lowercaseName = name.toLowerCase(Locale.ROOT);
            if (!name.equals(lowercaseName)) {
                if (name.contains("_")) {
                    name = lowercaseName;
                } else {
                    name = StringUtils.capitalize(name);
                    name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
                }
            }
        }
        return name;
    }

    public static String toUpperCamel(String name) {
        if (name != null) {
            name = name.trim();
            if (name.contains("_")) {
                name = name.toUpperCase(Locale.ROOT);
                name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
            } else {
                name = StringUtils.capitalize(name);
            }
        }
        return name;
    }

    public static String toLowerCamel(String name) {
        if (name != null) {
            name = name.trim();
            if (name.contains("_")) {
                name = name.toUpperCase(Locale.ROOT);
                name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
            } else {
                name = StringUtils.uncapitalize(name);
            }
        }
        return name;
    }

}
