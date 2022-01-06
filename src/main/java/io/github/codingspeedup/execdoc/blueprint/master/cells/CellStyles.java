package io.github.codingspeedup.execdoc.blueprint.master.cells;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public class CellStyles {

    public static final BorderStyle UI_COMP_BORDER_STYLE = BorderStyle.MEDIUM;
    public static final BorderStyle UI_SEP_BORDER_STYLE = BorderStyle.THIN;

    public static final String HYPERLINK = "hyperlink";

    public static final String UI_COMP_TL = "ui-component-top-left";
    public static final String UI_COMP_T = "ui-component-top";
    public static final String UI_COMP_TR = "ui-component-top-right";
    public static final String UI_COMP_R = "ui-component-right";
    public static final String UI_COMP_BR = "ui-component-bottom-right";
    public static final String UI_COMP_B = "ui-component-bottom";
    public static final String UI_COMP_BL = "ui-component-bottom-left";
    public static final String UI_COMP_L = "ui-component-left";

    private final Workbook workbook;
    private final Map<String, CellStyle> cellStyles = new HashMap<>();

    public CellStyles(Workbook workbook) {
        this.workbook = workbook;
    }

    public static boolean isTop(Cell cell, BorderStyle bStyle) {
        if (cell != null) {
            if (cell.getCellStyle().getBorderTop() == bStyle) {
                return true;
            }
            if (cell.getRowIndex() > 0) {
                Row row = cell.getSheet().getRow(cell.getRowIndex() - 1);
                if (row != null) {
                    cell = row.getCell(cell.getColumnIndex());
                    if (cell != null) {
                        return cell.getCellStyle().getBorderBottom() == bStyle;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isRight(Cell cell, BorderStyle bStyle) {
        if (cell != null) {
            if (cell.getCellStyle().getBorderRight() == bStyle) {
                return true;
            }
            cell = cell.getRow().getCell(cell.getColumnIndex() + 1);
            if (cell != null) {
                return cell.getCellStyle().getBorderLeft() == bStyle;
            }
        }
        return false;
    }

    public static boolean isBottom(Cell cell, BorderStyle bStyle) {
        if (cell != null) {
            if (cell.getCellStyle().getBorderBottom() == bStyle) {
                return true;
            }
            Row row = cell.getSheet().getRow(cell.getRowIndex() + 1);
            if (row != null) {
                cell = row.getCell(cell.getColumnIndex());
                if (cell != null) {
                    return cell.getCellStyle().getBorderTop() == bStyle;
                }
            }
        }
        return false;
    }

    public static boolean isLeft(Cell cell, BorderStyle bStyle) {
        if (cell != null) {
            if (cell.getCellStyle().getBorderLeft() == bStyle) {
                return true;
            }
            if (cell.getColumnIndex() > 0) {
                cell = cell.getRow().getCell(cell.getColumnIndex() - 1);
                if (cell != null) {
                    return cell.getCellStyle().getBorderRight() == bStyle;
                }
            }
        }
        return false;
    }

    public CellStyle get(String styleKey) {
        if (!cellStyles.containsKey(styleKey)) {
            switch (styleKey) {
                case HYPERLINK:
                    cellStyles.put(styleKey, configureHyperlink(workbook.createCellStyle()));
                    break;
                case UI_COMP_TL:
                    cellStyles.put(styleKey, configureUiCompTL(workbook.createCellStyle()));
                    break;
                case UI_COMP_T:
                    cellStyles.put(styleKey, configureUiCompT(workbook.createCellStyle()));
                    break;
                case UI_COMP_TR:
                    cellStyles.put(styleKey, configureUiCompTR(workbook.createCellStyle()));
                    break;
                case UI_COMP_R:
                    cellStyles.put(styleKey, configureUiCompR(workbook.createCellStyle()));
                    break;
                case UI_COMP_BR:
                    cellStyles.put(styleKey, configureUiCompBR(workbook.createCellStyle()));
                    break;
                case UI_COMP_B:
                    cellStyles.put(styleKey, configureUiCompB(workbook.createCellStyle()));
                    break;
                case UI_COMP_BL:
                    cellStyles.put(styleKey, configureUiCompBL(workbook.createCellStyle()));
                    break;
                case UI_COMP_L:
                    cellStyles.put(styleKey, configureUiCompL(workbook.createCellStyle()));
                    break;
            }
        }
        return cellStyles.get(styleKey);
    }

    private CellStyle configureHyperlink(CellStyle cellStyle) {
        Font hlinkfont = workbook.createFont();
        // hlinkfont.setUnderline(Font.U_SINGLE);
        hlinkfont.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        cellStyle.setFont(hlinkfont);
        return cellStyle;
    }

    private CellStyle configureUiCompL(CellStyle cellStyle) {
        cellStyle.setBorderLeft(UI_COMP_BORDER_STYLE);
        return cellStyle;
    }

    private CellStyle configureUiCompBL(CellStyle cellStyle) {
        cellStyle.setBorderBottom(UI_COMP_BORDER_STYLE);
        cellStyle.setBorderLeft(UI_COMP_BORDER_STYLE);
        return cellStyle;
    }

    private CellStyle configureUiCompB(CellStyle cellStyle) {
        cellStyle.setBorderBottom(UI_COMP_BORDER_STYLE);
        return cellStyle;
    }

    private CellStyle configureUiCompBR(CellStyle cellStyle) {
        cellStyle.setBorderBottom(UI_COMP_BORDER_STYLE);
        cellStyle.setBorderRight(UI_COMP_BORDER_STYLE);
        return cellStyle;
    }

    private CellStyle configureUiCompR(CellStyle cellStyle) {
        cellStyle.setBorderRight(UI_COMP_BORDER_STYLE);
        return cellStyle;
    }

    private CellStyle configureUiCompTR(CellStyle cellStyle) {
        cellStyle.setBorderTop(UI_COMP_BORDER_STYLE);
        cellStyle.setBorderRight(UI_COMP_BORDER_STYLE);
        return cellStyle;
    }

    private CellStyle configureUiCompT(CellStyle cellStyle) {
        cellStyle.setBorderTop(UI_COMP_BORDER_STYLE);
        return cellStyle;
    }

    private CellStyle configureUiCompTL(CellStyle cellStyle) {
        cellStyle.setBorderTop(UI_COMP_BORDER_STYLE);
        cellStyle.setBorderLeft(UI_COMP_BORDER_STYLE);
        return cellStyle;
    }

}
