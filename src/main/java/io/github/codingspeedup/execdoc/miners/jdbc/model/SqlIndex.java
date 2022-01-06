package io.github.codingspeedup.execdoc.miners.jdbc.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlIndex extends SqlColumnSet {

    private final Map<String, SqlIndexColumn> columns = new LinkedHashMap<>();
    @Getter
    @Setter
    private boolean nonUnique;
    @Getter
    @Setter
    private String indexQualifier;
    @Getter
    @Setter
    private int type;
    @Getter
    @Setter
    private long cardinality;
    @Getter
    @Setter
    private long pages;
    @Getter
    @Setter
    private String filterCondition;

    public SqlIndex(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public SqlIndexColumn addColumn(String name) {
        return columns.computeIfAbsent(name, key -> new SqlIndexColumn(key, this));
    }

    @Override
    public SqlIndexColumn getColumn(String name) {
        return columns.get(name);
    }

    @Override
    public List<String> getColumnNames() {
        return new ArrayList<>(columns.keySet());
    }

}
