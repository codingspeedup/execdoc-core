package io.github.codingspeedup.execdoc.toolbox.documents.xlsx;

import io.github.codingspeedup.execdoc.toolbox.documents.BinaryFileWrapper;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor
public class XlsxDocument extends BinaryFileWrapper {

    private XSSFWorkbook workbook;

    public XlsxDocument(File file) {
        super(file);
    }

    @SneakyThrows
    @Override
    protected void loadFromWrappedFile() {
        workbook = new XSSFWorkbook(getFile());
    }

    public XSSFWorkbook getWorkbook() {
        if (workbook == null) {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    public List<String> getSheetNames() {
        Workbook wb = getWorkbook();
        return IntStream.range(0, wb.getNumberOfSheets()).mapToObj(i -> getWorkbook().getSheetAt(i).getSheetName()).collect(Collectors.toList());
    }

    public Integer getSheetIndex(String name) {
        Sheet sheet = getWorkbook().getSheet(name);
        if (sheet != null) {
            return getWorkbook().getSheetIndex(sheet);
        }
        return null;
    }

    public Sheet maybeMakeSheet(Integer index) {
        return XlsxUtil.createOrderGetSheet(getWorkbook(), null, index);
    }

    public Sheet maybeMakeSheet(String name) {
        return XlsxUtil.createOrderGetSheet(getWorkbook(), name, null);
    }

    public Sheet maybeMakeSheet(String name, Integer index) {
        return XlsxUtil.createOrderGetSheet(getWorkbook(), name, index);
    }

    public void removeSheet(String name) {
        Integer sheetIndex = getSheetIndex(name);
        if (sheetIndex != null) {
            getWorkbook().removeSheetAt(sheetIndex);
        }
    }

    public void visitSheets(SheetVisitor visitor) {
        if (visitor == null) {
            return;
        }
        for (int i = 0; i < getWorkbook().getNumberOfSheets(); ++i) {
            Sheet sheet = getWorkbook().getSheetAt(i);
            if (visitor.process(this, sheet, i)) {
                return;
            }
        }
    }

    public void visitCells(Sheet sheet, CellVisitor visitor) {
        if (visitor == null) {
            return;
        }
        for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); ++rowIndex) {
            Row row = sheet.getRow(rowIndex);
            if (row != null && row.getFirstCellNum() >= 0) {
                for (int columnIndex = row.getFirstCellNum(); columnIndex <= row.getLastCellNum(); ++columnIndex) {
                    System.out.println(sheet.getSheetName() + " " + rowIndex + " " + columnIndex);
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null && visitor.process(this, sheet, cell, rowIndex, columnIndex)) {
                        return;
                    }
                }
            }
        }
    }

    public Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return null;
        }
        return row.getCell(columnIndex);
    }

    public Cell maybeMakeCell(Sheet sheet, int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        return cell;
    }

    public Cell setCellValue(Sheet sheet, int rowIndex, int columnIndex, Object value) {
        Cell cell = maybeMakeCell(sheet, rowIndex, columnIndex);
        XlsxUtil.setCellValue(cell, value);
        return cell;
    }

    @SneakyThrows
    @Override
    protected void saveToWrappedFile() {
        File tempXlsx = File.createTempFile(getBinaryFile().getBaseName() + "-", "." + getBinaryFile().getExtension());
        try (OutputStream fileOut = new FileOutputStream(tempXlsx)) {
            getWorkbook().write(fileOut);
        }
        getWorkbook().close();
        FileUtils.delete(getFile());
        FileUtils.moveFile(tempXlsx, getFile());
        loadFromWrappedFile();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        try (ByteArrayOutputStream sout = new ByteArrayOutputStream()) {
            getWorkbook().write(sout);
            return sout.toByteArray();
        }
    }

}
