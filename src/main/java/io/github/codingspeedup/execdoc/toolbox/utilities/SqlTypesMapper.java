package io.github.codingspeedup.execdoc.toolbox.utilities;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

public class SqlTypesMapper {

    @SneakyThrows
    public static String toTypesName(int type) {
        for (Field field : java.sql.Types.class.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers) && int.class.equals(field.getType())) {
                if (field.get(null).equals(type)) {
                    return field.getName();
                }
            }
        }
        return null;
    }

    @SneakyThrows
    public static Integer toTypesValue(String type) {
        type = StringUtils.trimToEmpty(type).toUpperCase(Locale.ROOT);
        try {
            return (Integer) java.sql.Types.class.getDeclaredField(type).get(null);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}
