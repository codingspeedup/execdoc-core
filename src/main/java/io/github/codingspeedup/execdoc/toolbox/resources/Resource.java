package io.github.codingspeedup.execdoc.toolbox.resources;

import io.github.codingspeedup.execdoc.toolbox.resources.filtering.ResourceFilter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;

import java.util.List;

public abstract class Resource {

    public static final String RESOURCE_CLASS_SEPARATOR = "=";

    @Getter
    private final String descriptor;
    @Getter
    @Setter
    private Resource parent;
    @Getter
    @Setter
    private JSONObject properties;

    public Resource(String description) {
        this.descriptor = getClass().getName() + RESOURCE_CLASS_SEPARATOR + (description == null ? "" : description);
    }

    public final String getDescription() {
        return descriptor.substring(descriptor.indexOf(RESOURCE_CLASS_SEPARATOR) + RESOURCE_CLASS_SEPARATOR.length());
    }

    public String getName() {
        return getDescription();
    }

    public ResourceVisitorAction visit(ResourceFilter filter, ResourceVisitor visitor) {
        List<Resource> children = visitor.buildChildren(this);
        if (CollectionUtils.isNotEmpty(children)) {
            for (Resource resource : children) {
                if (filter != null && !filter.accept(resource)) {
                    continue;
                }
                ResourceVisitorAction action = visitor.visit(filter, resource);
                if (action == ResourceVisitorAction.ABORT) {
                    return ResourceVisitorAction.ABORT;
                }
                if (action == ResourceVisitorAction.PROCESS_CHILDREN) {
                    action = resource.visit(filter, visitor);
                }
                if (action == ResourceVisitorAction.ABORT) {
                    return ResourceVisitorAction.ABORT;
                }
            }
        }
        return ResourceVisitorAction.PROCESS_SIBLING;
    }

    @Override
    public String toString() {
        return getName();
    }

}
