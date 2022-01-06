package io.github.codingspeedup.execdoc.miners.diff.xlsx;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxDocument;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XlsxDiffMiner {

    public static final String DEL_MARKER = "<";
    public static final String ADD_MARKER = ">";

    private static String clean(String value) {
        if (value == null) {
            return "";
        }
        value = value.replace(ADD_MARKER, "").replace(DEL_MARKER, "");
        return XlsxTexcoder.EMPTY_CELL_MARKER.equals(value) ? "" : value;
    }

    private static String normalize(String line) {
        return line.replace(" <-", "< -").replace(" >-", "> -");
    }

    public XlsxDiffContainer compare(File leftXlsx, File rightXlsx) {
        XlsxDiffContainer diffs = new XlsxDiffContainer();
        XlsxDocument left = new XlsxDocument(leftXlsx);
        XlsxDocument right = new XlsxDocument(rightXlsx);

        List<String> leftSheets = left.getSheetNames();
        List<String> rightSheets = right.getSheetNames();
        Set<String> commonSheets = new HashSet<>();

        for (String rSheet : rightSheets) {
            if (leftSheets.contains(rSheet)) {
                commonSheets.add(rSheet);
            } else {
                diffs.addSheetDiff(null, rSheet);
            }
        }
        for (String lSheet : leftSheets) {
            if (!commonSheets.contains(lSheet)) {
                diffs.addSheetDiff(lSheet, null);
            }
        }
        for (String sheet : commonSheets) {
            Sheet leftSheet = left.getWorkbook().getSheet(sheet);
            Sheet rightSheet = right.getWorkbook().getSheet(sheet);
            compare(diffs, leftSheet, rightSheet);
        }

        return diffs;
    }

    private void compare(XlsxDiffContainer diffs, Sheet leftSheet, Sheet rightSheet) {
        List<String> leftLines = XlsxTexcoder.textcode(leftSheet);
        List<String> rightLines = XlsxTexcoder.textcode(rightSheet);

        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .ignoreWhiteSpaces(true)
                .lineNormalizer(s -> s)
                .oldTag(f -> DEL_MARKER)
                .newTag(f -> ADD_MARKER)
                .build();
        List<DiffRow> rows = generator.generateDiffRows(leftLines, rightLines);

        int leftRowIdx = 0, rightRowIdx = 0;
        for (DiffRow row : rows) {
            switch (row.getTag()) {
                case DELETE:
                    diffs.addRowDiff(leftSheet.getSheetName(), leftRowIdx, null, rightRowIdx);
                    ++leftRowIdx;
                    break;
                case INSERT:
                    diffs.addRowDiff(null, rightRowIdx, rightSheet.getSheetName(), rightRowIdx);
                    ++rightRowIdx;
                    break;
                case CHANGE:
                    compare(diffs, leftSheet.getSheetName(), leftRowIdx, rightRowIdx,
                            normalize(row.getOldLine()), normalize(row.getNewLine()));
                default:
                    ++leftRowIdx;
                    ++rightRowIdx;
            }
        }
    }

    private void compare(XlsxDiffContainer diffs, String sheetName, int leftRowIdx, int rightRowIdx, String oldLine, String newLine) {
        String[] oldCells = oldLine.split(" ");
        String[] newCells = newLine.split(" ");
        int maxCommonColIdx = Math.min(oldCells.length, newCells.length);
        int leftColIdx = 0;
        int rightColIdx = 0;
        for (int colIdx = 0; colIdx < maxCommonColIdx; ++colIdx) {
            leftColIdx = colIdx;
            rightColIdx = colIdx;
            compare(diffs, sheetName, leftRowIdx, rightRowIdx, leftColIdx, rightColIdx, oldCells[colIdx], newCells[colIdx]);
        }
        if (maxCommonColIdx < oldCells.length) {
            for (int colIdx = maxCommonColIdx; colIdx < oldCells.length; ++colIdx) {
                leftColIdx = colIdx;
                compare(diffs, sheetName, leftRowIdx, rightRowIdx, leftColIdx, rightColIdx, oldCells[colIdx], null);
            }
        } else if (maxCommonColIdx < newCells.length) {
            for (int colIdx = maxCommonColIdx; colIdx < newCells.length; ++colIdx) {
                rightColIdx = colIdx;
                compare(diffs, sheetName, leftRowIdx, rightRowIdx, leftColIdx, rightColIdx, null, newCells[colIdx]);
            }
        }
    }

    private void compare(XlsxDiffContainer diffs, String sheetName,
                         int leftRowIdx, int rightRowIdx,
                         int leftColIdx, int rightColIdx,
                         String oldValue, String newValue) {
        String oldValueClean = clean(oldValue);
        String newValueClean = clean(newValue);
        if (oldValueClean.equals(newValueClean)) {
            return;
        }
        if (StringUtils.isBlank(oldValueClean)) {
            diffs.addCellDiff(sheetName, leftRowIdx, rightRowIdx, leftColIdx, rightColIdx, null, newValue);
        } else if (StringUtils.isBlank(newValueClean)) {
            diffs.addCellDiff(sheetName, leftRowIdx, rightRowIdx, leftColIdx, rightColIdx, oldValue, null);
        } else {
            diffs.addCellDiff(sheetName, leftRowIdx, rightRowIdx, leftColIdx, rightColIdx, oldValue, newValue);
        }
    }

}
