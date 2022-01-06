package io.github.codingspeedup.execdoc.miners.diff.xlsx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XlsxDiffEntrySheet extends XlsxDiffEntry {

    public XlsxDiffEntrySheet(int operation, String sheetName) {
        super(operation, sheetName);
    }

    public boolean isLeft() {
        return isRemoved();
    }

    public boolean isRight() {
        return isAdded();
    }

    @Override
    public String toString() {
        if (isAdded()) {
            return "+ SHEET " + getSheetName();
        } else if (isRemoved()) {
            return "- SHEET " + getSheetName();
        } else {
            return "* SHEET " + getSheetName();
        }
    }

}
