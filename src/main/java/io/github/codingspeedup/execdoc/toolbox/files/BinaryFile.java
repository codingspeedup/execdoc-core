package io.github.codingspeedup.execdoc.toolbox.files;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class BinaryFile extends FileLeaf {

    public BinaryFile(File file) {
        super(file);
    }

    public static BinaryFile of(File file) {
        return new BinaryFile(file);
    }

    @SneakyThrows
    public byte[] readToByteArray() {
        return FileUtils.readFileToByteArray(this);
    }

    @SneakyThrows
    public void writeFromByteArray(byte[] content) {
        FileUtils.writeByteArrayToFile(this, content);
    }

}
