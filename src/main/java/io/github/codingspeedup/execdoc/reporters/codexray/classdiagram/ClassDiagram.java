package io.github.codingspeedup.execdoc.reporters.codexray.classdiagram;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import io.github.codingspeedup.execdoc.reporters.codexray.Diagram;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassDiagram extends Diagram<ClassVertex, ClassEdge> {

    public static final String STEREOTYPE_EXTERNAL = "external";
    public static final String COLOR_SELECTED_EXTERNAL = "WhiteSmoke";
    public static final String STEREOTYPE_SELECTED = "selected";
    public static final String COLOR_SELECTED_BACKGROUND = "Moccasin";
    public static final String STEREOTYPE_UNKNOWN_TYPE = "?";
    public static final String COLOR_UNKNOWN_TYPE = "White";

    public ClassDiagram() {
        super(new DirectedPseudograph<>(ClassEdge.class));
    }

    public static String normalizeMethodDeclaration(MethodDeclaration methodDeclaration) {
        String declaration = methodDeclaration.getDeclarationAsString(false, false, false);
        int splitPos = declaration.indexOf("(");
        splitPos = declaration.lastIndexOf(" ", splitPos);
        return StringUtils.trim(declaration.substring(splitPos)) + " : " + StringUtils.trim(declaration.substring(0, splitPos));
    }

    private static String getVisibilityString(Modifier.Keyword visibility) {
        if (visibility == null) {
            return " ";
        }
        switch (visibility) {
            case PUBLIC:
                return "+";
            case PROTECTED:
                return "#";
            case PRIVATE:
                return "-";
            case DEFAULT:
                return "~";
            default:
                return " ";
        }
    }

    private void render(StringBuilder uml, ClassVertex v) {
        if (StringUtils.isNotBlank(v.getPackageName())) {
            uml.append("package \"").append(v.getPackageName()).append("\" {\n");
        }
        uml.append("  ");
        ImmutablePair<String, JavaType> keywordType = v.getTypeDeclarationKeyword();
        uml.append(keywordType.getLeft());
        uml.append(" ");
        uml.append(v.getTypeId());
        uml.append(" as \"");
        uml.append(v.getSimpleName());
        uml.append(v.getTypeParameters());
        uml.append("\"");

        List<String> stereotypes = new ArrayList<>();
        if (keywordType.getRight() == JavaType.X) {
            stereotypes.add("(" + STEREOTYPE_UNKNOWN_TYPE + "," + COLOR_UNKNOWN_TYPE + ")");
        }
        if (v.isFlag(ClassVertex.FLAG_SELECTED)) {
            stereotypes.add(STEREOTYPE_SELECTED);
        }
        if (v.isFlag(ClassVertex.FLAG_EXTERNAL)) {
            stereotypes.add(STEREOTYPE_EXTERNAL);
        }
        if (CollectionUtils.isNotEmpty(stereotypes)) {
            uml.append("<< ").append(String.join(" ", stereotypes)).append(">>");
        }

        if (StringUtils.isNotBlank(v.getUrl())) {
            uml.append(" [[");
            uml.append(v.getUrl());
            if (v.getUrlTooltip() != null) {
                uml.append("{").append(v.getUrlTooltip()).append("}");
            }
            uml.append("]]");
        }
        uml.append(" {\n");
        v.getFields().forEach(field -> {
            uml.append("    ");
            uml.append(getVisibilityString(field.getVisibility()));
            if (field.isStaticFlag()) {
                uml.append(" {static}");
            }
            uml.append(" ");
            uml.append(field.getNameString());
            String typeString = field.getTypeString();
            if (StringUtils.isNotBlank(typeString)) {
                uml.append(" : ");
                uml.append(typeString);
            }
            uml.append("\n");
        });
        v.getMethods().forEach(method -> {
            uml.append("    ");
            uml.append(getVisibilityString(method.getVisibility()));
            if (method.isStaticFlag()) {
                uml.append(" {static}");
            }
            if (method.isAbstractFlag()) {
                uml.append(" {abstract}");
            }
            uml.append(" ");
            uml.append(method.getNameString());
            uml.append(" (");
            uml.append(method.getArgumentsString());
            uml.append(")");
            String typeString = method.getTypeString();
            if (StringUtils.isNotBlank(typeString)) {
                uml.append(" : ");
                uml.append(typeString);
            }
            uml.append("\n");
        });
        uml.append("  }\n");
        if (StringUtils.isNotBlank(v.getPackageName())) {
            uml.append("}\n");
        }
    }

    private void render(StringBuilder uml, ClassEdge e) {
        ClassVertex s = this.getCoreGraph().getEdgeSource(e);
        ClassVertex t = this.getCoreGraph().getEdgeTarget(e);
        switch (e.getRelation()) {
            case EXTENDS:
                uml.append(t.getTypeId()).append(" <|-- ").append(s.getTypeId()).append("\n");
                break;
            case IMPLEMENTS:
                uml.append(t.getTypeId()).append(" <|.. ").append(s.getTypeId()).append("\n");
                break;
            case HAS_A:
                uml.append(s.getTypeId()).append(" o-[bold]- ").append(t.getTypeId()).append("\n");
                break;
        }
    }

    @Override
    public String toPlantUmlScript(Set<ClassVertex> vertexSet) {
        StringBuilder uml = new StringBuilder();
        uml.append("@startuml\n");
        uml.append("skinparam groupInheritance 3\n");
        // uml.append("left to right direction\n");
        // uml.append("skinparam linetype ortho\n");
        uml.append("skinparam class {\n");
        uml.append("BackgroundColor<<").append(STEREOTYPE_EXTERNAL).append(">> ").append(COLOR_SELECTED_EXTERNAL).append("\n");
        uml.append("BackgroundColor<<").append(STEREOTYPE_SELECTED).append(">> ").append(COLOR_SELECTED_BACKGROUND).append("\n");
        uml.append("}\n");
        vertexSet.forEach(v -> render(uml, v));
        vertexSet.forEach(v -> this.getCoreGraph().outgoingEdgesOf(v).stream()
                .filter(e -> vertexSet.contains(this.getCoreGraph().getEdgeTarget(e)))
                .forEach(e -> render(uml, e)));
        uml.append("\n@enduml\n");
        return uml.toString();
    }

}
