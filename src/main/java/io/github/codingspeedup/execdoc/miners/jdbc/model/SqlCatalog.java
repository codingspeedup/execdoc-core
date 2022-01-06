package io.github.codingspeedup.execdoc.miners.jdbc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlCatalog extends SqlElement {

    private final Map<String, SqlSchema> schemas = new LinkedHashMap<>();

    public SqlCatalog(String name) {
        setName(name);
    }

    public SqlSchema addSchema(String name) {
        return schemas.computeIfAbsent(name, key -> new SqlSchema(key, this));
    }

    public SqlSchema getSchema(String name) {
        return schemas.get(name);
    }

    public List<String> getSchemaNames() {
        return new ArrayList<>(schemas.keySet());
    }

}
