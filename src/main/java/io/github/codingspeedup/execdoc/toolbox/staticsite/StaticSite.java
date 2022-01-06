package io.github.codingspeedup.execdoc.toolbox.staticsite;

import io.github.codingspeedup.execdoc.toolbox.documents.FolderWrapper;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.utilities.ThymeleafUtility;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class StaticSite extends FolderWrapper {

    public static final String EMBEDDINGS = "embeddings";
    private static final String CORE_ROOT = "/sxhtml/core";
    private static final List<String> CORE_RESOURCES = new ArrayList<>();
    private static final TemplateEngine THYMELEAF;

    static {
        THYMELEAF = ThymeleafUtility.xhtmlTemplateEngine();
        CORE_RESOURCES.add(CORE_ROOT + "/bootstrap/bootstrap.min.css");
        CORE_RESOURCES.add(CORE_ROOT + "/bootstrap/bootstrap.bundle.min.js");
        CORE_RESOURCES.add(CORE_ROOT + "/highlight/highlight.min.js");
        CORE_RESOURCES.add(CORE_ROOT + "/highlight/vs.min.css");
    }

    @Getter
    @Setter
    private String indexXhtmlTitle;

    @Getter
    @Setter
    private String indexXhtmlStyle;

    public StaticSite(File folder) {
        super(folder);
    }

    public static String renderCodeSnipper(String language, String snippet) {
        return "<pre><code class=\"language-" + language + "\"><![CDATA[" + snippet + "]]></code></pre>";
    }

    public File getIndexHtml() {
        return new File(getFile(), "_index.xhtml");
    }

    public Folder getEmbeddingsFolder() {
        return new Folder(new File(getFile(), EMBEDDINGS));
    }

    @SneakyThrows
    protected void saveToWrappedFile() {
        Folder coreFolder = Folder.of(new File(getFile(), "core"));
        for (String resource : CORE_RESOURCES) {
            File coreResource = new File(coreFolder, resource.substring(CORE_ROOT.length()));
            if (!coreResource.exists()) {
                Folder.of(coreResource.getParentFile());
                try (InputStream inp = getClass().getResourceAsStream(resource)) {
                    if (inp == null) {
                        throw new UnsupportedOperationException("Resource not found: " + resource);
                    }
                    FileUtils.copyInputStreamToFile(inp, coreResource);
                }
            }
        }
        Context ct = new Context();
        ct.setVariable("title", getIndexXhtmlTitle());
        String style = StringUtils.trimToEmpty(indexXhtmlStyle);
        if (StringUtils.isNotBlank(style)) {
            style = "\n" + style + "\n";
        }
        ct.setVariable("style", style);
        ct.setVariable("content", getIndexXhtmlContent());
        String xhtml = THYMELEAF.process("index", ct);
        FileUtils.writeStringToFile(getIndexHtml(), xhtml, StandardCharsets.UTF_8);
    }

    protected abstract String getIndexXhtmlContent();

    @SneakyThrows
    public String embed(File file, String embeddedName) {
        if (StringUtils.isBlank(embeddedName)) {
            embeddedName = file.getName();
        }
        File embeddedFile = new File(getEmbeddingsFolder(), embeddedName);
        FileUtils.copyFile(file, embeddedFile);
        return EMBEDDINGS + "/" + embeddedFile.getName();
    }

    @SneakyThrows
    public String embedAsXhtml(File file, String embeddedName) {
        if (StringUtils.isBlank(embeddedName)) {
            embeddedName = file.getName();
        }
        String extension = FilenameUtils.getExtension(embeddedName);
        if ("java".equalsIgnoreCase(extension)) {
            String content = renderCodeSnipper("java", FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            Context ct = new Context();
            ct.setVariable("title", file.getName());
            ct.setVariable("content", content);
            String xhtml = THYMELEAF.process("source", ct);
            embeddedName = FilenameUtils.getBaseName(embeddedName) + ".xhtml";
            File embeddedFile = new File(getEmbeddingsFolder(), embeddedName);
            FileUtils.writeStringToFile(embeddedFile, xhtml, StandardCharsets.UTF_8);
        } else {
            return embed(file, embeddedName);
        }
        return EMBEDDINGS + "/" + embeddedName;
    }

}
