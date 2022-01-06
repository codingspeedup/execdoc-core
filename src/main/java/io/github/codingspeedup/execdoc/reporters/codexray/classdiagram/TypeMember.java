package io.github.codingspeedup.execdoc.reporters.codexray.classdiagram;

import com.github.javaparser.ast.Modifier;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

public abstract class TypeMember {

    public static final List<Modifier.Keyword> VISIBILITY_MODIFIERS = Arrays.asList(Modifier.Keyword.PRIVATE, Modifier.Keyword.PROTECTED, Modifier.Keyword.PUBLIC);

    @Getter
    @Setter
    private Modifier.Keyword visibility;

    @Getter
    @Setter
    private boolean staticFlag;

    @Getter
    @Setter
    private String nameString;

    abstract public String getTypeString();

}
