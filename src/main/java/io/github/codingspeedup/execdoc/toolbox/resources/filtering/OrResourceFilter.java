package io.github.codingspeedup.execdoc.toolbox.resources.filtering;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;

public class OrResourceFilter extends CombinedFilter {

    public OrResourceFilter(ResourceFilter... filters) {
        super(filters);
    }

    @Override
    public boolean accept(Resource resource) {
        for (ResourceFilter filter : filters) {
            if (filter.accept(resource)) {
                return true;
            }
        }
        return false;
    }

}
