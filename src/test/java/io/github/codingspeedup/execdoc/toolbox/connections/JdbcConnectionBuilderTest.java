package io.github.codingspeedup.execdoc.toolbox.connections;

import io.github.codingspeedup.execdoc.miners.jdbc.DatabaseMetaDataMiner;
import io.github.codingspeedup.execdoc.miners.jdbc.model.SqlSchema;
import io.github.codingspeedup.execdoc.miners.jdbc.model.SqlTable;
import io.github.codingspeedup.execdoc.toolbox.utilities.SqlTypesMapper;
import io.github.codingspeedup.execdoc.miners.jdbc.model.SqlTableColumn;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnectionBuilderTest {

    @Test
    public void toMySql() throws SQLException {
        JdbcConnectionBuilder builder = JdbcConnectionBuilder.toMySql("//localhost/dex").user("root").password("Root_123");
        try (Connection conn = builder.build()) {
            DatabaseMetaDataMiner miner = new DatabaseMetaDataMiner(conn.getMetaData());

            System.out.println();
            SqlSchema schema = miner.getSchema("dex");
            miner.findObjects(schema);
            System.out.println("Tables " + schema.getTableNames());
            System.out.println(" Views " + schema.getViewNames());

            System.out.println();
            SqlTable table = schema.getTable("Lexeme");
            miner.fillTable(table);
            for (String name : table.getColumnNames()) {
                SqlTableColumn column = table.getColumn(name);
                System.out.println("Column " + name + ": " + SqlTypesMapper.toTypesName(column.getDataType()));
            }
            System.out.println("PK " + table.getPrimaryKey());

            System.out.println();
            System.out.println("Done.");
        }
    }

}