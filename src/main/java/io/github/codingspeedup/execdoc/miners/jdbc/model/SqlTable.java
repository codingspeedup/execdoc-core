package io.github.codingspeedup.execdoc.miners.jdbc.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlTable extends SqlColumnSet {

    private final Map<String, SqlTableColumn> columns = new LinkedHashMap<>();
    private final Map<String, SqlIndex> indexInfo = new LinkedHashMap<>();
    private final Map<String, SqlTableForeignKey> foreignKeys = new LinkedHashMap<>();
    @Getter
    private SqlTablePrimaryKey primaryKey;
    @Getter
    @Setter
    private String type;

    public SqlTable(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public SqlTableColumn addColumn(String name) {
        return columns.computeIfAbsent(name, key -> new SqlTableColumn(key, this));
    }

    @Override
    public SqlTableColumn getColumn(String name) {
        return columns.get(name);
    }

    @Override
    public List<String> getColumnNames() {
        return new ArrayList<>(columns.keySet());
    }

    public SqlIndex addIndex(String name) {
        return indexInfo.computeIfAbsent(name, key -> new SqlIndex(key, this));
    }

    public SqlIndex getIndex(String name) {
        return indexInfo.get(name);
    }

    public List<String> getIndexNames() {
        return new ArrayList<>(indexInfo.keySet());
    }

    public SqlTablePrimaryKey addPrimaryKey(String name) {
        return primaryKey = new SqlTablePrimaryKey(name, this);
    }

    public SqlTableForeignKey addForeignKey(String name) {
        return foreignKeys.computeIfAbsent(name, key -> new SqlTableForeignKey(key, this));
    }

    public SqlTableForeignKey getForeignKey(String name) {
        return foreignKeys.get(name);
    }

    public List<String> getForeignKeyNames() {
        return new ArrayList<>(foreignKeys.keySet());
    }

}
