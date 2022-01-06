package io.github.codingspeedup.execdoc.toolbox.documents;

import io.github.codingspeedup.execdoc.toolbox.files.TextFile;

import java.io.File;

public abstract class TextFileWrapper extends FileWrapper {

    protected TextFileWrapper() {
    }

    public TextFileWrapper(File file) {
        open(file);
    }

    public TextFile getTextFile() {
        return asTextFile(getFile());
    }

    @Override
    public final void open(File file) {
        super.open(file);
    }

    @Override
    public final void saveAs(File file) {
        super.saveAs(file);
    }

    @Override
    protected File checkCreate(File file) {
        return asTextFile(file);
    }

    @Override
    protected final void saveToWrappedFile() {
        String textContent = toString();
        if (textContent == null) {
            throw new NullPointerException("Empty file content");
        }
        getTextFile().writeStringToContent(textContent);
    }

}
