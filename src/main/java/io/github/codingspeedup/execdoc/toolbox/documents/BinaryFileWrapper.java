package io.github.codingspeedup.execdoc.toolbox.documents;

import io.github.codingspeedup.execdoc.toolbox.files.BinaryFile;

import java.io.File;
import java.io.IOException;

public abstract class BinaryFileWrapper extends FileWrapper {

    protected BinaryFileWrapper() {
    }

    public BinaryFileWrapper(File file) {
        super(file);
    }

    public BinaryFile getBinaryFile() {
        return asBinaryFile(getFile());
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
        return FileWrapper.asBinaryFile(file);
    }

    public abstract byte[] toByteArray() throws IOException;

}
