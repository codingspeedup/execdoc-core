package io.github.codingspeedup.execdoc.blueprint.sql;

import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XlsxBaseTable {

    @Getter
    private final Sheet tableSheet;

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final CellStyle headingStyle = createHeadingStyle();

    public XlsxBaseTable(Sheet xlsxBaseSheet) {
        this.tableSheet = xlsxBaseSheet;
    }

    public String getName() {
        return tableSheet.getSheetName();
    }

    public int addColumn(XlsxBaseColumn column) {
        Row row = tableSheet.getRow(XlsxBase.NAME_ROW_INDEX);
        int columnIndex = row.getLastCellNum();
        if (columnIndex < 0) {
            columnIndex = 0;
        } else {
            while (columnIndex >= 0) {
                if (XlsxUtil.isBlank(row.getCell(columnIndex))) {
                    --columnIndex;
                } else {
                    break;
                }
            }
            ++columnIndex;
        }
        row.createCell(columnIndex).setCellValue(column.getName());
        row = tableSheet.getRow(XlsxBase.TYPE_ROW_INDEX);
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        cell.setCellValue(column.isMandatory() ? column.getType().getCode() + XlsxBase.MANDATORY_MARKER : column.getType().getCode());
        cell.setCellStyle(getHeadingStyle());
        tableSheet.autoSizeColumn(columnIndex);
        return columnIndex;
    }

    public XlsxBaseColumn getColumn(int columnIndex) {
        String columnName = XlsxUtil.getCellValue(tableSheet.getRow(XlsxBase.NAME_ROW_INDEX).getCell(columnIndex), String.class);
        if (StringUtils.isBlank(columnName)) {
            return null;
        }
        String typeHint = XlsxUtil.getCellValue(tableSheet.getRow(XlsxBase.TYPE_ROW_INDEX).getCell(columnIndex), String.class);
        if (StringUtils.isBlank(typeHint)) {
            return null;
        }
        XlsxBaseColumn column = new XlsxBaseColumn();
        column.setIndex(columnIndex);
        column.setName(columnName);
        column.setType(XlsxBaseType.from(typeHint));
        column.setMandatory(typeHint.endsWith(XlsxBase.MANDATORY_MARKER));
        return column;
    }

    public XlsxBaseColumn getColumn(String columnName) {
        columnName = XlsxBase.normalizeName(columnName);
        if (columnName != null) {
            Row row = tableSheet.getRow(XlsxBase.NAME_ROW_INDEX);
            if (row.getFirstCellNum() < 0) {
                return null;
            }
            for (int colIdx = row.getFirstCellNum(); colIdx <= row.getLastCellNum(); ++colIdx) {
                String cellName = XlsxUtil.getCellValue(row.getCell(colIdx), String.class);
                if (columnName.equals(cellName)) {
                    return getColumn(colIdx);
                }
            }
        }
        return null;
    }

    public Map<String, XlsxBaseColumn> getColumnMap() {
        Map<String, XlsxBaseColumn> columns = new LinkedHashMap<>();
        getColumnList().forEach(c -> columns.put(c.getName(), c));
        return columns;
    }

    public List<XlsxBaseColumn> getColumnList() {
        List<XlsxBaseColumn> columns = new ArrayList<>();
        Row row = tableSheet.getRow(XlsxBase.NAME_ROW_INDEX);
        for (int colIdx = row.getFirstCellNum(); colIdx <= row.getLastCellNum(); ++colIdx) {
            XlsxBaseColumn column = getColumn(colIdx);
            if (column != null) {
                columns.add(column);
            }
        }
        return columns;
    }

    private CellStyle createHeadingStyle() {
        if (tableSheet == null) {
            throw new UnsupportedOperationException();
        }
        CellStyle style = tableSheet.getWorkbook().createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

}
