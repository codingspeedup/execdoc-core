package io.github.codingspeedup.execdoc.toolbox.utilities;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileUtility {

    public static List<String> readLines(File file) throws IOException {
        return FileUtils.readLines(file, StandardCharsets.UTF_8);
    }

}
