package io.github.codingspeedup.execdoc.reporters.codexray.classdiagram;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import io.github.codingspeedup.execdoc.reporters.codexray.DiagramVertex;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaParserUtility;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class ClassVertex extends DiagramVertex {

    public static final int FLAG_EXTERNAL = 0b1000_0000_0000_0000_0000_0000_0000_0000;
    public static final int FLAG_SELECTED = 0b0000_0000_0000_0000_0000_0000_0000_0001;
    public static final int FLAG_REFERRED = 0b0000_0000_0000_0000_0000_0000_0000_0010;

    private final Set<String> selectedMethods = new HashSet<>();

    private String qualifiedName;
    private String packageName;
    private String simpleName;
    private JavaDocument javaDocument;
    private TypeDeclaration<?> typeDeclaration;
    private ResolvedType resolvedType;
    private Type type;

    public ClassVertex(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public String getVertexId() {
        return qualifiedName;
    }

    public ImmutablePair<String, JavaType> getTypeDeclarationKeyword() {
        JavaType javaType = JavaType.X;
        boolean isAbstract = false;
        if (typeDeclaration != null) {
            javaType = JavaParserUtility.inferJavaType(typeDeclaration);
            if (javaType == JavaType.C) {
                isAbstract = ((ClassOrInterfaceDeclaration) typeDeclaration).isAbstract();
            }
        } else if (resolvedType != null && resolvedType.isReferenceType()) {
            Optional<ResolvedReferenceTypeDeclaration> resolvedDeclaration = resolvedType.asReferenceType().getTypeDeclaration();
            if (resolvedDeclaration.isPresent()) {
                javaType = JavaParserUtility.inferJavaType(resolvedDeclaration.get());
            }
        }
        String keyword;
        switch (javaType) {
            case C:
                keyword = isAbstract ? "abstract" : "class";
                break;
            case I:
                keyword = "interface";
                break;
            case E:
                keyword = "enum";
                break;
            case A:
                keyword = "annotation";
                break;
            default:
                keyword = "class";
                break;
        }
        return new ImmutablePair<>(keyword, javaType);
    }

    public String getTypeId() {
        return getVertexId()
                .replace('.', '_')
                .replace('?', '_')
                .replace(',', '_')
                .replace("<", "_of_")
                .replace(">", "_fo_")
                .replaceAll("\\s+", "");
    }

    public String getTypeParameters() {
        if (typeDeclaration != null && typeDeclaration.isClassOrInterfaceDeclaration()) {
            NodeList<TypeParameter> foo = ((ClassOrInterfaceDeclaration) typeDeclaration).getTypeParameters();
            if (CollectionUtils.isNotEmpty(foo)) {
                return "<" + foo.stream().map(TypeParameter::toString).collect(Collectors.joining(",")) + ">";
            }
        }
        return JavaParserUtility.getTypeArguments(type);
    }

    public List<TypeField> getFields() {
        if (isFlag(FLAG_SELECTED)) {
            if (typeDeclaration == null) {
                return Collections.emptyList();
            }
            List<TypeField> fields = new ArrayList<>();
            typeDeclaration.getFields().forEach(field -> {
                List<Modifier.Keyword> modifiers = field.getModifiers().stream().map(Modifier::getKeyword).collect(Collectors.toList());
                field.getVariables().forEach(v -> fields.add(new TypeField(modifiers, v.getName(), v.getType())));
            });
            return fields.stream().sorted(Comparator.comparing(TypeField::getNameString)).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<TypeMethod> getMethods() {
        if (isFlag(FLAG_SELECTED)) {
            if (typeDeclaration == null) {
                return getCalledMethods();
            }
            List<TypeMethod> constructors = typeDeclaration.getConstructors().stream().map(TypeMethod::new).collect(Collectors.toList());
            List<TypeMethod> methods = typeDeclaration.getMethods().stream().map(TypeMethod::new).sorted(Comparator.comparing(TypeMethod::getNameString)).collect(Collectors.toList());
            constructors.addAll(methods);
            return constructors;
        } else {
            return getCalledMethods();
        }
    }

    public List<TypeMethod> getCalledMethods() {
        if (getSelectedMethods().isEmpty()) {
            return Collections.emptyList();
        }
        List<TypeMethod> methods = new ArrayList<>();
        for (String description : getSelectedMethods()) {
            if (typeDeclaration == null) {
                methods.add(new TypeMethod(JavaParserUtility.splitMethodDescription(description)));
            } else {
                MethodDeclaration declaration = JavaParserUtility.findMethodDeclaration(typeDeclaration, description);
                methods.add(declaration == null
                                    ? new TypeMethod(JavaParserUtility.splitMethodDescription(description))
                                    : new TypeMethod(declaration));
            }
        }
        return methods;
    }

}
