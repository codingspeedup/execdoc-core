package io.github.codingspeedup.execdoc.toolbox.documents.xlsx;

import org.apache.poi.ss.usermodel.Sheet;

public interface SheetVisitor {

    boolean process(XlsxDocument file, Sheet sheet, int sheetIndex);

}
