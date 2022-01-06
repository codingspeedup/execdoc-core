package io.github.codingspeedup.execdoc.toolbox.files;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

public class FileNode extends File {

    public FileNode(String pathname) {
        super(pathname);
    }

    public FileNode(URI uri) {
        super(uri);
    }

    public FileNode(File file) {
        super(file, "");
    }

    public FileNode(Path path) {
        this(path.toFile());
    }

    public static File extend(File file, Object... path) {
        if (path == null) {
            return file;
        }
        File extended = file;
        for (Object step : path) {
            if (step instanceof String) {
                if (StringUtils.isNotBlank((String) step)) {
                    String[] subSteps = ((String) step).split("/");
                    for (String subStep : subSteps) {
                        extended = new File(extended, subStep);
                    }
                }
            } else if (step instanceof Package) {
                String[] subSteps = ((Package) step).getName().split("\\.");
                for (String subStep : subSteps) {
                    extended = new File(extended, subStep);
                }
            }
        }
        return extended;
    }

    @SneakyThrows
    public String getCanonicalPath() {
        return super.getCanonicalPath();
    }

    public Folder asFolder() {
        return new Folder(this);
    }

}
