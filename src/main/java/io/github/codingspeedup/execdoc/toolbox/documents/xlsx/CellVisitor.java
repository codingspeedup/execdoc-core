package io.github.codingspeedup.execdoc.toolbox.documents.xlsx;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

public interface CellVisitor {

    boolean process(XlsxDocument file, Sheet sheet, Cell cell, int rowIndex, int columnIndex);

}
