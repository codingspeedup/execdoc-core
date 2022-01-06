package io.github.codingspeedup.execdoc.toolbox.documents.xml;

import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.StringWriter;

/**
 * https://dom4j.github.io/
 */
@NoArgsConstructor
public class XmlDocument extends TextFileWrapper {

    @Getter
    @Setter
    private OutputFormat format = OutputFormat.createPrettyPrint();
    private Document document;

    public XmlDocument(File file) {
        super(file);
    }

    @SneakyThrows
    @Override
    protected void loadFromWrappedFile() {
        SAXReader reader = new SAXReader();
        document = reader.read(getFile());
    }

    @SneakyThrows
    public Document getDocument() {
        if (document == null) {
            document = DocumentHelper.createDocument();
        }
        return document;
    }

    @SneakyThrows
    @Override
    public String toString() {
        if (document == null) {
            return null;
        }
        try (StringWriter w = new StringWriter()) {
            XMLWriter writer = new XMLWriter(w, format);
            writer.write(document);
            return w.toString();
        }
    }

}
