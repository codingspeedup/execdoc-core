package io.github.codingspeedup.execdoc.toolbox.resources.filesystem;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public abstract class FileOrFolderResource extends Resource {

    public FileOrFolderResource(String description) {
        super(description);
    }

    public File getFile() {
        return new File(getDescription());
    }

    @Override
    public String getName() {
        return FilenameUtils.getName(getDescription());
    }

}
