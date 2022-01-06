package io.github.codingspeedup.execdoc.miners.diff.xlsx;

import lombok.Getter;

public class XlsxDiffEntryRow extends XlsxDiffEntry {

    @Getter
    private final float leftRowIdx;
    @Getter
    private final float rightRowIdx;

    public XlsxDiffEntryRow(int operation, String sheetName, float leftRowIdx, float rightRowIdx) {
        super(operation, sheetName);
        this.leftRowIdx = leftRowIdx;
        this.rightRowIdx = rightRowIdx;
    }

    public static String getRowName(float rowIdx) {
        int idx = (int) rowIdx;
        String name = String.valueOf(idx + 1);
        if (idx != rowIdx) {
            name += "*";
        }
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isAdded()) {
            sb.append("+");
        } else if (isRemoved()) {
            sb.append("-");
        } else {
            sb.append("*");
        }
        sb.append(" ROW   ");
        if (isAdded()) {
            sb.append(getRowName(rightRowIdx));
        } else if (isRemoved()) {
            sb.append(getRowName(leftRowIdx));
        }
        return sb.toString();
    }

}
