package io.github.codingspeedup.execdoc.toolbox.resources.java;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

public abstract class JavaResource extends Resource {

    @Getter
    @Setter
    private File javaSourceFile;
    @Getter
    @Setter
    private String packageName;

    public JavaResource(String description, Object... extras) {
        super(description);
        if (ArrayUtils.isNotEmpty(extras)) {
            for (Object extra : extras) {
                if (extra instanceof File) {
                    setJavaSourceFile((File) extra);
                }
            }
        }
    }

}
