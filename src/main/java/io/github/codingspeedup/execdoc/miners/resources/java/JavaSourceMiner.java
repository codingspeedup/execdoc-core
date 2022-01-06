package io.github.codingspeedup.execdoc.miners.resources.java;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import io.github.codingspeedup.execdoc.miners.resources.java.workflows.PartiallyResolvedType;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaSourceMiner {

    private final List<Folder> roots;
    private final JavaParserFacade parserFacade;

    public JavaSourceMiner(ResourceGroup group) {
        this(group.getChildren().stream().filter(FolderResource.class::isInstance).map(FolderResource.class::cast).map(FolderResource::getFile).collect(Collectors.toList()));
    }

    public JavaSourceMiner(List<File> roots) {
        this.roots = roots.stream().map(Folder::new).collect(Collectors.toList());
        CombinedTypeSolver solver = new CombinedTypeSolver(new ReflectionTypeSolver());
        this.roots.stream().map(JavaParserTypeSolver::new).forEach(solver::add);
        parserFacade = JavaParserFacade.get(solver);
    }

    public static File getTypeSourceFile(ImmutableTriple<Folder, String, String[]> location) {
        if (location != null) {
            return new File(location.getLeft(), location.getMiddle().replace('.', File.separatorChar) + File.separatorChar + location.getRight()[0] + ".java");
        }
        return null;
    }

    public Optional<ResolvedType> resolveType(Node node) {
        if (node != null) {
            try {
                if (node instanceof ClassOrInterfaceType) {
                    return Optional.of(parserFacade.convertToUsage((ClassOrInterfaceType) node));
                }
                return Optional.of(parserFacade.getType(node));
            } catch (UnsolvedSymbolException e) {
                if (node instanceof ClassOrInterfaceType) {
                    ClassOrInterfaceType typeNode = (ClassOrInterfaceType) node;
                    String typeSimpleName = typeNode.getNameAsString();
                    ResolvedType solution = resolveType("java.lang." + typeSimpleName);
                    if (solution != null) {
                        return Optional.of(solution);
                    }

                    CompilationUnit compilationUnit = (CompilationUnit) node.findRootNode();
                    String typeHint = "." + typeSimpleName;

                    for (ImportDeclaration foo : compilationUnit.getImports()) {
                        String importName = foo.getNameAsString();
                        if (foo.isAsterisk()) {
                            solution = resolveType(importName + "." + typeSimpleName);
                            if (solution != null) {
                                return Optional.of(solution);
                            }
                        } else if (importName.endsWith(typeHint)) {
                            solution = resolveType(importName);
                            return Optional.of(Objects.requireNonNullElse(solution, new PartiallyResolvedType(importName)));
                        }
                    }

                    String typeString = typeNode.toString();
                    String[] typePath = typeString.split("\\.");
                    typeSimpleName = typePath[0];
                    typeHint = "." + typeSimpleName;

                    for (ImportDeclaration foo : compilationUnit.getImports()) {
                        String importName = foo.getNameAsString();
                        if (foo.isAsterisk()) {
                            solution = resolveType(importName + "." + typeSimpleName);
                            if (solution != null) {
                                return Optional.of(solution);
                            }
                        } else if (importName.endsWith(typeHint)) {
                            solution = resolveType(importName);
                            return Optional.of(Objects.requireNonNullElse(solution, new PartiallyResolvedType(importName + typeString.substring(typeSimpleName.length()))));
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    private ResolvedType resolveType(String typeName) {
        try {
            return parserFacade.classToResolvedType(Class.forName(typeName));
        } catch (ClassNotFoundException ex) {
            ImmutableTriple<Folder, String, String[]> location = locateType(typeName);
            if (location != null) {
                System.out.println(location);
            }
        }
        return null;
    }

    public ImmutableTriple<Folder, String, String[]> locateType(String typeQualifiedName) {
        String[] path = typeQualifiedName.split("\\.");
        for (Folder root : roots) {
            File container = root;
            for (int i = 0; i < path.length; ++i) {
                String step = path[i];
                File foo = new File(container, step);
                if (!foo.exists()) {
                    foo = new File(container, step + ".java");
                    if (foo.exists()) {
                        String packageName = Arrays.stream(path).limit(i).collect(Collectors.joining("."));
                        String[] typePath = new String[path.length - i];
                        System.arraycopy(path, i, typePath, 0, typePath.length);
                        return new ImmutableTriple<>(root, packageName, typePath);
                    } else {
                        break;
                    }
                }
                container = foo;
            }
        }
        return null;
    }

}
