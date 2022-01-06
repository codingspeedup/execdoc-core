package io.github.codingspeedup.execdoc.toolbox.resources.java;

public class JavaInterfaceResource extends JavaTypeResource {

    public JavaInterfaceResource(String className) {
        this(className, (Object[]) null);
    }

    public JavaInterfaceResource(String className, Object... extras) {
        super(className, extras);
    }

    @Override
    public String toString() {
        return "(I) " + super.toString();
    }

}
