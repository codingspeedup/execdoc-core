package io.github.codingspeedup.execdoc.miners.jdbc.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class SqlElement {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String name;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private SqlElement owner;

    public SqlElement(String name, SqlElement owner) {
        this.name = name;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return name;
    }

}
