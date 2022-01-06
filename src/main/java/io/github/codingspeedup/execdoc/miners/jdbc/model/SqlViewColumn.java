package io.github.codingspeedup.execdoc.miners.jdbc.model;

public class SqlViewColumn extends SqlTabularColumn {

    public SqlViewColumn(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public SqlView getOwner() {
        return (SqlView) super.getOwner();
    }

    public void setOwner(SqlView owner) {
        super.setOwner(owner);
    }

}
