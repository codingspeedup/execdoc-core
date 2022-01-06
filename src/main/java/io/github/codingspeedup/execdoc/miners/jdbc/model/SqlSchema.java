package io.github.codingspeedup.execdoc.miners.jdbc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlSchema extends SqlElement {

    private final Map<String, SqlTable> tables = new LinkedHashMap<>();
    private final Map<String, SqlView> views = new LinkedHashMap<>();

    public SqlSchema(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public SqlCatalog getOwner() {
        return (SqlCatalog) super.getOwner();
    }

    public void setOwner(SqlCatalog owner) {
        super.setOwner(owner);
    }

    public SqlTable addTable(String name) {
        return tables.computeIfAbsent(name, key -> new SqlTable(key, this));
    }

    public SqlTable getTable(String name) {
        return tables.get(name);
    }

    public List<String> getTableNames() {
        return new ArrayList<>(tables.keySet());
    }

    public SqlView addView(String name) {
        return views.computeIfAbsent(name, key -> new SqlView(key, this));
    }

    public SqlView getView(String name) {
        return views.get(name);
    }

    public List<String> getViewNames() {
        return new ArrayList<>(views.keySet());
    }

}
