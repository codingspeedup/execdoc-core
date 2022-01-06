package io.github.codingspeedup.execdoc.reporters.codexray.classdiagram;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.Collectors;

public class TypeMethod extends TypeMember {

    private ConstructorDeclaration constructorDeclaration;
    private MethodDeclaration methodDeclaration;
    private String arguments;

    @Getter
    @Setter
    private boolean abstractFlag;

    public TypeMethod(MethodDeclaration declaration) {
        if (declaration.isPublic()) {
            setVisibility(Modifier.Keyword.PUBLIC);
        } else if (declaration.isProtected()) {
            setVisibility(Modifier.Keyword.PROTECTED);
        } else if (declaration.isPrivate()) {
            setVisibility(Modifier.Keyword.PRIVATE);
        } else {
            setVisibility(Modifier.Keyword.DEFAULT);
        }
        setStaticFlag(declaration.isStatic());
        setAbstractFlag(declaration.isAbstract());
        setNameString(declaration.getNameAsString());
        this.methodDeclaration = declaration;
    }

    public TypeMethod(String name, String arguments) {
        setNameString(name);
        this.arguments = arguments;
        setNameString(methodDeclaration.getNameAsString());
    }

    public TypeMethod(Pair<String, String> nameSignature) {
        this(nameSignature.getLeft(), nameSignature.getRight());
    }

    public TypeMethod(ConstructorDeclaration declaration) {
        if (declaration.isPublic()) {
            setVisibility(Modifier.Keyword.PUBLIC);
        } else if (declaration.isProtected()) {
            setVisibility(Modifier.Keyword.PROTECTED);
        } else if (declaration.isPrivate()) {
            setVisibility(Modifier.Keyword.PRIVATE);
        } else {
            setVisibility(Modifier.Keyword.DEFAULT);
        }
        setNameString(declaration.getNameAsString());
        constructorDeclaration = declaration;
    }

    public String getArgumentsString() {
        if (arguments == null) {
            if (methodDeclaration != null) {
                arguments = methodDeclaration.getParameters().stream()
                        .map(Parameter::toString)
                        .collect(Collectors.joining(", "));
            } else if (constructorDeclaration != null) {
                arguments = constructorDeclaration.getParameters().stream()
                        .map(Parameter::toString)
                        .collect(Collectors.joining(", "));
            } else {
                arguments = "N/A";
            }
        }
        return arguments;
    }

    @Override
    public String getTypeString() {
        return methodDeclaration == null ? "" : methodDeclaration.getType().asString();
    }

}
