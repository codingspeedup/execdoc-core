package io.github.codingspeedup.execdoc.toolbox.resources.filesystem;

public class FileResource extends FileOrFolderResource {

    public FileResource(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "(F) " + super.toString();
    }

}
