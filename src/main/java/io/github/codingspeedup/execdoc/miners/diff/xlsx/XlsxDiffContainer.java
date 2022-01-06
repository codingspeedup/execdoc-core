package io.github.codingspeedup.execdoc.miners.diff.xlsx;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class XlsxDiffContainer {

    @Getter
    private final List<XlsxDiffEntry> diffs = new ArrayList<>();

    public void addSheetDiff(String lSheet, String rSheet) {
        if (StringUtils.isBlank(lSheet)) {
            diffs.add(new XlsxDiffEntrySheet(1, rSheet));
        } else if (StringUtils.isBlank(rSheet)) {
            diffs.add(new XlsxDiffEntrySheet(-1, lSheet));
        }
    }

    public void addRowDiff(String lSheet, int leftRowIdx, String rSheet, int rightRowIdx) {
        if (StringUtils.isBlank(lSheet)) {
            diffs.add(new XlsxDiffEntryRow(1, rSheet, leftRowIdx + 0.5f, rightRowIdx));
        } else if (StringUtils.isBlank(rSheet)) {
            diffs.add(new XlsxDiffEntryRow(-1, lSheet, leftRowIdx, rightRowIdx + 0.5f));
        }
    }

    public void addCellDiff(String sheetName,
                            int leftRowIdx, int rightRowIdx,
                            int leftColIdx, int rightColIdx,
                            String leftValue, String rightValue) {
        if (leftValue == null) {
            diffs.add(new XlsxDiffEntryCell(+1, sheetName,
                    leftRowIdx, leftColIdx + 0.5f,
                    rightRowIdx, rightColIdx));
        } else if (rightValue == null) {
            diffs.add(new XlsxDiffEntryCell(-1, sheetName,
                    leftRowIdx, leftColIdx,
                    rightRowIdx, rightColIdx + 0.5f));
        } else {
            diffs.add(new XlsxDiffEntryCell(0, sheetName,
                    leftRowIdx, leftColIdx,
                    rightRowIdx, rightColIdx));
        }
    }

    @Override
    public String toString() {
        StringBuilder rep = new StringBuilder();
        for (XlsxDiffEntry diff : diffs) {
            rep.append(diff.toString()).append("\n");
        }
        return rep.toString();
    }

}
