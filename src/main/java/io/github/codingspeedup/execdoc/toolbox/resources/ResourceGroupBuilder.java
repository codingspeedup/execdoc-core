package io.github.codingspeedup.execdoc.toolbox.resources;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ResourceGroupBuilder {

    private final List<Resource> resources = new ArrayList<>();
    private String name;
    private ResourceFactory factory = new DefaultResourceFactory();

    public ResourceGroupBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ResourceGroupBuilder factory(ResourceFactory factory) {
        this.factory = factory;
        return this;
    }

    public ResourceGroupBuilder res(Resource resource) {
        this.resources.add(resource);
        return this;
    }

    @SneakyThrows
    public ResourceGroupBuilder res(File... resources) {
        for (File res : resources) {
            if (res != null) {
                this.res((String) null, res);
            }
        }
        return this;
    }

    public ResourceGroupBuilder res(String description, Object... hints) {
        Resource resource = factory.from(description, hints);
        if (resource != null) {
            resources.add(resource);
        }
        return this;
    }

    public ResourceGroup build() {
        return new ResourceGroup(this);
    }

}
