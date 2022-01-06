package io.github.codingspeedup.execdoc.toolbox.workflow;

import java.util.Properties;

public abstract class SharedState {

    private Properties properties = new Properties();

    public Object get(String key) {
        return properties.get(key);
    }

    public String getString(String key) {
        return (String) properties.get(key);
    }

    public void set(String key, Object value) {
        properties.put(key, value);
    }

    public Object remove(String key) {
        return properties.remove(key);
    }

    @Override
    public String toString() {
        return properties.toString();
    }

}
