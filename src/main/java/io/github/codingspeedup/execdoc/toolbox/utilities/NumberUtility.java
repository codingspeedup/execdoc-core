package io.github.codingspeedup.execdoc.toolbox.utilities;

public class NumberUtility {

    public static int toIntOrZero(Number value) {
        return value == null ? 0 : value.intValue();
    }

    public static String toStringOrNull(Number value) {
        return value == null ? null : value.toString();
    }

}
