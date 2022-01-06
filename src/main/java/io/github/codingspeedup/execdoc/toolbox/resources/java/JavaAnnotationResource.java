package io.github.codingspeedup.execdoc.toolbox.resources.java;

public class JavaAnnotationResource extends JavaTypeResource {

    public JavaAnnotationResource(String className) {
        this(className, (Object[]) null);
    }

    public JavaAnnotationResource(String className, Object... extras) {
        super(className, extras);
    }

    @Override
    public String toString() {
        return "(@) " + super.toString();
    }

}
