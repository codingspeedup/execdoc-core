package io.github.codingspeedup.execdoc.reporters.xlsxdiff;

import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffEntry;
import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffEntryCell;
import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffEntryRow;
import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffEntrySheet;

public class XlsxDiffComparator implements java.util.Comparator<XlsxDiffEntry> {

    @Override
    public int compare(XlsxDiffEntry d1, XlsxDiffEntry d2) {
        if (d1.getClass().equals(XlsxDiffEntrySheet.class)) {
            if (d2.getClass().equals(XlsxDiffEntrySheet.class)) {
                int cmp = compareByOperation(d1, d2);
                if (cmp == 0) {
                    cmp = compareBySheetName(d1, d2);
                }
                return cmp;
            }
            return -1;
        }
        if (d1.getClass().equals(XlsxDiffEntryRow.class)) {
            if (d2.getClass().equals(XlsxDiffEntrySheet.class)) {
                return 1;
            }
            int cmp = compareBySheetName(d1, d2);
            if (cmp == 0) {
                if (d2.getClass().equals(XlsxDiffEntryCell.class)) {
                    return -1;
                }
                cmp = compareByOperation(d1, d2);
                if (cmp == 0) {
                    if (d1.isRemoved()) {
                        cmp = Float.compare(((XlsxDiffEntryRow) d1).getLeftRowIdx(), ((XlsxDiffEntryRow) d2).getLeftRowIdx());
                    } else {
                        cmp = Float.compare(((XlsxDiffEntryRow) d1).getRightRowIdx(), ((XlsxDiffEntryRow) d2).getRightRowIdx());
                    }
                }
            }
            return cmp;
        }
        if (d2.getClass().equals(XlsxDiffEntrySheet.class)) {
            return 1;
        }
        int cmp = compareBySheetName(d1, d2);
        if (cmp == 0) {
            if (d2.getClass().equals(XlsxDiffEntryRow.class)) {
                return 1;
            }
            cmp = Float.compare(((XlsxDiffEntryRow) d1).getLeftRowIdx(), ((XlsxDiffEntryRow) d2).getRightRowIdx());
            if (cmp == 0) {
                cmp = compareByOperation(d1, d2);
                if (cmp == 0) {
                    if (d1.isRemoved()) {
                        cmp = Float.compare(((XlsxDiffEntryCell) d1).getLeftColIdx(), ((XlsxDiffEntryCell) d2).getLeftColIdx());
                    } else {
                        cmp = Float.compare(((XlsxDiffEntryCell) d1).getRightColIdx(), ((XlsxDiffEntryCell) d2).getRightColIdx());
                    }
                }
            }
        }
        return cmp;
    }

    private int compareByOperation(XlsxDiffEntry d1, XlsxDiffEntry d2) {
        int op1 = d1.getOperation();
        int op2 = d2.getOperation();
        if (op1 > 0) {
            return op2 > 0 ? 0 : -1;
        }
        if (op1 == 0) {
            return op2 == 0 ? 0 : 1;
        }
        if (op2 == 0) {
            return -1;
        }
        return op2;
    }

    private int compareBySheetName(XlsxDiffEntry d1, XlsxDiffEntry d2) {
        return d1.getSheetName().compareTo(d2.getSheetName());
    }

}
