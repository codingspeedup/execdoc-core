package io.github.codingspeedup.execdoc.miners.jdbc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SqlTableKey extends SqlElement {

    private final List<SqlTableColumn> columns = new ArrayList<>();

    public SqlTableKey(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public SqlTable getOwner() {
        return (SqlTable) super.getOwner();
    }

    public void setOwner(SqlTable owner) {
        super.setOwner(owner);
    }

    protected void addColumn(SqlTableColumn column) {
        addColumn(column, -1);
    }

    protected void addColumn(SqlTableColumn column, int keySeq) {
        if (keySeq < 0) {
            columns.add(column);
        } else {
            while (columns.size() < keySeq) {
                columns.add(null);
            }
            columns.set(keySeq - 1, column);
        }
    }

    public List<String> getColumnNames() {
        return columns.stream().map(c -> c == null ? null : c.getName()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return super.toString() + getColumnNames();
    }

}
