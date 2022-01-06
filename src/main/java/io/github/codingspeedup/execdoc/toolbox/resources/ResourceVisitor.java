package io.github.codingspeedup.execdoc.toolbox.resources;

import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.ResourceFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface ResourceVisitor {

    ResourceVisitorAction visit(ResourceFilter filter, Resource resource);

    default List<Resource> buildChildren(Resource resource) {
        if (resource instanceof ResourceGroup) {
            return ((ResourceGroup) resource).getChildren();
        } else if (resource instanceof FolderResource) {
            File folder = ((FolderResource) resource).getFile();
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                List<Resource> children = new ArrayList<>();
                for (File file : files) {
                    children.add(DefaultResourceFactory.INSTANCE.from(null, file));
                }
                return children;
            }
        }
        return null;
    }

}
