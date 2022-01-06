package io.github.codingspeedup.execdoc.toolbox.resources.java;

import org.json.JSONObject;

public class JavaMethodResource extends JavaResource {

    private static final String PUBLIC = "public";

    public JavaMethodResource(String className) {
        this(className, (Object[]) null);
    }

    public JavaMethodResource(String description, Object... extras) {
        super(description, extras);
    }

    @Override
    public String toString() {
        String description = getDescription();
        return "(M) " + description.substring(0, description.indexOf('('));
    }

    public boolean isPublic() {
        return getProperties() != null && getProperties().getBoolean(PUBLIC);
    }

    public void setPublic(boolean flag) {
        if (flag) {
            if (getProperties() == null) {
                setProperties(new JSONObject());
            }
            getProperties().put(PUBLIC, true);
        } else {
            if (getProperties() != null) {
                getProperties().put(PUBLIC, false);
            }
        }
    }

}
