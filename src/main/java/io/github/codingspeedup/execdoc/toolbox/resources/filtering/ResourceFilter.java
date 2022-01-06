package io.github.codingspeedup.execdoc.toolbox.resources.filtering;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;

@FunctionalInterface
public interface ResourceFilter {

    boolean accept(Resource resource);

}
