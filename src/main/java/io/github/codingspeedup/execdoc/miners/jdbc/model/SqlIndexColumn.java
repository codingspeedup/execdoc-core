package io.github.codingspeedup.execdoc.miners.jdbc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlIndexColumn extends SqlColumn {

    private String ascOrDesc;

    public SqlIndexColumn(String name, SqlElement owner) {
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
