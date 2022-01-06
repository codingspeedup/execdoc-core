package io.github.codingspeedup.execdoc.miners.resources.filesystem.filters;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.ResourceFilter;

public class FoldersOnlyResourcesFilter implements ResourceFilter {

    @Override
    public boolean accept(Resource resource) {
        return resource instanceof FolderResource;
    }

}
