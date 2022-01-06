package io.github.codingspeedup.execdoc.toolbox.resources.filtering;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class CombinedFilter implements ResourceFilter {

    protected final List<ResourceFilter> filters = new ArrayList<>();

    public CombinedFilter(ResourceFilter... filters) {
        if (ArrayUtils.isNotEmpty(filters)) {
            for (ResourceFilter filter : filters) {
                if (filter != null) {
                    this.filters.add(filter);
                }
            }
        }
    }

}
