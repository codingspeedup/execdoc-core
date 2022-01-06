package io.github.codingspeedup.execdoc.miners.resources.java.filters;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.ResourceFilter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class JavaFileFilter implements ResourceFilter {

    @Override
    public boolean accept(Resource resource) {
        if (resource instanceof FileResource) {
            File file = ((FileResource) resource).getFile();
            if (file.isFile()) {
                String name = file.getName();
                if ("java".equalsIgnoreCase(FilenameUtils.getExtension(name))) {
                    return !"package-info.java".equalsIgnoreCase(name);
                }
            }
        }
        return false;
    }

}
