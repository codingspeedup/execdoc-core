package io.github.codingspeedup.execdoc.blueprint.master.sheets;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;

@Getter
public class SheetArea {

    private final Cell cell;
    private final int xDelta;
    private final int yDelta;

    public SheetArea(Cell cell, int xDelta, int yDelta) {
        this.cell = cell;
        this.xDelta = xDelta;
        this.yDelta = yDelta;
    }

    public boolean contains(Cell cell) {
        if (cell.getRowIndex() < this.cell.getRowIndex()) {
            return false;
        }
        if (cell.getRowIndex() > this.cell.getRowIndex() + yDelta) {
            return false;
        }
        if (cell.getColumnIndex() < this.cell.getColumnIndex()) {
            return false;
        }
        return cell.getColumnIndex() > this.cell.getColumnIndex() + xDelta;
    }

}
