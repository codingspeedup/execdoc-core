package io.github.codingspeedup.execdoc.blueprint.master.sheets.core;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellStyles;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.BlueprintSheet;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TocSheet extends BlueprintSheet {

    public static final String NAME_MARKER = "TOC";

    private static final int START_ROW_IDX = 1;
    private static final int L1_COL_IDX = 1;
    private static final int L2_COL_IDX = 2;
    private static final int L3_COL_IDX = 3;
    private static final int L4_COL_IDX = 4;

    public TocSheet(BlueprintMaster master, Sheet sheet) {
        super(master, sheet);
    }

    @SneakyThrows
    private static String getTocChapter(Class<? extends BlueprintSheet> sheetClass) {
        try {
            Field tocChapterField = sheetClass.getField("TOC_CHAPTER");
            return StringUtils.trimToNull((String) tocChapterField.get(null));
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Override
    public int initialize() {
        getSheet().addMergedRegion(new CellRangeAddress(START_ROW_IDX, START_ROW_IDX, L1_COL_IDX, L4_COL_IDX));
        return START_ROW_IDX + 1;
    }

    @Override
    public void normalize() {
        clearSheet();
        int rowIdx = START_ROW_IDX;

        Row row = getSheet().createRow(rowIdx);
        Cell cell = row.createCell(L1_COL_IDX);
        cell.setCellValue("Table of Contents");
        cell.setCellStyle(getWorkbook().createCellStyle());
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

        rowIdx += 2;

        List<String> chapters = getMaster().getSheetRegistry().values().stream().map(TocSheet::getTocChapter).filter(Objects::nonNull).distinct().collect(Collectors.toList());

        for (String chapter : chapters) {
            boolean chapterIsNotAdded = true;
            List<Class<? extends BlueprintSheet>> chapterSheetClasses = getMaster().getSheetRegistry().values().stream().filter(sheetClass -> chapter.equals(getTocChapter(sheetClass))).collect(Collectors.toList());
            for (Class<? extends BlueprintSheet> sheetClass : chapterSheetClasses) {
                List<? extends BlueprintSheet> sheets = getMaster().getSheets(sheetClass);
                if (CollectionUtils.isNotEmpty(sheets)) {
                    if (chapterIsNotAdded) {
                        getSheet().createRow(rowIdx++).createCell(L1_COL_IDX).setCellValue(chapter);
                        chapterIsNotAdded = false;
                    }
                    if (BlueprintMaster.isSingleton(sheetClass)) {
                        rowIdx = addReference(rowIdx, sheets.get(0));
                    } else {
                        rowIdx = addReferences(rowIdx, sheets);
                    }
                }
            }
        }

        rowIdx += 1;

        getSheet().createRow(rowIdx++).createCell(L1_COL_IDX).setCellValue("NEW");
        for (Map.Entry<String, Class<? extends BlueprintSheet>> entry : getMaster().getSheetRegistry().entrySet()) {
            String sheetMarker = entry.getKey();
            if (sheetMarker.endsWith(BlueprintMaster.INSTANTIABLE_SHEET_MARKER)) {
                rowIdx = addReference(rowIdx, sheetMarker);
            }
        }

        autoSizeColumns(L1_COL_IDX, L2_COL_IDX, L3_COL_IDX, L4_COL_IDX);
        getSheet().setActiveCell(new CellAddress(START_ROW_IDX, L1_COL_IDX));
    }

    @Override
    public void expand(BpKb bpKb) {
    }

    private int addReferences(int rowIdx, List<? extends BlueprintSheet> bSheets) {
        if (CollectionUtils.isNotEmpty(bSheets)) {
            getSheet().createRow(rowIdx++).createCell(L2_COL_IDX).setCellValue(bSheets.get(0).getSheetMarker());
            for (BlueprintSheet bSheet : bSheets) {
                rowIdx = addReference(rowIdx, bSheet);
            }
        }
        return rowIdx;
    }

    private int addReference(int rowIdx, BlueprintSheet bSheet) {
        int colIdx = L2_COL_IDX;
        String fromName = bSheet.getSheetName();
        if (!bSheet.getSheetName().equals(bSheet.getSheetMarker())) {
            colIdx = L3_COL_IDX;
            fromName = bSheet.getInstanceName();
        }
        fromName = "\u261b  " + fromName;
        createDoubleLink(rowIdx, bSheet.getSheet(), colIdx, fromName);
        if (colIdx == L3_COL_IDX) {
            String hint = bSheet.getSheetMarker();
            hint = hint.substring(0, hint.length() - BlueprintMaster.INSTANTIABLE_SHEET_MARKER.length());
            hint = CellMarkers.TOC_TYPE_MARKER + " " + hint.toLowerCase();
            getSheet().getRow(rowIdx).createCell(L4_COL_IDX).setCellValue(hint);
        }
        return ++rowIdx;
    }

    private int addReference(int rowIdx, String sheetName) {
        String fromName = "\u261b  " + sheetName;
        createDoubleLink(rowIdx, getWorkbook().getSheet(sheetName), L2_COL_IDX, fromName);
        return ++rowIdx;
    }

    private void createDoubleLink(int rowIdx, Sheet other, int colIdx, String fromName) {
        CreationHelper createHelper = getWorkbook().getCreationHelper();
        Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
        link.setAddress(XlsxUtil.createCellReference(other, 0, 0));

        Row row = getSheet().createRow(rowIdx);
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(fromName);
        cell.setHyperlink(link);
        cell.setCellStyle(getMaster().getCellStyles().get(CellStyles.HYPERLINK));

        link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
        link.setAddress(XlsxUtil.createCellReference(getSheet(), rowIdx, colIdx));

        row = other.getRow(0);
        if (row == null) {
            row = other.createRow(0);
        }
        cell = row.getCell(0);
        if (cell == null) {
            cell = row.createCell(0);
        }
        cell.setCellValue("\u261a  TOC");
        cell.setHyperlink(link);
        cell.setCellStyle(getMaster().getCellStyles().get(CellStyles.HYPERLINK));
    }

    private void clearSheet() {
        for (int rowIdx = getSheet().getFirstRowNum(); rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Row row = getSheet().getRow(rowIdx);
            if (row != null) {
                getSheet().removeRow(row);
            }
        }
    }

}
