package io.github.codingspeedup.execdoc.toolbox.resources.java;

import org.json.JSONObject;

public class JavaClassResource extends JavaTypeResource {

    public static final String ABSTRACT = "abstract";

    public JavaClassResource(String className) {
        this(className, (Object[]) null);
    }

    public JavaClassResource(String className, Object... extras) {
        super(className, extras);
    }

    public boolean isAbstract() {
        return getProperties() != null && getProperties().getBoolean(ABSTRACT);
    }

    public void setAbstract(boolean flag) {
        if (flag) {
            if (getProperties() == null) {
                setProperties(new JSONObject());
            }
            getProperties().put(ABSTRACT, true);
        } else {
            if (getProperties() != null) {
                getProperties().put(ABSTRACT, false);
            }
        }
    }

    @Override
    public String toString() {
        return (isAbstract() ? "(A) " : "(C) ") + super.toString();
    }

}
