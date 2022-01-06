package io.github.codingspeedup.execdoc.miners.diff.xlsx;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class XlsxTexcoder {

    public static String EMPTY_CELL_MARKER = "-";
    public static String VALUE_SEPARATOR = "|";

    public static String textcode(Cell cell) {
        if (cell == null) {
            return EMPTY_CELL_MARKER;
        }
        String value = null;
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                value = String.valueOf(cell.getNumericCellValue());
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue() ? "\u000e" : "\u000f";
                break;
        }
        if (StringUtils.isBlank(value)) {
            value = "";
        } else {
            value = VALUE_SEPARATOR + Base64.encodeBase64String(value.getBytes(StandardCharsets.UTF_8));
        }
        String comment = "";
        if (cell.getCellComment() != null) {
            comment = cell.getCellComment().getString().getString();
        }
        if (StringUtils.isNotBlank(comment)) {
            value += VALUE_SEPARATOR + Base64.encodeBase64String(comment.getBytes(StandardCharsets.UTF_8));
        }
        return StringUtils.isBlank(value) ? EMPTY_CELL_MARKER : value;
    }

    public static String textcode(Row row) {
        StringBuilder x = new StringBuilder();
        if (row != null) {
            for (int i = 0; i <= row.getLastCellNum(); ++i) {
                x.append(textcode(row.getCell(i))).append(" ");
            }
        }
        String value = x.toString();
        while (!value.isEmpty()) {
            value = StringUtils.stripEnd(value, null);
            if (value.endsWith(EMPTY_CELL_MARKER)) {
                value = value.substring(0, value.length() - EMPTY_CELL_MARKER.length());
            } else {
                break;
            }
        }
        return value;
    }

    public static List<String> textcode(Sheet sheet) {
        List<String> textRows = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); ++i) {
            textRows.add(textcode(sheet.getRow(i)));
        }
        return textRows;
    }

}
