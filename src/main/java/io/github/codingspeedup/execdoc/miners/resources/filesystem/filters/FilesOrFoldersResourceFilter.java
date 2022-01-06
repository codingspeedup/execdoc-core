package io.github.codingspeedup.execdoc.miners.resources.filesystem.filters;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileOrFolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.ResourceFilter;

public class FilesOrFoldersResourceFilter implements ResourceFilter {

    @Override
    public boolean accept(Resource resource) {
        return resource instanceof FileOrFolderResource;
    }

}
