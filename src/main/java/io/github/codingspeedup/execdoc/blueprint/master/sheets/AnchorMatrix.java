package io.github.codingspeedup.execdoc.blueprint.master.sheets;

import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnchorMatrix {

    private final Map<String, Integer> anchorColumn = new HashMap<>();
    private final Map<String, Integer> anchorRow = new HashMap<>();

    @Getter
    private Integer firstAnchorColumn;
    @Getter
    private Integer firstAnchorRow;
    @Getter
    private Integer lastAnchorRow;
    @Getter
    private Integer lastAnchorColumn;

    void put(String anchor, int rowIdx, int colIdx) {
        if (anchorRow.containsKey(anchor)) {
            throw new UnsupportedOperationException(
                    "Tag " + anchor + " already used at " + XlsxUtil.toCellName(anchorColumn.get(anchor), anchorRow.get(anchor)));
        }
        anchorRow.put(anchor, rowIdx);
        anchorColumn.put(anchor, colIdx);
        if (firstAnchorColumn == null) {
            firstAnchorColumn = lastAnchorColumn = colIdx;
            firstAnchorRow = lastAnchorRow = rowIdx;
        } else {
            if (colIdx < firstAnchorColumn) {
                firstAnchorColumn = colIdx;
            } else if (lastAnchorColumn < colIdx) {
                lastAnchorColumn = colIdx;
            }
            if (rowIdx < firstAnchorRow) {
                firstAnchorRow = rowIdx;
            } else if (lastAnchorRow < rowIdx) {
                lastAnchorRow = rowIdx;
            }
        }
    }

    public Integer getColumn(String anchor) {
        return anchorColumn.get(anchor);
    }

    public Integer getRow(String anchor) {
        return anchorRow.get(anchor);
    }

    public Set<String> anchorSet() {
        return anchorColumn.keySet();
    }

}
