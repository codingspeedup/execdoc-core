package io.github.codingspeedup.execdoc.miners.jdbc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SqlTabularColumn extends SqlColumn {

    private Integer dataType; // from java.sql.Types
    private String typeName;
    private int columnSize;
    private int decimalDigits;
    private int numPrecRadix;
    private Boolean nullable;
    private String remarks;
    private String columnDef;
    private int charOctetLength;
    private Boolean autoincrement;
    private Boolean generatedcolumn;

    public SqlTabularColumn(String name, SqlElement owner) {
        super(name, owner);
    }

}
