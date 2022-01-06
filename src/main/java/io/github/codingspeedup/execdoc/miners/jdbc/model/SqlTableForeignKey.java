package io.github.codingspeedup.execdoc.miners.jdbc.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

public class SqlTableForeignKey extends SqlTableKey {

    private final Map<String, SqlTableColumn> fkPkMapping = new LinkedHashMap<>();
    @Getter
    @Setter
    private SqlTable pkTable;
    @Getter
    @Setter
    private String pkName;
    @Getter
    @Setter
    private short updateRule;
    @Getter
    @Setter
    private short deleteRule;
    @Getter
    @Setter
    private short deferability;

    public SqlTableForeignKey(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public String toString() {
        return "-" + super.toString();
    }

    public void addColumn(SqlTableColumn fkColumn, int keySeq, SqlTableColumn pkColumn) {
        super.addColumn(fkColumn, keySeq);
        fkPkMapping.put(fkColumn.getName(), pkColumn);
    }

}
