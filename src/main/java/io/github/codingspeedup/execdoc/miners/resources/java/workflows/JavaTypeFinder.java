package io.github.codingspeedup.execdoc.miners.resources.java.workflows;

import com.github.javaparser.ast.body.TypeDeclaration;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import io.github.codingspeedup.execdoc.toolbox.resources.DefaultResourceFactory;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaTypeResource;
import io.github.codingspeedup.execdoc.toolbox.workflow.StatefulHandler;

import java.util.ArrayList;
import java.util.List;

public class JavaTypeFinder extends StatefulHandler<JavaMinerState, JavaDocument, List<JavaTypeResource>> {

    @Override
    public List<JavaTypeResource> process(JavaDocument input) {
        List<JavaTypeResource> classResources = new ArrayList<>();
        for (TypeDeclaration<?> typeDeclaration : input.getCompilationUnit().getTypes()) {
            classResources.add((JavaTypeResource) DefaultResourceFactory.INSTANCE.from(null, typeDeclaration));
        }
        return classResources;
    }

}
