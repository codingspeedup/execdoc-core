package io.github.codingspeedup.execdoc.miners.resources.java.workflows;

import com.github.javaparser.ast.CompilationUnit;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import io.github.codingspeedup.execdoc.toolbox.workflow.StatefulHandler;
import io.github.codingspeedup.execdoc.toolbox.workflow.WorkflowException;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;

public class CompilationUnitParser extends StatefulHandler<JavaMinerState, File, JavaDocument> {

    @Override
    public JavaDocument process(File file) {
        try {
            JavaDocument jDoc = new JavaDocument(file);
            CompilationUnit cUnit = jDoc.getCompilationUnit();
            if (CollectionUtils.isEmpty(cUnit.getChildNodes())) {
                throw new WorkflowException(this, "Could not read Compilation Unit from " + file.getCanonicalPath());
            }
            getSharedState().setJDoc(jDoc);
            return jDoc;
        } catch (Exception e) {
            throw new WorkflowException(this, e);
        }
    }

}
