package io.github.codingspeedup.execdoc.reporters.xlsxdiff;

import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffContainer;
import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffEntry;
import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffEntryCell;
import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffEntryRow;

public class XlsxDiffReporter {

    public String getReport(XlsxDiffContainer diffs) {
        StringBuilder report = new StringBuilder();
        if (!diffs.getDiffs().isEmpty()) {
            diffs.getDiffs().sort(new XlsxDiffComparator());
            String currentSheet = "";
            // float currentRow = -1;
            for (XlsxDiffEntry diff : diffs.getDiffs()) {
                if (diff instanceof XlsxDiffEntryCell) {
                    if (!currentSheet.equals(diff.getSheetName())) {
                        report.append("* [").append(diff.getSheetName()).append("]\n");
                    }
                    report.append("        ");
                    if (diff.isAdded()) {
                        report.append("+ CELL ").append(XlsxDiffEntryCell.getCellName(((XlsxDiffEntryCell) diff).getRightRowIdx(), ((XlsxDiffEntryCell) diff).getRightColIdx()));
                    } else if (diff.isRemoved()) {
                        report.append("- CELL ").append(XlsxDiffEntryCell.getCellName(((XlsxDiffEntryCell) diff).getLeftRowIdx(), ((XlsxDiffEntryCell) diff).getLeftColIdx()));
                    } else {
                        report.append("* CELL ").append(XlsxDiffEntryCell.getCellName(((XlsxDiffEntryCell) diff).getLeftRowIdx(), ((XlsxDiffEntryCell) diff).getRightColIdx()));
                    }
                    report.append("\n");
                } else if (diff instanceof XlsxDiffEntryRow) {
                    if (!currentSheet.equals(diff.getSheetName())) {
                        report.append("* [").append(diff.getSheetName()).append("]\n");
                    }
                    report.append("    ");
                    if (diff.isAdded()) {
                        report.append("+ ROW ").append(XlsxDiffEntryRow.getRowName(((XlsxDiffEntryRow) diff).getRightRowIdx()));
                    } else if (diff.isRemoved()) {
                        report.append("- ROW ").append(XlsxDiffEntryRow.getRowName(((XlsxDiffEntryRow) diff).getLeftRowIdx()));
                    }
                    report.append("\n");
                } else {
                    if (diff.isAdded()) {
                        report.append("+ [");
                    } else if (diff.isRemoved()) {
                        report.append("- [");
                    }
                    report.append(diff.getSheetName()).append("]\n");
                }
                currentSheet = diff.getSheetName();
            }
        } else {
            report.append("No differences found");
        }
        return report.toString();
    }

}
