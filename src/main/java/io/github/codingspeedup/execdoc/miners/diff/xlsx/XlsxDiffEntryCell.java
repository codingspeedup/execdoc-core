package io.github.codingspeedup.execdoc.miners.diff.xlsx;

import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.Getter;

public class XlsxDiffEntryCell extends XlsxDiffEntryRow {

    @Getter
    private final float leftColIdx;
    @Getter
    private final float rightColIdx;

    public XlsxDiffEntryCell(int operation, String sheetName,
                             int leftRowIdx, float leftColIdx,
                             int rightRowIdx, float rightColIdx) {
        super(operation, sheetName, leftRowIdx, rightRowIdx);
        this.leftColIdx = leftColIdx;
        this.rightColIdx = rightColIdx;
    }

    public static String getCellName(float rowIdx, float colIdx) {
        int idx = (int) colIdx;
        String name = XlsxUtil.columnIndexToName(idx);
        if (idx != colIdx) {
            name += "*";
        }
        return name + "." + getRowName(rowIdx);
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
        sb.append(" CELL  ");
        if (isAdded()) {
            sb.append(getCellName(getRightRowIdx(), rightColIdx));
        } else if (isRemoved()) {
            sb.append(getCellName(getLeftRowIdx(), leftColIdx));
        } else {
            sb.append(getCellName(getLeftRowIdx(), rightColIdx));
        }
        return sb.toString();
    }

}
