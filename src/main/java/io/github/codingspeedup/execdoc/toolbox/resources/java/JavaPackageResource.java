package io.github.codingspeedup.execdoc.toolbox.resources.java;

public class JavaPackageResource extends JavaResource {

    public JavaPackageResource(String className) {
        this(className, (Object[]) null);
    }

    public JavaPackageResource(String description, Object... extras) {
        super(description, extras);
    }

    @Override
    public String toString() {
        return "(P) " + super.toString();
    }

}
