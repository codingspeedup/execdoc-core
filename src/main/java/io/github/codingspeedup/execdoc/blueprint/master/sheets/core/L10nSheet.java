package io.github.codingspeedup.execdoc.blueprint.master.sheets.core;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.ui.BpL10nLabel;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.BlueprintSheet;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import io.github.codingspeedup.execdoc.toolbox.utilities.StringUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class L10nSheet extends BlueprintSheet {

    public static final String NAME_MARKER = "L10N";
    public static final String TOC_CHAPTER = "Client";

    public static final String ANCHOR_KEY = CellMarkers.ANCHOR_MARKER + "Key";
    public static final String ANCHOR_LABEL = CellMarkers.ANCHOR_MARKER + "DefaultLabel";

    public L10nSheet(BlueprintMaster xlsx, Sheet sheet) {
        super(xlsx, sheet);
    }

    @Override
    public int initialize() {
        int rowIdx = 0;
        int colIdx = 0;
        setCellValue(rowIdx, ++colIdx, ANCHOR_KEY);
        setCellValue(rowIdx, ++colIdx, ANCHOR_LABEL);
        setCellValue(rowIdx, ++colIdx, "...");
        setCellValue(rowIdx, ++colIdx, "...");
        autoSizeColumns(1, 2);
        getSheet().createFreezePane(0, rowIdx + 1);

        rowIdx = getAnchors().getLastAnchorRow() + 2;
        colIdx = getAnchors().getColumn(ANCHOR_KEY);
        getSheet().setActiveCell(new CellAddress(rowIdx, colIdx));

        return rowIdx;
    }

    @Override
    public void normalize() {
        int keyColIdx = getAnchors().getColumn(ANCHOR_KEY);
        List<Integer> colsIdxs = getAnchors().anchorSet().stream()
                .filter(key -> !ANCHOR_KEY.equals(key))
                .map(key -> getAnchors().getColumn(key))
                .sorted()
                .collect(Collectors.toList());
        Set<String> l10nKeys = new HashSet<>();
        for (Row row : getSheet()) {
            if (row.getRowNum() > getAnchors().getLastAnchorRow()) {
                l10nKeys.add(XlsxUtil.getCellValue(row.getCell(keyColIdx), String.class));
            }
        }
        for (Row row : getSheet()) {
            if (row.getRowNum() > getAnchors().getLastAnchorRow()) {
                Cell keyCell = row.getCell(keyColIdx);
                String key = XlsxUtil.getCellValue(keyCell, String.class);
                if (StringUtils.isBlank(key)) {
                    for (int colIdx : colsIdxs) {
                        String label = XlsxUtil.getCellValue(row.getCell(colIdx), String.class);
                        if (StringUtils.isNotBlank(label)) {
                            if (keyCell == null) {
                                keyCell = row.createCell(keyColIdx);
                            }
                            keyCell.setCellValue(createL10nKey(label, l10nKeys));
                            break;
                        }
                    }
                }
            }
        }
        autoSizeColumns(keyColIdx);
    }

    public Cell getKeyCell(int rowIndex) {
        int keyColIdx = getAnchors().getColumn(ANCHOR_KEY);
        return getCell(rowIndex, keyColIdx);
    }

    public Cell[] addLabel(String label) {
        label = StringUtils.trim(label);
        if (StringUtils.isBlank(label)) {
            return null;
        }
        int keyColIdx = getAnchors().getColumn(ANCHOR_KEY);
        int labelColIdx = getAnchors().getColumn(ANCHOR_LABEL);
        Set<String> l10nKeys = new HashSet<>();
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Cell keyCell = getCell(rowIdx, keyColIdx);
            Cell labelCell = getCell(rowIdx, labelColIdx);
            if (label.equals(XlsxUtil.getCellValue(labelCell, String.class))) {
                return new Cell[]{keyCell, labelCell};
            }
            l10nKeys.add(XlsxUtil.getCellValue(labelCell, String.class));
        }
        String key = createL10nKey(label, l10nKeys);
        int rowIdx = getSheet().getLastRowNum() + 1;
        Row row = getSheet().createRow(rowIdx);
        Cell keyCell = row.createCell(keyColIdx);
        keyCell.setCellValue(key);
        Cell labelCell = row.createCell(labelColIdx);
        labelCell.setCellValue(label);
        return new Cell[]{keyCell, labelCell};
    }

    @Override
    public void expand(BpKb bpKb) {
        int keyColIdx = getAnchors().getColumn(ANCHOR_KEY);
        List<String> labelAnchors = getAnchors().anchorSet().stream().filter(key -> !ANCHOR_KEY.equals(key)).collect(Collectors.toList());
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Row row = getSheet().getRow(rowIdx);
            if (row == null) {
                continue;
            }
            Cell cell = row.getCell(keyColIdx);
            if (cell == null) {
                continue;
            }
            BpL10nLabel l10n = new BpL10nLabel(cell);
            if (StringUtils.isBlank(l10n.getName())) {
                continue;
            }
            for (String anchor : labelAnchors) {
                String label = getCellValue(rowIdx, getAnchors().getColumn(anchor), String.class);
                if (StringUtils.isNotBlank(label)) {
                    label = StringUtils.trim(label);
                    if (ANCHOR_LABEL.equals(anchor)) {
                        l10n.getL10n().put(BpL10nLabel.DEFAULT_LANGUAGE_KEY, label);
                    } else {
                        String languageKey = anchor.substring(CellMarkers.ANCHOR_MARKER.length());
                        l10n.getL10n().put(languageKey, label);
                    }
                }
            }
            bpKb.learn(l10n);
        }
    }

    private String createL10nKey(String label, Set<String> l10nKeys) {
        String basicKey = StringUtility.toBasicL10nKey(label);
        int keyIndex = 1;
        String key = basicKey;
        while (l10nKeys.contains(key)) {
            key = basicKey + "." + (++keyIndex);
        }
        return key;
    }

}
