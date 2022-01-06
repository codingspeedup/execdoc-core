package io.github.codingspeedup.execdoc.toolbox.files;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class TextFile extends BinaryFile {

    public TextFile(File file) {
        super(file);
    }

    public static TextFile of(File file) {
        return new TextFile(file);
    }

    @SneakyThrows
    public String readContentAsString() {
        return FileUtils.readFileToString(this, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public void writeStringToContent(String content) {
        FileUtils.writeStringToFile(this, content, StandardCharsets.UTF_8);
    }

}
