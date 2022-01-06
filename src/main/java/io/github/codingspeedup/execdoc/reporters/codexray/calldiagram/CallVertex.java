package io.github.codingspeedup.execdoc.reporters.codexray.calldiagram;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import io.github.codingspeedup.execdoc.reporters.codexray.DiagramVertex;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaParserUtility;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

public class CallVertex extends DiagramVertex {

    public static final int FLAG_START = 0b0000_0000_0000_0000_0000_0000_0000_0001;

    @Getter
    private String typeName;
    @Getter
    private final String methodName;
    @Getter
    private final String methodSignature;
    @Getter
    private final String vertexId;

    @Getter
    private final MethodDeclaration methodDeclaration;

    public CallVertex(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
        Node typeDeclaration = JavaParserUtility.findDeclaringParent(methodDeclaration).orElseThrow(UnsupportedOperationException::new);
        JavaParserUtility.getTypeFullyQualifiedName(typeDeclaration).ifPresent(name -> typeName = name);
        methodName = methodDeclaration.getNameAsString();
        methodSignature = JavaParserUtility.buildMethodSignature(methodDeclaration);
        vertexId = ("N" + (typeName + "." + methodName + "(" + methodSignature + ")").hashCode()).replace('-', '_');
    }

    public String getTypeSimpleName() {
        return typeName == null ? "?" : FilenameUtils.getExtension(typeName);
    }

    @Override
    public String toString() {
        return typeName + "." + methodName + "(" + methodSignature + ")";
    }

}
