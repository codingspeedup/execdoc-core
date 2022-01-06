package io.github.codingspeedup.execdoc.reporters.codexray.classdiagram;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import lombok.Getter;

import java.util.List;

public class TypeField extends TypeMember {

    @Getter
    private final Type type;

    public TypeField(List<Modifier.Keyword> modifiers, SimpleName name, Type type) {
        VISIBILITY_MODIFIERS.stream().filter(modifiers::contains).findFirst().ifPresent(this::setVisibility);
        setStaticFlag(modifiers.contains(Modifier.Keyword.STATIC));
        setNameString(name.asString());
        this.type = type;
    }

    @Override
    public String getTypeString() {
        return type.asString();
    }

}
