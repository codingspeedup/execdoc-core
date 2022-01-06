package io.github.codingspeedup.execdoc.blueprint.sql;

import java.sql.Types;

public enum XlsxBaseType {

    BOOLEAN,
    DATE,
    NUMERIC,
    STRING,
    TIMESTAMP,

    ;

    public static XlsxBaseType from(Integer dataType, String typeName) {
        if (dataType != null) {
            switch (dataType) {
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                case Types.NCHAR:
                case Types.NVARCHAR:
                case Types.LONGNVARCHAR:
                case Types.NCLOB:
                case Types.BLOB:
                case Types.CLOB:
                    return XlsxBaseType.STRING;
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.BIGINT:
                case Types.FLOAT:
                case Types.REAL:
                case Types.DOUBLE:
                case Types.NUMERIC:
                case Types.DECIMAL:
                    return XlsxBaseType.NUMERIC;
                case Types.DATE:
                    return XlsxBaseType.DATE;
                case Types.TIMESTAMP:
                case Types.TIMESTAMP_WITH_TIMEZONE:
                    return XlsxBaseType.TIMESTAMP;
                case Types.BIT:
                case Types.BOOLEAN:
                    return XlsxBaseType.BOOLEAN;
            }
        }
        throw new UnsupportedOperationException("Cannot infer type from " + dataType + " and " + typeName);
    }

    public static XlsxBaseType from(String typeHint) {
        String code = typeHint.substring(0, 1);
        for (XlsxBaseType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public String getCode() {
        return this.name().substring(0, 1);
    }

}
