package io.github.codingspeedup.execdoc.miners.diff.xlsx;

import lombok.Getter;

public abstract class XlsxDiffEntry {

    @Getter
    private final String sheetName;
    @Getter
    private final int operation;

    public XlsxDiffEntry(int operation, String sheetName) {
        this.sheetName = sheetName;
        this.operation = operation;
    }

    public boolean isAdded() {
        return operation > 0;
    }

    public boolean isChanged() {
        return operation == 0;
    }

    public boolean isRemoved() {
        return operation < 0;
    }

}
