package io.github.codingspeedup.execdoc.toolbox.documents.java;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaParserUtility {

    public static JavaType inferJavaType(TypeDeclaration<?> typeDeclaration) {
        if (typeDeclaration.isAnnotationDeclaration()) {
            return JavaType.A;
        } else if (typeDeclaration.isEnumDeclaration()) {
            return JavaType.E;
        } else {
            if (((ClassOrInterfaceDeclaration) typeDeclaration).isInterface()) {
                return JavaType.I;
            } else {
                return JavaType.C;
            }
        }
    }

    public static JavaType inferJavaType(ResolvedReferenceTypeDeclaration typeDeclaration) {
        if (typeDeclaration.isAnnotation()) {
            return JavaType.A;
        } else if (typeDeclaration.isEnum()) {
            return JavaType.E;
        } else {
            if (typeDeclaration.isInterface()) {
                return JavaType.I;
            } else {
                return JavaType.C;
            }
        }
    }

    public static String buildMethodSignature(MethodDeclaration declaration) {
        String description = declaration.getDeclarationAsString(false, false, false);
        return description.substring(description.indexOf("(") + 1, description.indexOf(")"));
    }

    public static String getTypeArguments(Type type) {
        if (type != null && type.isClassOrInterfaceType()) {
            Optional<NodeList<Type>> typeArguments = type.asClassOrInterfaceType().getTypeArguments();
            if (typeArguments.isPresent()) {
                return ("<" + typeArguments.get().stream().map(Type::toString).collect(Collectors.joining(",")) + ">").replaceAll("\\s+", "");
            }
        }
        return "";
    }

    public static ImmutablePair<String, String> splitMethodDescription(String description) {
        int nameEndIndex = description.indexOf("(");
        String name = nameEndIndex < 0 ? description : StringUtils.trimToEmpty(description.substring(0, nameEndIndex));
        String signature = nameEndIndex < 0 ? null : StringUtils.trimToEmpty(description.substring(nameEndIndex + 1, description.lastIndexOf(")")));
        return ImmutablePair.of(name, signature);
    }

    public static MethodDeclaration findMethodDeclaration(TypeDeclaration<?> typeDeclaration, String description) {
        int nameEndIndex = description.indexOf("(");
        String methodName = nameEndIndex < 0 ? description : StringUtils.trimToEmpty(description.substring(0, nameEndIndex));
        String methodSignature = nameEndIndex < 0 ? null : StringUtils.trimToEmpty(description.substring(nameEndIndex + 1, description.lastIndexOf(")")));
        return findMethodDeclaration(typeDeclaration, methodName, methodSignature);
    }

    public static MethodDeclaration findMethodDeclaration(TypeDeclaration<?> typeDeclaration, String methodName, List<String> methodSignature) {
        return findMethodDeclaration(typeDeclaration, methodName, String.join(", ", methodSignature));
    }

    public static MethodDeclaration findMethodDeclaration(TypeDeclaration<?> typeDeclaration, String methodName, String methodSignature) {
        List<MethodDeclaration> candidates = typeDeclaration.getMethodsByName(methodName);
        if (candidates.size() == 1 && methodSignature == null) {
            return candidates.get(0);
        }
        for (MethodDeclaration foo : candidates) {
            String fooSignature = JavaParserUtility.buildMethodSignature(foo);
            if (fooSignature.equals(methodSignature)) {
                return foo;
            }
        }
        return null;
    }

    public static Optional<Node> findDeclaringParent(Node node) {
        if (node == null) {
            return Optional.empty();
        }
        Optional<Node> optionalParent = node.getParentNode();
        if (optionalParent.isEmpty()) {
            return optionalParent;
        }
        Node parent = optionalParent.get();
        if (parent instanceof ClassOrInterfaceDeclaration) {
            return Optional.of(parent);
        }
        return findDeclaringParent(parent);
    }

    public static Optional<String> getTypeFullyQualifiedName(Node typeDeclaration) {
        List<String> reversedPath = new ArrayList<>();

        Optional<String> simpleName = getTypeSimpleName(typeDeclaration);
        if (simpleName.isEmpty()) {
            return simpleName;
        }
        reversedPath.add(simpleName.get());

        Optional<Node> optionalParent = findDeclaringParent(typeDeclaration);
        while (optionalParent.isPresent()) {
            reversedPath.add(getTypeSimpleName(optionalParent.get()).orElseThrow(UnsupportedOperationException::new));
            optionalParent = findDeclaringParent(optionalParent.get());
        }

        typeDeclaration.findCompilationUnit()
                .flatMap(CompilationUnit::getPackageDeclaration)
                .ifPresent(pd -> reversedPath.add(pd.getNameAsString()));
        reversedPath.sort(Comparator.reverseOrder());
        return Optional.of(String.join(".", reversedPath));
    }

    public static Optional<String> getTypeSimpleName(Node typeDeclaration) {
        if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
            return Optional.of(((ClassOrInterfaceDeclaration) typeDeclaration).getNameAsString());
        }
        return Optional.empty();
    }

    public static String getTypeFullyQualifiedName(ResolvedType declaringType) {
        return declaringType.asReferenceType().getQualifiedName();
    }

    public static Optional<String> getTypeSimpleName(ResolvedType type) {
        if (type.isPrimitive()) {
            return Optional.of(type.asPrimitive().describe());
        }
        return Optional.empty();
    }

}
