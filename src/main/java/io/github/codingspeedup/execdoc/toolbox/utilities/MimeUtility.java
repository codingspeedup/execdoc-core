package io.github.codingspeedup.execdoc.toolbox.utilities;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MimeUtility {

    private static final Tika tika = new Tika();

    private static final Set<String> IMAGE_MARKERS = new HashSet<>(Arrays.asList("image", "postscript"));
    private static final Set<String> TEXT_MARKERS = new HashSet<>(Arrays.asList("text", "javascript", "json", "xml"));

    public static String guessMimeType(String name, byte[] data) {
        return tika.detect(data, name);
    }

    @SneakyThrows
    public static String guessMimeType(File file) {
        return tika.detect(file);
    }

    public static boolean isText(String mimeType) {
        if (StringUtils.isBlank(mimeType)) {
            return false;
        }
        Set<String> description = new HashSet<>(Arrays.asList(mimeType.split("/")));
        return description.removeAll(TEXT_MARKERS);
    }

    public static boolean isImage(String mimeType) {
        if (StringUtils.isBlank(mimeType)) {
            return false;
        }
        Set<String> description = new HashSet<>(Arrays.asList(mimeType.split("/")));
        return description.removeAll(IMAGE_MARKERS);
    }

    public static boolean isXlsx(String mimeType) {
        if (StringUtils.isBlank(mimeType)) {
            return false;
        }
        return mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

}
