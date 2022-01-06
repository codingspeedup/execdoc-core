package io.github.codingspeedup.execdoc.blueprint.sql;

import io.github.codingspeedup.execdoc.miners.jdbc.model.SqlTable;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class XlsxBaseOperator {

    @Getter
    private final XlsxBase xlsxBase;

    public XlsxBaseOperator() {
        this(new XlsxBase());
    }

    public XlsxBaseOperator(File xlsxBase) {
        this(new XlsxBase(xlsxBase));
    }

    public XlsxBaseOperator(XlsxBase xlsxBase) {
        this.xlsxBase = xlsxBase;
    }

    public void merge(SqlTable headTable, SqlTable... tailTables) {
        List<SqlTable> tables = new ArrayList<>();
        tables.add(headTable);
        Arrays.stream(tailTables).filter(Objects::nonNull).forEach(tables::add);
        merge(tables);
    }

    public void merge(List<SqlTable> tables) {
        for (SqlTable sqlTable : tables) {
            XlsxBaseTable xlsxTable = xlsxBase.maybeAddTable(sqlTable.getName());
            for (String columnName : sqlTable.getColumnNames()) {
                XlsxBaseColumn xlsxColumn = xlsxTable.getColumn(columnName);
                if (xlsxColumn == null) {
                    xlsxTable.addColumn(XlsxBaseColumn.from(sqlTable.getColumn(columnName)));
                }
            }
        }
        for (int tableIndex = 0; tableIndex < tables.size(); ++tableIndex) {
            String tableName = XlsxBase.normalizeName(tables.get(tableIndex).getName());
            xlsxBase.maybeMakeSheet(tableName, tableIndex);
        }
    }

}
