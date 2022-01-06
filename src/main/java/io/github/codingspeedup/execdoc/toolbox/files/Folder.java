package io.github.codingspeedup.execdoc.toolbox.files;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Folder extends FileNode {

    public Folder(File file) {
        super(file);
        if (!file.exists()) {
            mkdirsOrThrow();
        } else if (!file.isDirectory()) {
            throw new UnsupportedOperationException(file.toPath() + " is not a " + this.getClass().getSimpleName());
        }
    }

    public static Folder of(File file) {
        return new Folder(file);
    }

    public static Folder of(String path) {
        return of(new File(path));
    }

    private void mkdirsOrThrow() {
        if (!exists()) {
            if (!mkdirs() && !exists()) {
                throw new UnsupportedOperationException("Cannot create directory " + toPath());
            }
        }
    }

    public void deleteContent() throws IOException {
        if (exists()) {
            FileUtils.forceDelete(this);
            mkdirsOrThrow();
        }
    }

}
