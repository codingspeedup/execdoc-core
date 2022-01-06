package io.github.codingspeedup.execdoc.toolbox.files;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class FileLeaf extends FileNode {

    @SneakyThrows
    public FileLeaf(File file) {
        super(file);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.mkdirs() && !parentFile.exists()) {
                throw new UnsupportedOperationException("Cannot create parent for " + file.toPath());
            }
            if (!file.createNewFile() && !file.exists()) {
                throw new UnsupportedOperationException("Cannot create file " + file.toPath());
            }
        } else if (!file.isFile()) {
            throw new UnsupportedOperationException(file.toPath() + " is not a " + this.getClass().getSimpleName());
        }
    }

    public String getBaseName() {
        return FilenameUtils.getBaseName(getName());
    }

    public String getExtension() {
        return FilenameUtils.getExtension(getName());
    }

}
