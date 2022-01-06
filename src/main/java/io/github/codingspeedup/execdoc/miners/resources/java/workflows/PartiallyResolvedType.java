package io.github.codingspeedup.execdoc.miners.resources.java.workflows;

import com.github.javaparser.resolution.types.ResolvedType;
import lombok.Getter;

public class PartiallyResolvedType implements ResolvedType {

    @Getter
    private String qualifiedName;

    public PartiallyResolvedType(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public boolean isAssignableBy(ResolvedType other) {
        return false;
    }

}
