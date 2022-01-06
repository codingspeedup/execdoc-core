package io.github.codingspeedup.execdoc.miners.resources.filesystem;

import io.github.codingspeedup.execdoc.miners.resources.filesystem.filters.FilesOrFoldersResourceFilter;
import io.github.codingspeedup.execdoc.miners.resources.AbstractResourceMiner;
import io.github.codingspeedup.execdoc.miners.resources.ResourceMinerListener;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;

public class FilesystemMiner extends AbstractResourceMiner {

    public FilesystemMiner(ResourceGroup resources, ResourceMinerListener... listeners) {
        super(resources.filter(new FilesOrFoldersResourceFilter()), listeners);
    }

}
