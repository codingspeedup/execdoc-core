package io.github.codingspeedup.execdoc.miners.jdbc.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class SqlColumnSet extends SqlElement {

    @Getter
    @Setter
    private String remarks;

    public SqlColumnSet(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public SqlSchema getOwner() {
        return (SqlSchema) super.getOwner();
    }

    public void setOwner(SqlSchema owner) {
        super.setOwner(owner);
    }

    public abstract SqlColumn addColumn(String name);

    public abstract SqlColumn getColumn(String name);

    public abstract List<String> getColumnNames();

}
