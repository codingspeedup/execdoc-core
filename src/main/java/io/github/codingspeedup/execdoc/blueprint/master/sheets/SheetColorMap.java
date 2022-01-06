package io.github.codingspeedup.execdoc.blueprint.master.sheets;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SheetColorMap {

    public static final char EMPTY = '\0';

    private final char[][] colorMap;

    public SheetColorMap(Sheet sheet) {
        colorMap = new char[sheet.getLastRowNum() + 1][];
        for (int rowIdx = 0; rowIdx < colorMap.length; ++rowIdx) {
            Row row = sheet.getRow(rowIdx);
            if (row != null) {
                colorMap[rowIdx] = new char[row.getLastCellNum() + 1];
            }
        }
    }

    public char getColor(int rowIdx, int colIdx) {
        if (rowIdx >= colorMap.length) {
            return EMPTY;
        }
        char[] row = colorMap[rowIdx];
        if (row == null) {
            return EMPTY;
        }
        if (colIdx >= row.length) {
            return EMPTY;
        }
        return row[colIdx];
    }

    public boolean setColor(int rowIdx, int colIdx, char color) {
        if (rowIdx >= colorMap.length) {
            return false;
        }
        char[] row = colorMap[rowIdx];
        if (row == null) {
            return false;
        }
        if (colIdx >= row.length) {
            return false;
        }
        row[colIdx] = color;
        return true;
    }

    public void setColor(SheetArea area, char color) {
        for (int rowIdx = area.getCell().getRowIndex(); rowIdx <= area.getCell().getRowIndex() + area.getYDelta(); ++rowIdx) {
            for (int colIdx = area.getCell().getColumnIndex(); colIdx <= area.getCell().getColumnIndex() + area.getXDelta(); ++colIdx) {
                setColor(rowIdx, colIdx, color);
            }
        }
    }

}
