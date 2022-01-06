package io.github.codingspeedup.execdoc.reporters.codexray;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedType;
import io.github.codingspeedup.execdoc.miners.resources.java.JavaSourceMiner;
import io.github.codingspeedup.execdoc.miners.resources.java.workflows.PartiallyResolvedType;
import io.github.codingspeedup.execdoc.reporters.codexray.calldiagram.CallDiagram;
import io.github.codingspeedup.execdoc.reporters.codexray.calldiagram.CallVertex;
import io.github.codingspeedup.execdoc.reporters.codexray.classdiagram.*;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaParserUtility;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaMethodResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaTypeResource;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class XRayReporter {

    @Getter
    private final XRay xRay;
    private final JavaSourceMiner srcMiner;

    public XRayReporter(File homeFolder, ResourceGroup rGroup) {
        srcMiner = new JavaSourceMiner(rGroup);
        xRay = new XRay(homeFolder);
    }

    private static String getQualifiedName(ResolvedType resolvedType) {
        String qualifiedName = null;
        if (resolvedType != null) {
            if (resolvedType.isReferenceType()) {
                qualifiedName = resolvedType.asReferenceType().getQualifiedName();
            } else if (resolvedType instanceof PartiallyResolvedType) {
                qualifiedName = ((PartiallyResolvedType) resolvedType).getQualifiedName();
            }
        }
        return qualifiedName;
    }

    public XRay buildReport(Resource... references) {
        return buildReport(Arrays.asList(references));
    }

    public XRay buildReport(Collection<Resource> references) {

        List<JavaTypeResource> selectedTypes = references.stream()
                .filter(JavaTypeResource.class::isInstance)
                .map(JavaTypeResource.class::cast)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(selectedTypes)) {
            ClassDiagram diagram = new ClassDiagram();
            for (JavaTypeResource typeResource : selectedTypes) {
                maybeAddType(diagram, typeResource.getDescription())
                        .ifPresent(v -> {
                            if (!v.isFlag(ClassVertex.FLAG_SELECTED)) {
                                v.setFlags(ClassVertex.FLAG_SELECTED);
                                addTypeHierarchy(diagram, v);
                                addTypeComposition(diagram, v);
                            }
                        });
            }
            xRay.addClassDiagramsSection(diagram);
        }

        List<JavaMethodResource> selectedMethods = references.stream()
                .filter(JavaMethodResource.class::isInstance)
                .map(JavaMethodResource.class::cast)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(selectedMethods)) {
            xRay.addMethodsSection();
            for (JavaMethodResource methodResource : selectedMethods) {
                ClassDiagram classDiagram = new ClassDiagram();
                ClassVertex v = maybeAddType(classDiagram, methodResource.getParent().getDescription()).orElseThrow(UnsupportedOperationException::new);
                v.setFlags(ClassVertex.FLAG_REFERRED);
                v.getSelectedMethods().add(methodResource.getDescription());
                addTypeHierarchy(classDiagram, v);

                MethodDeclaration methodDeclaration = JavaParserUtility.findMethodDeclaration(v.getTypeDeclaration(), methodResource.getDescription());
                if (methodDeclaration == null) {
                    throw new UnsupportedOperationException();
                }

                CallDiagram callDiagram = new CallDiagram();
                CallVertex start = callDiagram.addVertex(new CallVertex(methodDeclaration));
                start.setFlags(CallVertex.FLAG_START);
                start.getMethodDeclaration().getBody().ifPresent(node -> exploreCalls(classDiagram, callDiagram, start, node));

                xRay.addMethodAnalysis(methodResource, classDiagram, callDiagram);
            }
        }

        return xRay;
    }

    private Optional<ClassVertex> maybeAddType(ClassDiagram diagram, String fullyQualifiedName) {
        Optional<ClassVertex> optionalClassVertex = diagram.getVertex(fullyQualifiedName);
        if (optionalClassVertex.isPresent()) {
            return optionalClassVertex;
        }
        ImmutableTriple<Folder, String, String[]> typeLocation = srcMiner.locateType(fullyQualifiedName);
        if (typeLocation == null) {
            return Optional.empty();
        }
        ClassVertex vertex = new ClassVertex(fullyQualifiedName);
        JavaDocument jDoc = new JavaDocument(JavaSourceMiner.getTypeSourceFile(typeLocation));
        vertex.setPackageName(typeLocation.getMiddle());
        vertex.setSimpleName(typeLocation.getRight()[typeLocation.getRight().length - 1]);
        vertex.setJavaDocument(jDoc);
        vertex.setTypeDeclaration(jDoc.getTypeDeclaration(typeLocation.getRight()));
        return Optional.of(diagram.addVertex(vertex));
    }

    private Optional<ClassVertex> maybeAddType(ClassDiagram diagram, Type type) {
        String qualifiedName = null;
        Optional<ResolvedType> optionalResolvedType = srcMiner.resolveType(type);
        if (optionalResolvedType.isPresent()) {
            qualifiedName = getQualifiedName(optionalResolvedType.get());
            Optional<ClassVertex> optionalClassVertex = maybeAddType(diagram, qualifiedName);
            if (optionalClassVertex.isPresent()) {
                return optionalClassVertex;
            }
        }
        String typeSimpleName = qualifiedName == null ? type.asString() : qualifiedName;
        String fullyQualifiedName = typeSimpleName + JavaParserUtility.getTypeArguments(type);
        Optional<ClassVertex> optionalClassVertex = diagram.getVertex(fullyQualifiedName);
        if (optionalClassVertex.isPresent()) {
            return optionalClassVertex;
        }
        ClassVertex vertex = new ClassVertex(fullyQualifiedName);
        vertex.setFlags(ClassVertex.FLAG_EXTERNAL);
        vertex.setSimpleName(typeSimpleName);
        vertex.setType(type);
        optionalResolvedType.ifPresent(vertex::setResolvedType);
        return Optional.of(diagram.addVertex(vertex));
    }

    private void addTypeHierarchy(ClassDiagram diagram, ClassVertex typeVertex) {
        if (typeVertex.getTypeDeclaration() == null) {
            return;
        }
        if (typeVertex.getTypeDeclaration().isClassOrInterfaceDeclaration()) {
            ClassOrInterfaceDeclaration typeDeclaration = (ClassOrInterfaceDeclaration) typeVertex.getTypeDeclaration();
            NodeList<ClassOrInterfaceType> extendedTypes = typeDeclaration.getExtendedTypes();
            for (ClassOrInterfaceType extendedType : extendedTypes) {
                ClassVertex parentVertex = maybeAddType(diagram, extendedType).orElse(null);
                if (parentVertex != null) {
                    diagram.addEdge(typeVertex, parentVertex, new ClassEdge(ClassRelation.EXTENDS));
                    addTypeHierarchy(diagram, parentVertex);
                }
            }
            NodeList<ClassOrInterfaceType> implementedTypes = typeDeclaration.getImplementedTypes();
            for (ClassOrInterfaceType implementedType : implementedTypes) {
                ClassVertex parentVertex = maybeAddType(diagram, implementedType).orElse(null);
                if (parentVertex != null) {
                    diagram.addEdge(typeVertex, parentVertex, new ClassEdge(ClassRelation.IMPLEMENTS));
                    addTypeHierarchy(diagram, parentVertex);
                }
            }
        } else if (typeVertex.getTypeDeclaration().isEnumDeclaration()) {
            EnumDeclaration typeDeclaration = (EnumDeclaration) typeVertex.getTypeDeclaration();
            NodeList<ClassOrInterfaceType> implementedTypes = typeDeclaration.getImplementedTypes();
            for (ClassOrInterfaceType implementedType : implementedTypes) {
                ClassVertex parentVertex = maybeAddType(diagram, implementedType).orElse(null);
                if (parentVertex != null) {
                    diagram.addEdge(typeVertex, parentVertex, new ClassEdge(ClassRelation.IMPLEMENTS));
                    addTypeHierarchy(diagram, parentVertex);
                }
            }
        }
    }

    private void addTypeComposition(ClassDiagram diagram, ClassVertex typeVertex) {
        for (TypeField field : typeVertex.getFields()) {
            Type type = field.getType();
            while (type.isArrayType()) {
                type = type.asArrayType().getComponentType();
            }
            if (type.isClassOrInterfaceType()) {
                maybeAddType(diagram, type)
                        .ifPresent(hasAVertex -> diagram.addEdge(typeVertex, hasAVertex, new ClassEdge(ClassRelation.HAS_A)));
            }
        }
    }

    private void addTypeAssociations(ClassDiagram classDiagram, ClassVertex typeVertex) {
    }

    private void exploreCalls(ClassDiagram classDiagram, CallDiagram callDiagram, CallVertex caller, Node node) {
        if (node instanceof MethodCallExpr) {
            CallVertex called;
            MethodCallExpr methodCall = (MethodCallExpr) node;
            MethodDeclaration declaration = resolveMethodDeclaration(classDiagram, caller.getMethodDeclaration(), methodCall);
            if (declaration != null) {
                called = new CallVertex(declaration);
                Optional<CallVertex> optionalCalled = callDiagram.getVertex(called.getVertexId());
                if (optionalCalled.isPresent()) {
                    callDiagram.addEdge(caller, optionalCalled.get());
                } else {
                    callDiagram.addEdge(caller, callDiagram.addVertex(called));
                    exploreCalls(classDiagram, callDiagram, called, called.getMethodDeclaration());
                }
            }
        } else {
            for (Node childNode : node.getChildNodes()) {
                exploreCalls(classDiagram, callDiagram, caller, childNode);
            }
        }
    }

    public MethodDeclaration resolveMethodDeclaration(ClassDiagram classDiagram, MethodDeclaration callerDeclaration, MethodCallExpr callExpr) {
        Optional<Expression> optionalScope = callExpr.getScope();
        List<String> declaringTypeCandidates = new ArrayList<>();
        if (optionalScope.isPresent()) {
            srcMiner.resolveType(optionalScope.get())
                    .ifPresent(declaringType -> declaringTypeCandidates.add(JavaParserUtility.getTypeFullyQualifiedName(declaringType)));
        } else {
            Node typeDeclaration = JavaParserUtility.findDeclaringParent(callerDeclaration).orElseThrow(UnsupportedOperationException::new);
            JavaParserUtility.getTypeFullyQualifiedName(typeDeclaration).ifPresent(declaringTypeCandidates::add);
            for (int cIdx = 0; cIdx < declaringTypeCandidates.size(); ++cIdx) {
                String declaringTypeName = declaringTypeCandidates.get(cIdx);
                ClassVertex v = classDiagram.getVertex(declaringTypeName).orElseThrow(UnsupportedOperationException::new);
                classDiagram.getCoreGraph().outgoingEdgesOf(v).stream()
                        .filter(e -> e.getRelation() == ClassRelation.EXTENDS || e.getRelation() == ClassRelation.IMPLEMENTS)
                        .forEach(e -> declaringTypeCandidates.add(classDiagram.getCoreGraph().getEdgeTarget(e).getVertexId()));
            }
        }
        if (CollectionUtils.isNotEmpty(declaringTypeCandidates)) {
            for (String declaringTypeName : declaringTypeCandidates) {
                ClassVertex v = classDiagram.getVertex(declaringTypeName).orElse(null);
                if (v != null && v.getTypeDeclaration() != null) {
                    MethodDeclaration methodDeclaration = JavaParserUtility.findMethodDeclaration(v.getTypeDeclaration(), callExpr.getNameAsString(), buildMethodSignature(callExpr.getArguments()));
                    if (methodDeclaration != null) {
                        return methodDeclaration;
                    }
                }
            }
        }
        return null;
    }

    private List<String> buildMethodSignature(NodeList<Expression> arguments) {
        List<String> names = new ArrayList<>();
        for (Expression argument : arguments) {
            Optional<ResolvedType> argType = srcMiner.resolveType(argument);
            if (argType.isPresent()) {
                Optional<String> simpleName = JavaParserUtility.getTypeSimpleName(argType.get());
                if (simpleName.isPresent()) {
                    names.add(simpleName.get());
                } else {
                    names.add("*");
                }
            } else {
                names.add("*");
            }
        }
        return names;
    }

}
