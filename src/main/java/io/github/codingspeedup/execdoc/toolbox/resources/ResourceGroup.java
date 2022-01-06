package io.github.codingspeedup.execdoc.toolbox.resources;

import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileOrFolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filtering.ResourceFilter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ResourceGroup extends Resource {

    @Getter
    private final List<Resource> children;

    ResourceGroup(ResourceGroupBuilder builder) {
        super(builder.getName());
        children = builder.getResources();
    }

    public static ResourceGroupBuilder builder() {
        return new ResourceGroupBuilder();
    }

    @SneakyThrows
    public Iterator<File> fileIterator(FileFilter fileFilter) {
        List<File> discovered = new ArrayList<>();
        for (Resource resource : children) {
            if (!(resource instanceof FileOrFolderResource)) {
                continue;
            }
            File location = ((FileOrFolderResource) resource).getFile();
            if (location.isFile() && (fileFilter == null || fileFilter.accept(location))) {
                discovered.add(location);
            }
            try (Stream<Path> walk = Files.walk(location.toPath())) {
                walk.forEach(path -> {
                    File file = path.toFile();
                    if (fileFilter == null || fileFilter.accept(file)) {
                        discovered.add(file);
                    }
                });
            }
        }
        return discovered.iterator();
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(children);
    }

    public ResourceGroup filter(ResourceFilter filter) {
        ResourceGroupBuilder builder = ResourceGroup.builder();
        builder.name(getName() + "|" + filter.getClass().getSimpleName());
        for (Resource resource : children) {
            if (filter.accept(resource)) {
                builder.res(resource);
            }
        }
        return builder.build();
    }

}
