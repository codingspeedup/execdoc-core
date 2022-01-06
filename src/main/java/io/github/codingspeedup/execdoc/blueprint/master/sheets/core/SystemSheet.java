package io.github.codingspeedup.execdoc.blueprint.master.sheets.core;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.structure.BasicSoftwareSystem;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.BlueprintSheet;
import org.apache.poi.ss.usermodel.Sheet;

public class SystemSheet extends BlueprintSheet {

    public static final String NAME_MARKER = "SYSTEM";
    public static final String TOC_CHAPTER = "Global";

    public static final String ANCHOR_SYSTEM_NAME = CellMarkers.ANCHOR_MARKER + "SystemName";

    public SystemSheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet);
    }

    @Override
    public int initialize() {
        int rowIdx = 0;
        setCellValue(++rowIdx, 1, ANCHOR_SYSTEM_NAME);
        autoSizeColumns(1);
        return rowIdx + 1;
    }

    @Override
    public void normalize() {
        autoSizeColumns(getAnchors().getColumn(ANCHOR_SYSTEM_NAME));
    }

    public String getSystemName() {
        return getAnchorRight(ANCHOR_SYSTEM_NAME);
    }

    @Override
    public void expand(BpKb bpKb) {
        BasicSoftwareSystem ss = new BasicSoftwareSystem(getMaster());
        ss.setName(getSystemName());
        bpKb.learn(ss);
    }

}
