package io.github.codingspeedup.execdoc.toolbox.resources;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaAnnotationResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaClassResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaEnumResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaInterfaceResource;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaParserUtility;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaType;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class DefaultResourceFactory implements ResourceFactory {

    public static final DefaultResourceFactory INSTANCE = new DefaultResourceFactory();

    @SneakyThrows
    @Override
    public Resource from(String descriptor, Object... hints) {
        if (StringUtils.isNotBlank(descriptor)) {
            String[] descriptorChunks = descriptor.split(Resource.RESOURCE_CLASS_SEPARATOR, 2);
            String resourceClassName = descriptorChunks[0];
            String resourceDescription = "";
            if (descriptorChunks.length > 1) {
                resourceDescription = descriptorChunks[1];
            }
            return (Resource) Class.forName(resourceClassName).getConstructor(String.class).newInstance(resourceDescription);
        }
        if (ArrayUtils.isNotEmpty(hints)) {
            for (Object hint : hints) {
                if (hint instanceof File) {
                    File file = (File) hint;
                    if (file.exists()) {
                        if (file.isFile()) {
                            return new FileResource(file.getCanonicalPath());
                        } else if (file.isDirectory()) {
                            return new FolderResource(file.getCanonicalPath());
                        }
                    }
                } else if (hint instanceof TypeDeclaration<?>) {
                    TypeDeclaration<?> typeDeclaration = (TypeDeclaration<?>) hint;
                    String typeName = typeDeclaration.getFullyQualifiedName().orElse(typeDeclaration.getNameAsString());
                    JavaType javaType = JavaParserUtility.inferJavaType(typeDeclaration);
                    switch (javaType) {
                        case C:
                            JavaClassResource classType = new JavaClassResource(typeName, typeDeclaration);
                            classType.setAbstract(((ClassOrInterfaceDeclaration) typeDeclaration).isAbstract());
                            return classType;
                        case I:
                            return new JavaInterfaceResource(typeName, typeDeclaration);
                        case E:
                            return new JavaEnumResource(typeName, typeDeclaration);
                        case A:
                            return new JavaAnnotationResource(typeName, typeDeclaration);
                    }
                }
            }
        }
        return null;
    }

}
