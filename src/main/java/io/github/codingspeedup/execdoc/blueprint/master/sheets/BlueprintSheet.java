package io.github.codingspeedup.execdoc.blueprint.master.sheets;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellComment;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellStyles;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Optional;

public abstract class BlueprintSheet {

    @Getter
    private final BlueprintMaster master;
    @Getter
    private final Sheet sheet;
    @Getter(lazy = true)
    private final AnchorMatrix anchors = locateAnchors();

    public BlueprintSheet(BlueprintMaster master, Sheet sheet) {
        this.master = master;
        this.sheet = sheet;
    }

    public boolean isHidden() {
        return false;
    }

    public abstract int initialize();

    public abstract void normalize();

    public abstract void expand(BpKb bpKb);

    public XSSFWorkbook getWorkbook() {
        return getMaster().getWorkbook();
    }

    public String getSheetMarker() {
        return BlueprintMaster.getNameMarker(getClass());
    }

    public String getSheetName() {
        return getSheet().getSheetName();
    }

    public String getInstanceName() {
        return getSheetName().substring(BlueprintMaster.getNameMarker(this.getClass()).length()).trim();
    }

    public CellStyles getCellStyles() {
        return master.getCellStyles();
    }

    @SuppressWarnings("null")
    private AnchorMatrix locateAnchors() {
        AnchorMatrix matrix = new AnchorMatrix();
        if (sheet != null) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    String anchor = XlsxUtil.getStringOrEmpty(cell);
                    if (anchor.startsWith(CellMarkers.ANCHOR_MARKER) && anchor.length() > CellMarkers.ANCHOR_MARKER.length()) {
                        matrix.put(anchor, cell.getRowIndex(), cell.getColumnIndex());
                    }
                }
            }
        }
        return matrix;
    }

    public String getAnchorRight(String anchorName) {
        int rowIdx = getAnchors().getRow(anchorName);
        int colIndex = getAnchors().getColumn(anchorName) + 1;
        return getCellValue(rowIdx, colIndex, String.class);
    }

    public Cell getCell(int rowIdx, int colIdx) {
        return master.getCell(sheet, rowIdx, colIdx);
    }

    public Cell maybeMakeCell(int rowIdx, int colIdx) {
        return master.maybeMakeCell(sheet, rowIdx, colIdx);
    }

    public <T> T getCellValue(int rowIdx, int colIdx, Class<T> asType) {
        return XlsxUtil.getCellValue(master.getCell(sheet, rowIdx, colIdx), asType);
    }

    protected Cell setCellValue(int rowIdx, int colIdx, Object value) {
        return master.setCellValue(sheet, rowIdx, colIdx, value);
    }

    public Optional<CellComment> getCellComment(Cell cell) {
        if (cell == null) {
            return Optional.empty();
        }
        Comment comment = cell.getCellComment();
        if (comment == null) {
            return Optional.empty();
        }
        RichTextString rts = comment.getString();
        String commentString = rts.getString();
        if (StringUtils.isNotBlank(commentString)) {
            return Optional.of(CellComment.parse(commentString));
        }
        return Optional.empty();
    }

    protected void setCellComment(Cell cell, CellComment cellComment) {
        if (cellComment == null) {
            cell.setCellComment(null);
        } else {
            CreationHelper factory = cell.getSheet().getWorkbook().getCreationHelper();
            Comment comment = cell.getCellComment();
            if (comment == null) {
                Drawing<?> drawing = cell.getSheet().createDrawingPatriarch();
                ClientAnchor anchor = factory.createClientAnchor();
                anchor.setRow1(cell.getRowIndex());
                anchor.setRow2(cell.getRowIndex() + 12);
                anchor.setCol1(cell.getColumnIndex());
                anchor.setCol2(cell.getColumnIndex() + 3);
                comment = drawing.createCellComment(anchor);
            }
            RichTextString rts = factory.createRichTextString(cellComment.toString());
            comment.setString(rts);
        }
    }

    public void autoSizeColumns(Object... specs) {
        for (Object spec : specs) {
            if (spec instanceof Integer) {
                sheet.autoSizeColumn((Integer) spec);
            } else if (spec instanceof String) {
                String[] limits = ((String) spec).split("-");
                if (limits.length == 1) {
                    sheet.autoSizeColumn(Integer.parseInt(limits[0]));
                } else if (limits.length == 2) {
                    for (int colIdx = Integer.parseInt(limits[0]); colIdx <= Integer.parseInt(limits[1]); ++colIdx) {
                        sheet.autoSizeColumn(colIdx);
                    }
                }
            }
        }
    }

    public boolean isOwnerUnit(int rowIdx, int colIdx) {
        if (XlsxUtil.isBlank(getCell(rowIdx, colIdx))) {
            return false;
        }
        if (rowIdx > getAnchors().getLastAnchorRow()) {
            if (XlsxUtil.isBlank(getCell(rowIdx - 1, colIdx))) {
                return true;
            }
            return rowIdx == getAnchors().getLastAnchorRow() + 1;
        }
        return false;
    }

    public Cell findOwnerUnit(int rowIdx, int colIdx) {
        Cell cell = getCell(rowIdx, colIdx);
        if (XlsxUtil.isBlank(cell)) {
            return null;
        }
        int firstDataRow = getAnchors().getLastAnchorRow();
        if (rowIdx <= firstDataRow) {
            return null;
        }
        while (firstDataRow < rowIdx) {
            rowIdx -= 1;
            if (XlsxUtil.isBlank(getCell(rowIdx, colIdx))) {
                rowIdx += 1;
                if (rowIdx < cell.getRowIndex()) {
                    return getCell(rowIdx, colIdx);
                } else {
                    return null;
                }
            }
        }
        rowIdx += 1;
        if (rowIdx < cell.getRowIndex()) {
            return getCell(rowIdx, colIdx);
        } else {
            return null;
        }
    }

    protected void createValidation(int firstRow, int lastRow, int firstCol, int lastCol, String listFormula) {
        DataValidationHelper dvHelper = getSheet().getDataValidationHelper();
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(listFormula);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
        getSheet().addValidationData(validation);
    }

}
