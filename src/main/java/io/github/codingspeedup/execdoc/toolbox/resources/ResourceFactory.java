package io.github.codingspeedup.execdoc.toolbox.resources;

public interface ResourceFactory {

    Resource from(String descriptor, Object... hints);

}
