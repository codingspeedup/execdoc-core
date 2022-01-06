package io.github.codingspeedup.execdoc.reporters.codexray;

import io.github.codingspeedup.execdoc.reporters.codexray.calldiagram.CallDiagram;
import io.github.codingspeedup.execdoc.reporters.codexray.calldiagram.CallVertex;
import io.github.codingspeedup.execdoc.reporters.codexray.classdiagram.ClassDiagram;
import io.github.codingspeedup.execdoc.reporters.codexray.classdiagram.ClassVertex;
import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaMethodResource;
import io.github.codingspeedup.execdoc.toolbox.staticsite.StaticSite;
import io.github.codingspeedup.execdoc.toolbox.utilities.PlantUmlUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.StringUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class XRay extends StaticSite {

    private final List<XRaySection> sections = new ArrayList<>();

    public XRay(File folder) {
        super(folder);
        String style = "";
        style += "body { padding: 12px; }\n";
        style += "svg a:link { text-decoration: none; }\n";
        style += "svg a:visited { text-decoration: none; }\n";
        style += "svg a:hover { text-decoration: none; }\n";
        style += "svg a:active { text-decoration: none; }\n";
        style += ".diagrams { display: flex; flex-flow: row wrap; justify-content: flex-start; align-items: center;}\n";
        style += ".diagram { display: inline-block; margin: 15px 0px 25px 40px}\n";
        setIndexXhtmlStyle(style);
        setIndexXhtmlTitle("XRay: " + getFile().getName());
    }

    private static String renderPlantUmlSvgDiagram(String script) {
        return "\n<div class=\"diagram\">" + PlantUmlUtility.asSvgXhtml(script) + "</div>\n";
    }

    @Override
    protected String getIndexXhtmlContent() {
        StringBuilder tocXhtml = new StringBuilder();
        tocXhtml.append("<h1>").append(getFile().getName().toUpperCase(Locale.ROOT)).append("</h1>\n");
        StringBuilder contentsXhtml = new StringBuilder();
        sections.forEach(section -> {
            tocXhtml.append(section.getTocEntry()).append("<br/>\n");
            contentsXhtml.append(section.getContent()).append("\n");
        });
        return tocXhtml + "\n" + contentsXhtml;
    }

    public XRaySection addSection(String heading, int level) {
        XRaySection section = new XRaySection(heading, level);
        sections.add(section);
        return section;
    }

    public void addClassDiagramsSection(ClassDiagram diagram) {
        diagram.vertexSet().forEach(this::fillClassVertex);
        XRaySection section = addSection("Selected Classes Diagrams", 2);
        section.getContent().append("<div class=\"diagrams\">\n");
        diagram.connectedSets().forEach(cc -> section.getContent().append(renderPlantUmlSvgDiagram(diagram.toPlantUmlScript(cc))));
        section.getContent().append("</div>\n");
    }

    public void addMethodsSection() {
        addSection("Selected Methods Analysis", 2);
    }

    public void addMethodAnalysis(JavaMethodResource resource, ClassDiagram classDiagram, CallDiagram callDiagram) {
        classDiagram.vertexSet().forEach(this::fillClassVertex);

        XRaySection section = addSection(resource.getParent().getDescription() + "." + resource.getDescription(), 3);

        List<XRaySection> subSections = new ArrayList<>();
        for (CallVertex v : callDiagram.vertexSet()) {
            if (v.getMethodDeclaration() != null) {
                XRaySection subSection = addSection(v.getTypeSimpleName() + "." + v.getMethodName() + "(" + v.getMethodSignature() + ")", 4);
                v.setUrl("#" + subSection.getAnchor());
                subSection.getContent().append(renderCodeSnipper("java", v.getMethodDeclaration().toString(JavaDocument.DEFAULT_PRINTER_CONFIGURATION))).append("\n");
                subSections.add(subSection);
            }
        }

        section.getContent().append("<div class=\"diagrams\">\n");
        classDiagram.connectedSets().forEach(cc -> section.getContent().append(renderPlantUmlSvgDiagram(classDiagram.toPlantUmlScript(cc))));
        section.getContent().append(renderPlantUmlSvgDiagram(callDiagram.toPlantUmlScript()));
        section.getContent().append("</div>\n");
    }

    private void fillClassVertex(ClassVertex v) {
        if (v.getJavaDocument() != null) {
            String embeddedName = v.getJavaDocument().getPackageName() + "." + v.getJavaDocument().getFile().getName();
            v.setUrl(embedAsXhtml(v.getJavaDocument().getFile(), embeddedName));
            v.setUrlTooltip("Inspect " + v.getJavaDocument().getFile().getName());
        } else if (v.isFlag(ClassVertex.FLAG_EXTERNAL)) {
            v.setUrl("https://www.google.com/search?q=" + StringUtility.urlEncode(v.getSimpleName()));
            v.setUrlTooltip("Search for " + v.getSimpleName());
        }
    }

}
