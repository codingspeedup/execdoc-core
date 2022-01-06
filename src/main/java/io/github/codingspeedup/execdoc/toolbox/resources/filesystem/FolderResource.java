package io.github.codingspeedup.execdoc.toolbox.resources.filesystem;

public class FolderResource extends FileOrFolderResource {

    public FolderResource(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "(D) " + super.toString();
    }

}
