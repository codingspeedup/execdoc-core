package io.github.codingspeedup.execdoc.toolbox.documents.docx;

import io.github.codingspeedup.execdoc.toolbox.documents.BinaryFileWrapper;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;

import java.io.*;

@NoArgsConstructor
public class DocxDocument extends BinaryFileWrapper {

    private XWPFDocument document;

    public DocxDocument(File file) {
        super(file);
    }

    @SneakyThrows
    protected DocxDocument(String template) {
        try (InputStream inp = getClass().getResourceAsStream(template)) {
            if (inp == null) {
                throw new UnsupportedOperationException("Template not found: " + template);
            }
            document = new XWPFDocument(inp);
        }
    }

    @SneakyThrows
    public XWPFDocument getDocument() {
        if (document == null) {
            document = new XWPFDocument();
        }
        return document;
    }

    @SneakyThrows
    @Override
    protected void loadFromWrappedFile() {
        try (InputStream inp = new FileInputStream(getFile())) {
            document = new XWPFDocument(inp);
        }
    }

    @SneakyThrows
    @Override
    protected void saveToWrappedFile() {
        try (OutputStream fileOut = new FileOutputStream(getFile())) {
            getDocument().write(fileOut);
            getDocument().close();
        }
        loadFromWrappedFile();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        try (ByteArrayOutputStream sout = new ByteArrayOutputStream()) {
            getDocument().write(sout);
            return sout.toByteArray();
        }
    }

    public XWPFParagraph createParagraph(Object... args) {
        XWPFParagraph paragraph = getDocument().createParagraph();
        if (args != null) {
            Object arg0 = null;
            if (args.length > 0) {
                arg0 = args[0];
            }
            if (arg0 instanceof String) {
                paragraph.setStyle((String) arg0);
            }
        }
        return paragraph;
    }

    public XWPFRun createRun(XWPFParagraph paragraph) {
        return paragraph.createRun();
    }

    public XWPFHyperlinkRun createHyperlinkRun(XWPFParagraph paragraph, String uri) {
        String rId = paragraph.getPart().getPackagePart().addExternalRelationship(uri, XWPFRelation.HYPERLINK.getRelation()).getId();
        CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
        cthyperLink.setId(rId);
        cthyperLink.addNewR();
        return new XWPFHyperlinkRun(cthyperLink, cthyperLink.getRArray(0), paragraph);
    }

    public void insertTableOfContents() {
        XWPFParagraph tocPar = createParagraph();
        CTP ctP = tocPar.getCTP();
        CTSimpleField toc = ctP.addNewFldSimple();
        toc.setInstr("TOC \\h");
        toc.setDirty(STOnOff1.ON);
    }

}
