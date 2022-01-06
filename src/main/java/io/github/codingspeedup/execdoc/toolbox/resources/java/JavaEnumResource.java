package io.github.codingspeedup.execdoc.toolbox.resources.java;

public class JavaEnumResource extends JavaTypeResource {

    public JavaEnumResource(String className) {
        this(className, (Object[]) null);
    }

    public JavaEnumResource(String className, Object... extras) {
        super(className, extras);
    }

    @Override
    public String toString() {
        return "(E) " + super.toString();
    }

}
