package io.github.codingspeedup.execdoc.reporters.folderdiff;

import io.github.codingspeedup.execdoc.toolbox.documents.docx.DocxDocument;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class FolderDiffReport extends DocxDocument {

    public static final String H1 = "Heading1";
    public static final String H2 = "Heading2";
    public static final String H3 = "Heading3";

    public static final String ADDED_MARKER = "\u2727";
    public static final String DELETED_MARKER = "\u2718";
    public static final String MODIFIED_MARKER = "\u229B";
    public static final String FOLDER_MARKER = "\u25B1";

    public static final String ROOT_FOLDER_HEADING = "/";

    public static final int PATH_FONT_SIZE = 10;
    public static final String PATH_SEP = "\u27A5";
    public static final String PATH_BACK_SEP = "\u2923";
    public static final String PATH_COLOR = "666666";
    public static final String PATH_SEP_COLOR = "BB0000";

    public static final String TEXT_FONT = "Courier";
    public static final long TEXT_FONT_SIZE = 7L;
    public static final String TEXT_CHANGED_COLOR = "0000FF";
    public static final String TEXT_DELETED_COLOR = "DD0000";
    public static final String TEXT_INSERTED_COLOR = "007700";
    public static final String TEXT_UNCHANGED_COLOR = "BBBBBB";

    public FolderDiffReport() {
        super("/templates/folder-diff-report-a4.docx");
    }

    public XWPFRun deletedRun(XWPFParagraph par) {
        XWPFRun run = createPlainTextRun(par);
        run.setColor(TEXT_DELETED_COLOR);
        run.setStrikeThrough(true);
        return run;
    }

    public XWPFRun createPlainTextRun(XWPFParagraph par) {
        XWPFRun run = par.createRun();
        run.setFontFamily(FolderDiffReport.TEXT_FONT);
        run.setFontSize(FolderDiffReport.TEXT_FONT_SIZE);
        return run;
    }

    public XWPFRun unchangedRun(XWPFParagraph par) {
        XWPFRun run = createPlainTextRun(par);
        run.setColor(FolderDiffReport.TEXT_UNCHANGED_COLOR);
        return run;
    }

    public XWPFRun changedRun(XWPFParagraph par) {
        XWPFRun run = createPlainTextRun(par);
        run.setColor(FolderDiffReport.TEXT_CHANGED_COLOR);
        return run;
    }

    public XWPFRun insertedRun(XWPFParagraph par) {
        XWPFRun run = createPlainTextRun(par);
        run.setColor(FolderDiffReport.TEXT_INSERTED_COLOR);
        return run;
    }

    public XWPFParagraph createPlainTextParagraph() {
        XWPFParagraph par = createParagraph();
        par.setSpacingBetween(FolderDiffReport.TEXT_FONT_SIZE, LineSpacingRule.EXACT);
        return par;
    }

}
