package io.github.codingspeedup.execdoc.miners.jdbc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlView extends SqlColumnSet {

    private final Map<String, SqlViewColumn> columns = new LinkedHashMap<>();

    public SqlView(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public SqlViewColumn addColumn(String name) {
        return columns.computeIfAbsent(name, key -> new SqlViewColumn(key, this));
    }

    @Override
    public SqlViewColumn getColumn(String name) {
        return columns.get(name);
    }

    @Override
    public List<String> getColumnNames() {
        return new ArrayList<>(columns.keySet());
    }

}
