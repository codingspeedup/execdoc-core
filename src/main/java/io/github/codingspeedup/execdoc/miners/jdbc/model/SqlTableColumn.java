package io.github.codingspeedup.execdoc.miners.jdbc.model;

public class SqlTableColumn extends SqlTabularColumn {

    public SqlTableColumn(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public SqlTable getOwner() {
        return (SqlTable) super.getOwner();
    }

    public void setOwner(SqlTable owner) {
        super.setOwner(owner);
    }

}
