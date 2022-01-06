package io.github.codingspeedup.execdoc.toolbox.resources.filtering;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;

public class AndResourceFilter extends CombinedFilter {

    public AndResourceFilter(ResourceFilter... filters) {
        super(filters);
    }

    @Override
    public boolean accept(Resource resource) {
        for (ResourceFilter filter : filters) {
            if (!filter.accept(resource)) {
                return false;
            }
        }
        return true;
    }

}
