package io.github.codingspeedup.execdoc.miners.resources;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceVisitor;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceVisitorAction;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.ResourceFilter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractResourceMiner implements ResourceVisitor {

    protected final List<ResourceMinerListener> mineListeners = new ArrayList<>();
    private final ResourceGroup resourceGroup;

    public AbstractResourceMiner(ResourceGroup resourceGroup, ResourceMinerListener... listeners) {
        this.resourceGroup = resourceGroup;
        for (ResourceMinerListener listener : listeners) {
            if (listener != null) {
                mineListeners.add(listener);
            }
        }
        if (ArrayUtils.isEmpty(listeners)) {
            mineListeners.add(new DefaultResourceMinerListener());
        }
    }

    public void scan(ResourceFilter filter) {
        for (Resource resource : resourceGroup.getChildren()) {
            if (filter == null || filter.accept(resource)) {
                resourceGroup.visit(filter, this);
            }
        }
    }

    @Override
    public ResourceVisitorAction visit(ResourceFilter filter, Resource resource) {
        for (ResourceMinerListener listener : mineListeners) {
            listener.onResourceDiscovered(resource);
        }
        return ResourceVisitorAction.PROCESS_CHILDREN;
    }

    @Override
    public List<Resource> buildChildren(Resource tResource) {
        return ResourceVisitor.super.buildChildren(tResource);
    }

}
