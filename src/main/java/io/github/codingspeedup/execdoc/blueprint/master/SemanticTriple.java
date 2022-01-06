package io.github.codingspeedup.execdoc.blueprint.master;

import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SemanticTriple {

    @Getter
    private final Cell predicate;
    @Getter
    private List<Cell> subject = new ArrayList<>();
    @Getter
    private List<Cell> object = new ArrayList<>();

    public SemanticTriple(Cell predicate) {
        this.predicate = predicate;
        Row row = predicate.getRow();
        for (int colIndex = predicate.getColumnIndex() - 1; colIndex >= row.getFirstCellNum(); --colIndex) {
            Cell cell = row.getCell(colIndex);
            if (XlsxUtil.isBlank(cell)) {
                break;
            } else {
                subject.add(0, cell);
            }
        }
        for (int colIndex = predicate.getColumnIndex() + 1; colIndex <= row.getLastCellNum(); ++colIndex) {
            Cell cell = row.getCell(colIndex);
            if (XlsxUtil.isBlank(cell)) {
                break;
            } else {
                object.add(cell);
            }
        }
    }

    public static String getPredicateName(Cell cell) {
        String name = StringUtils.trimToEmpty(XlsxUtil.getCellValue(cell, String.class));
        if (name.startsWith(CellMarkers.PREDICATE_MARKER)) {
            name = name.substring(CellMarkers.PREDICATE_MARKER.length()).trim();
            if (!name.isEmpty()) {
                return name;
            }
        }
        return null;
    }

    public String getPredicateName() {
        return getPredicateName(predicate);
    }

    public void setSubject(Cell... cells) {
        subject = Arrays.asList(cells);
    }

    public void setObject(Cell... cells) {
        object = Arrays.asList(cells);
    }

}
