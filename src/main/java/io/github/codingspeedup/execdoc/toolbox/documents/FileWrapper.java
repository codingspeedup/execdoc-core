package io.github.codingspeedup.execdoc.toolbox.documents;

import io.github.codingspeedup.execdoc.toolbox.files.BinaryFile;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.files.TextFile;

import java.io.File;

public abstract class FileWrapper {

    private File wrappedFile;

    protected FileWrapper() {
    }

    public FileWrapper(File file) {
        open(file);
    }

    protected static BinaryFile asBinaryFile(File file) {
        if (file instanceof BinaryFile) {
            return (BinaryFile) file;
        }
        return new BinaryFile(file);
    }

    protected static TextFile asTextFile(File file) {
        if (file instanceof TextFile) {
            return (TextFile) file;
        }
        return new TextFile(file);
    }

    protected static Folder asFileParent(File file) {
        if (file instanceof Folder) {
            return (Folder) file;
        }
        return new Folder(file);
    }

    public File getFile() {
        return wrappedFile;
    }

    protected void open(File file) {
        this.wrappedFile = file;
        if (file.exists()) {
            this.wrappedFile = checkCreate(wrappedFile);
            loadFromWrappedFile();
        }
    }

    public final void save() {
        this.wrappedFile = checkCreate(wrappedFile);
        saveToWrappedFile();
    }

    protected void saveAs(File file) {
        this.wrappedFile = file;
        save();
    }

    protected abstract File checkCreate(File file);

    protected abstract void loadFromWrappedFile();

    protected abstract void saveToWrappedFile();

}
