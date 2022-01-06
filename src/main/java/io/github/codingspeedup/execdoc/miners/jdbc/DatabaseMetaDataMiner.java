package io.github.codingspeedup.execdoc.miners.jdbc;

import io.github.codingspeedup.execdoc.miners.jdbc.model.*;
import lombok.SneakyThrows;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseMetaDataMiner {

    private final DatabaseMetaData metaData;
    private final SqlEngine engine;
    private final Map<String, SqlCatalog> catalogs = new LinkedHashMap<>();

    @SneakyThrows
    public DatabaseMetaDataMiner(DatabaseMetaData metaData) {
        this.metaData = metaData;

        System.out.println("                 Engine: " + metaData.getDatabaseProductName() + " (" + metaData.getDatabaseProductVersion() + ")");
        System.out.println("                 Driver: " + metaData.getDriverName() + " ~~ " + metaData.getDriverVersion());
        System.out.println("                    URL: " + metaData.getURL());
        System.out.println("                   User: " + metaData.getUserName());
        System.out.println("Default isolation level: " + metaData.getDefaultTransactionIsolation());
        System.out.println("           Catalog term: " + metaData.getCatalogTerm());
        System.out.println("            Schema term: " + metaData.getSchemaTerm());
        System.out.println("         Procedure term: " + metaData.getProcedureTerm());
        System.out.println("           SQL Keywords: " + metaData.getSQLKeywords());
        System.out.println("       String functions: " + metaData.getStringFunctions());
        System.out.println("      Numeric functions: " + metaData.getNumericFunctions());
        System.out.println("    Time date functions: " + metaData.getTimeDateFunctions());
        System.out.println("       System functions: " + metaData.getSystemFunctions());
        System.out.println("      Catalog separator: " + metaData.getCatalogSeparator());
        System.out.println("   Search string escape: " + metaData.getSearchStringEscape());
        System.out.println("  Extra name characters: " + metaData.getExtraNameCharacters());
        System.out.println("Identifier quote string: " + metaData.getIdentifierQuoteString());

        if ("mysql".equals(metaData.getDatabaseProductName().toLowerCase(Locale.ROOT))) {
            engine = SqlEngine.MYSQL;
            SqlCatalog catalog = catalogs.computeIfAbsent("", key -> new SqlCatalog(""));
            ResultSet schemaSet = metaData.getCatalogs();
            while (schemaSet.next()) {
                catalog.addSchema(schemaSet.getString("TABLE_CAT"));
            }
        } else {
            engine = SqlEngine.DEFAULT;
            ResultSet catalogSet = metaData.getCatalogs();
            while (catalogSet.next()) {
                SqlCatalog catalog = new SqlCatalog(catalogSet.getString("TABLE_CAT"));
                catalogs.put(catalog.getName(), catalog);
            }
            catalogs.computeIfAbsent("", key -> new SqlCatalog(""));
            ResultSet schemaSet = metaData.getSchemas();
            while (schemaSet.next()) {
                SqlCatalog catalog = catalogs.get(schemaSet.getString("TABLE_CATALOG"));
                catalog.addSchema(schemaSet.getString("TABLE_SCHEM"));
            }
        }

        System.out.println();
        System.out.println("               Catalogs: " + getCatalogNames());
        System.out.println("                Schemas: " + getCatalog().getSchemaNames());
    }

    private static Boolean fromYesNoEmpty(String value) {
        if ("YES".equalsIgnoreCase(value)) {
            return true;
        }
        return !"NO".equalsIgnoreCase(value);
    }

    public SqlCatalog getCatalog() {
        return catalogs.get("");
    }

    public SqlCatalog getCatalog(String name) {
        return catalogs.get(name);
    }

    public SqlSchema getSchema(String name) {
        return getSchema("", name);
    }

    public SqlSchema getSchema(String catalogName, String schemaName) {
        return getCatalog(catalogName).getSchema(schemaName);
    }

    @SneakyThrows
    public List<String> getCatalogNames() {
        return new ArrayList<>(catalogs.keySet());
    }

    @SneakyThrows
    public void findObjects(SqlSchema schema) {
        String[] catalogSchema = fetchCatalogSchema(schema);
        ResultSet tabularSet = metaData.getTables(catalogSchema[0], catalogSchema[1], null, null);
        while (tabularSet.next()) {
            String columnsType = tabularSet.getString("TABLE_TYPE");
            if ("VIEW".equals(columnsType)) {
                SqlView view = schema.addView(tabularSet.getString("TABLE_NAME"));
                view.setRemarks(tabularSet.getString("REMARKS"));
            } else {
                SqlTable table = schema.addTable(tabularSet.getString("TABLE_NAME"));
                table.setType(columnsType);
                table.setRemarks(tabularSet.getString("REMARKS"));
            }
        }
    }

    @SneakyThrows
    public void fillTable(SqlTable table) {
        String[] catalogSchema = fetchCatalogSchema(table.getOwner());
        ResultSet columnsSet = metaData.getColumns(catalogSchema[0], catalogSchema[1], table.getName(), null);
        while (columnsSet.next()) {
            fillTabularColumn(columnsSet, table.addColumn(columnsSet.getString("COLUMN_NAME")));
        }
        ResultSet primaryKeySet = metaData.getPrimaryKeys(catalogSchema[0], catalogSchema[1], table.getName());
        while (primaryKeySet.next()) {
            SqlTablePrimaryKey pk = table.getPrimaryKey();
            if (pk == null) {
                pk = table.addPrimaryKey(primaryKeySet.getString("PK_NAME"));
            }
            pk.addColumn(table.getColumn(primaryKeySet.getString("COLUMN_NAME")), primaryKeySet.getInt("KEY_SEQ"));
        }
        ResultSet indexSet = metaData.getIndexInfo(catalogSchema[0], catalogSchema[1], table.getName(), false, true);
        while (indexSet.next()) {
            String indexName = indexSet.getString("INDEX_NAME");
            SqlIndex index = table.getIndex(indexName);
            if (index == null) {
                index = table.addIndex(indexName);
                index.setNonUnique(indexSet.getBoolean("NON_UNIQUE"));
                index.setIndexQualifier(indexSet.getString("INDEX_QUALIFIER"));
                index.setType(indexSet.getInt("TYPE"));
                index.setCardinality(indexSet.getLong("CARDINALITY"));
                index.setPages(indexSet.getLong("PAGES"));
                index.setFilterCondition(indexSet.getString("FILTER_CONDITION"));
            }
            SqlIndexColumn column = index.addColumn(indexSet.getString("COLUMN_NAME"));
            column.setOrdinalPosition(indexSet.getInt("ORDINAL_POSITION"));
            column.setAscOrDesc(indexSet.getString("ASC_OR_DESC"));
        }
    }

    @SneakyThrows
    public void fillForeignKeys(SqlTable fkTable) {
        String[] catalogSchema = fetchCatalogSchema(fkTable.getOwner());
        ResultSet foreignKeySet = metaData.getImportedKeys(catalogSchema[0], catalogSchema[1], fkTable.getName());
        while (foreignKeySet.next()) {
            String fkName = foreignKeySet.getString("FK_NAME");
            SqlTableForeignKey fk = fkTable.getForeignKey(fkName);
            if (fk == null) {
                fk = fkTable.addForeignKey(fkName);
                catalogSchema = fetchCatalogSchema(foreignKeySet.getString("PKTABLE_CAT"), foreignKeySet.getString("PKTABLE_SCHEM"));
                fk.setPkTable(catalogs.get(catalogSchema[0]).getSchema(catalogSchema[1]).getTable(foreignKeySet.getString("PKTABLE_NAME")));
                fk.setPkName(foreignKeySet.getString("PK_NAME"));
                fk.setDeferability(foreignKeySet.getShort("DEFERRABILITY"));
                fk.setDeleteRule(foreignKeySet.getShort("DELETE_RULE"));
                fk.setUpdateRule(foreignKeySet.getShort("UPDATE_RULE"));
            }
            fk.addColumn(fkTable.getColumn(foreignKeySet.getString("FKCOLUMN_NAME")),
                    foreignKeySet.getShort("KEY_SEQ"),
                    fkTable.getColumn(foreignKeySet.getString("PKCOLUMN_NAME")));
        }
    }

    @SneakyThrows
    public void fillView(SqlView view) {
        String[] catalogSchema = fetchCatalogSchema(view.getOwner());
        ResultSet columnsSet = metaData.getColumns(catalogSchema[0], catalogSchema[1], view.getName(), null);
        while (columnsSet.next()) {
            fillTabularColumn(columnsSet, view.addColumn(columnsSet.getString("COLUMN_NAME")));
        }
    }

    private String[] fetchCatalogSchema(SqlSchema schema) {
        String catalog = schema.getOwner().getName();
        String schemaPattern = schema.getName();
        return fetchCatalogSchema(catalog, schemaPattern);
    }

    private String[] fetchCatalogSchema(String catalog, String schemaPattern) {
        if (engine == SqlEngine.MYSQL) {
            catalog = schemaPattern;
            schemaPattern = null;
        }
        return new String[]{catalog, schemaPattern};
    }

    private void fillTabularColumn(ResultSet columnsSet, SqlTabularColumn column) throws SQLException {
        column.setDataType(columnsSet.getInt("DATA_TYPE"));
        column.setTypeName(columnsSet.getString("TYPE_NAME"));
        column.setColumnSize(columnsSet.getInt("COLUMN_SIZE"));
        column.setDecimalDigits(columnsSet.getInt("DECIMAL_DIGITS"));
        column.setNumPrecRadix(columnsSet.getInt("NUM_PREC_RADIX"));
        switch (columnsSet.getInt("NULLABLE")) {
            case DatabaseMetaData.columnNoNulls:
                column.setNullable(false);
                break;
            case DatabaseMetaData.columnNullable:
                column.setNullable(true);
                break;
            default:
                column.setNullable(fromYesNoEmpty(columnsSet.getString("IS_NULLABLE")));
        }
        column.setRemarks(columnsSet.getString("REMARKS"));
        column.setColumnDef(columnsSet.getString("COLUMN_DEF"));
        column.setCharOctetLength(columnsSet.getInt("CHAR_OCTET_LENGTH"));
        column.setOrdinalPosition(columnsSet.getInt("ORDINAL_POSITION"));
        column.setAutoincrement(fromYesNoEmpty(columnsSet.getString("IS_AUTOINCREMENT")));
        column.setGeneratedcolumn(fromYesNoEmpty(columnsSet.getString("IS_GENERATEDCOLUMN")));
    }

}
