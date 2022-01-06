package io.github.codingspeedup.execdoc.miners.resources.java;

import io.github.codingspeedup.execdoc.miners.resources.filesystem.FilesystemMiner;
import io.github.codingspeedup.execdoc.miners.resources.filesystem.filters.FoldersOnlyResourcesFilter;
import io.github.codingspeedup.execdoc.miners.resources.java.filters.JavaFileFilter;
import io.github.codingspeedup.execdoc.miners.resources.ResourceMinerListener;
import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceVisitorAction;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.AndResourceFilter;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.OrResourceFilter;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.ResourceFilter;

public class JavaFilesMiner extends FilesystemMiner {

    public JavaFilesMiner(ResourceGroup resources, ResourceMinerListener... listeners) {
        super(resources, listeners);
    }

    @Override
    public void scan(ResourceFilter filter) {
        super.scan(
                new OrResourceFilter(
                        new FoldersOnlyResourcesFilter(),
                        new AndResourceFilter(
                                new JavaFileFilter(),
                                filter
                        )
                )
        );
    }

    @Override
    public ResourceVisitorAction visit(ResourceFilter filter, Resource resource) {
        if (!(resource instanceof FolderResource)) {
            for (ResourceMinerListener listener : mineListeners) {
                listener.onResourceDiscovered(resource);
            }
        }
        return ResourceVisitorAction.PROCESS_CHILDREN;
    }

}
