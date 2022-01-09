package io.github.codingspeedup.execdoc.toolbox.utilities;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class StringUtility {

    private static final URLCodec URL_CODEC = new URLCodec();

    public static String[] splitLines(String string) {
        return string.split("\\r?\\n");
    }

    public static String toBasicL10nKey(String label) {
        label = StringUtils.stripAccents(label).toLowerCase(Locale.ROOT);
        label = label.replaceAll("[^a-z0-9\\s]", "");
        label = label.trim().replaceAll("\\s+", "-");
        int maxLength = 76;
        if (label.length() > maxLength) {
            if (label.charAt(maxLength - 1) == '-') {
                --maxLength;
            }
            label = label.substring(0, maxLength);
        }
        return label;
    }

    public static String encodeBase64(byte[] byteArray) {
        return Base64.encodeBase64String(byteArray);
    }

    public static byte[] decodeBase64(String base64Encoding) {
        return Base64.decodeBase64(base64Encoding);
    }

    @SneakyThrows
    public static String urlEncode(String value) {
        return URL_CODEC.encode(value);
    }

    public static String simpleQuote(String string) {
        if (string == null) {
            return  null;
        }
        if (string.contains("'")) {
            throw new UnsupportedOperationException("Escaping \"'\" is not specified");
        }
        return "'" + string + "'";
    }

}
