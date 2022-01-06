package io.github.codingspeedup.execdoc.toolbox.documents;

import io.github.codingspeedup.execdoc.toolbox.files.Folder;

import java.io.File;

public abstract class FolderWrapper extends FileWrapper {

    public FolderWrapper(File file) {
        super(file);
    }

    public Folder getDirectory() {
        return asFileParent(getFile());
    }

    @Override
    protected File checkCreate(File file) {
        return FileWrapper.asFileParent(file);
    }

    @Override
    protected void loadFromWrappedFile() {
        // by default there is nothing to load from the directory
    }

    @Override
    protected void saveToWrappedFile() {
        // by default creating the directory should be enough
    }

}
