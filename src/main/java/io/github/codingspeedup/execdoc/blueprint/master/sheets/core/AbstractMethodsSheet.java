package io.github.codingspeedup.execdoc.blueprint.master.sheets.core;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpSheet;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.BpClassUnit;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.BpMethodUnit;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.BlueprintSheet;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import java.util.List;

public abstract class AbstractMethodsSheet<C extends BpClassUnit, M extends BpMethodUnit> extends BlueprintSheet {

    public static final String ANCHOR_NAME = CellMarkers.ANCHOR_MARKER + "ClassName / methodName";

    private final Class<C> componentClass;
    private final Class<M> methodClass;

    protected AbstractMethodsSheet(BlueprintMaster bp, Sheet sheet, Class<C> componentClass, Class<M> methodClass) {
        super(bp, sheet);
        this.componentClass = componentClass;
        this.methodClass = methodClass;
    }

    @Override
    public int initialize() {
        int rowIdx = 0;
        int colIdx = 0;
        setCellValue(rowIdx, ++colIdx, ANCHOR_NAME);
        autoSizeColumns("0-" + colIdx);
        getSheet().createFreezePane(0, rowIdx + 1);

        rowIdx = getAnchors().getLastAnchorRow() + 2;
        colIdx = getAnchors().getColumn(ANCHOR_NAME);
        getSheet().setActiveCell(new CellAddress(rowIdx, colIdx));

        return rowIdx;
    }

    @Override
    public void normalize() {
        autoSizeColumns(getAnchors().getColumn(ANCHOR_NAME));
    }

    @SneakyThrows
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void expand(BpKb bpKb) {
        BpSheet owner = new BpSheet(getSheet());
        C classUnit = null;
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Row row = getSheet().getRow(rowIdx);
            if (row == null) {
                continue;
            }

            Cell nameCell = row.getCell(getAnchors().getColumn(ANCHOR_NAME));
            String nameString = XlsxUtil.getCellValue(nameCell, String.class);
            if (StringUtils.isBlank(nameString)) {
                bpKb.learn(classUnit);
                classUnit = null;
                continue;
            }

            if (classUnit == null) {
                classUnit = componentClass.getConstructor(Cell.class).newInstance(nameCell);
                ((IsOwned) classUnit).setOwner(owner);
            } else {
                M methodUnit = methodClass.getConstructor(Cell.class).newInstance(nameCell);
                ((List) classUnit.getCodeElement()).add(methodUnit);
            }
        }
        bpKb.learn(classUnit);
    }

}
