package io.github.codingspeedup.execdoc.toolbox.documents.xlsx;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public final class XlsxUtil {

    public static String rowIndexToName(int index) {
        return String.valueOf(index + 1);
    }

    public static int rowNameToIndex(String name) {
        return Integer.parseInt(name) - 1;
    }

    public static String columnIndexToName(int index) {
        return CellReference.convertNumToColString(index);
    }

    public static int columnNameToIndex(String name) {
        return CellReference.convertColStringToIndex(name);
    }

    public static String toCellName(int rowIndex, int columnIndex) {
        return columnIndexToName(columnIndex) + rowIndexToName(rowIndex);
    }

    public static String toCellName(Cell cell) {
        return toCellName(cell.getRowIndex(), cell.getColumnIndex());
    }

    public static boolean isFormula(Cell cell) {
        return cell != null && cell.getCellType() == CellType.FORMULA;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T getCellValue(Cell cell, Class<T> asType) {
        if (cell == null) {
            return null;
        }
        if (asType.isAssignableFrom(String.class)) {
            switch (cell.getCellType()) {
                case BLANK:
                    return (T) "";
                case STRING:
                    return (T) cell.getStringCellValue();
                case NUMERIC:
                    return (T) String.valueOf(cell.getNumericCellValue());
                case BOOLEAN:
                    return (T) String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case BLANK:
                            return (T) "";
                        case STRING:
                            return (T) cell.getStringCellValue();
                        case NUMERIC:
                            return (T) String.valueOf(cell.getNumericCellValue());
                        case BOOLEAN:
                            return (T) String.valueOf(cell.getBooleanCellValue());
                    }
            }
        } else if (Number.class.isAssignableFrom(asType)) {
            switch (cell.getCellType()) {
                case BLANK:
                    return (T) Double.valueOf(0);
                case NUMERIC:
                    return (T) Double.valueOf(cell.getNumericCellValue());
                case STRING:
                    return (T) new BigDecimal(cell.getStringCellValue());
                case BOOLEAN:
                    return (T) (cell.getBooleanCellValue() ? Double.valueOf(1) : Double.valueOf(0));
            }
        } else if (Boolean.class.isAssignableFrom(asType)) {
            switch (cell.getCellType()) {
                case BLANK:
                    return (T) Boolean.FALSE;
                case BOOLEAN:
                    return (T) Boolean.valueOf(cell.getBooleanCellValue());
                case NUMERIC:
                    return (T) (cell.getNumericCellValue() == 0 ? Boolean.FALSE : Boolean.TRUE);
                case STRING:
                    return (T) Boolean.valueOf(StringUtils.isNotBlank(cell.getStringCellValue()));
            }
        } else if (asType.isAssignableFrom(CellFormula.class)) {
            if (cell.getCellType() == CellType.FORMULA) {
                return (T) new CellFormula(cell.getCellFormula());
            } else {
                return null;
            }
        }
        throw new UnsupportedOperationException("Convert " + cell.getCellType().name() + " to " + asType.getName());
    }

    public static Cell setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
        } else if (value instanceof CellFormula) {
            cell.setCellFormula(((CellFormula) value).getSource());
        } else if (value instanceof Hyperlink) {
            cell.setHyperlink((Hyperlink) value);
        } else {
            throw new UnsupportedOperationException("Unsupported type " + value.getClass().getName());
        }
        return cell;
    }

    public static Sheet createOrderGetSheet(Workbook workbook, String name, Integer index) {
        if (index == null) {
            if (name == null) {
                return workbook.getSheetAt(-1);
            }
            Sheet sheet = workbook.getSheet(name);
            if (sheet == null) {
                sheet = workbook.createSheet(name);
            }
            return sheet;
        } else if (name == null) {
            if (index == workbook.getNumberOfSheets()) {
                return workbook.createSheet();
            }
            return workbook.getSheetAt(index);
        } else {
            Sheet sheet = createOrderGetSheet(workbook, name, null);
            workbook.setSheetOrder(name, index);
            return sheet;
        }
    }

    public static String getStringOrEmpty(Cell c) {
        if (c.getCellType() == CellType.STRING) {
            return c.getStringCellValue();
        }
        return "";
    }

    public static String createCellReference(Cell cell) {
        return createCellReference(cell.getSheet(), cell.getRowIndex(), cell.getColumnIndex());
    }

    public static String createCellReference(Sheet sheet, int rowIdx, int colIdx) {
        String reference = "'" + sheet.getSheetName() + "'!";
        reference += columnIndexToName(colIdx);
        reference += rowIndexToName(rowIdx);
        return reference;
    }

    public static String createAbsoluteAreaReference(Sheet sheet, int rowIdx1, int rowIdx2, int colIdx1, int colIdx2) {
        String reference = "'" + sheet.getSheetName() + "'!";
        reference += "$" + columnIndexToName(Math.min(colIdx1, colIdx2));
        reference += "$" + rowIndexToName(Math.min(rowIdx1, rowIdx2));
        reference += ":";
        reference += "$" + columnIndexToName(Math.max(colIdx1, colIdx2));
        reference += "$" + rowIndexToName(Math.max(rowIdx1, rowIdx2));
        return reference;
    }

    public static String getCellComment(Cell cell) {
        if (cell == null) {
            return null;
        }
        Comment comment = cell.getCellComment();
        if (comment == null) {
            return null;
        }
        RichTextString rts = comment.getString();
        return rts.getString();
    }

    public static boolean isBlank(Cell cell) {
        if (cell == null) {
            return true;
        }
        if (cell.getCellType() == CellType.BLANK) {
            return true;
        }
        if (cell.getCellType() == CellType.STRING) {
            return StringUtils.isBlank(cell.getStringCellValue());
        }
        return cell.getCellType() != CellType.FORMULA;
    }

    public static boolean isEmpty(Cell cell) {
        if (cell == null) {
            return true;
        }
        if (cell.getCellType() == CellType.BLANK) {
            return true;
        }
        if (cell.getCellType() == CellType.STRING) {
            return StringUtils.isEmpty(cell.getStringCellValue());
        }
        return cell.getCellType() != CellType.FORMULA;
    }

    public static boolean isEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int colIdx = row.getFirstCellNum(); colIdx <= row.getLastCellNum(); ++colIdx) {
            if (!isEmpty(row.getCell(colIdx))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(Cell cell) {
        return !isBlank(cell);
    }

    public static boolean areEqual(Cell f1, Cell f2) {
        return f1 != null && f2 != null
                       && f1.getRowIndex() == f2.getRowIndex()
                       && f1.getColumnIndex() == f2.getColumnIndex()
                       && f1.getSheet().getSheetName().equals(f2.getSheet().getSheetName());
    }

    public static Cell backtraceCellBySimpleFormulaReference(Cell cell) {
        return backtraceCellBySimpleFormulaReference(cell, Integer.MAX_VALUE);
    }

    public static Cell backtraceCellBySimpleFormulaReference(Cell cell, int maxDepth) {
        while (maxDepth > 0 && cell != null) {
            if (cell.getCellType() == CellType.FORMULA) {
                CellFormula formula = getCellValue(cell, CellFormula.class);
                try {
                    CellReference ref = new CellReference(formula.getSource());
                    Sheet sheet = cell.getSheet();
                    if (ref.getSheetName() != null) {
                        sheet = sheet.getWorkbook().getSheet(ref.getSheetName());
                    }
                    Row row = sheet.getRow(ref.getRow());
                    if (row != null) {
                        cell = row.getCell(ref.getCol());
                        maxDepth -= 1;
                    } else {
                        cell = null;
                    }
                } catch (RuntimeException e) {
                    break;
                }
            } else {
                break;
            }
        }
        return cell;
    }

}
