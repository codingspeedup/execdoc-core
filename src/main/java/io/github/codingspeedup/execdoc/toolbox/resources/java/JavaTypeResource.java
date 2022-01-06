package io.github.codingspeedup.execdoc.toolbox.resources.java;

import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

public abstract class JavaTypeResource extends JavaResource {

    @Getter
    @Setter
    private Class<?> javaClass;
    @Getter
    @Setter
    private TypeDeclaration<?> declaration;

    public JavaTypeResource(String className, Object... extras) {
        super(className, extras);
        if (ArrayUtils.isNotEmpty(extras)) {
            for (Object extra : extras) {
                if (extra instanceof Class) {
                    javaClass = (Class<?>) extra;
                } else if (extra instanceof TypeDeclaration) {
                    declaration = (TypeDeclaration<?>) extra;
                }
            }
        }
    }

    @Override
    public String getName() {
        String className = getDescription();
        int dotIndex = className.lastIndexOf('.');
        return dotIndex < 0 ? className : className.substring(dotIndex + 1);
    }

    @Override
    public String toString() {
        return getName();
    }

}
