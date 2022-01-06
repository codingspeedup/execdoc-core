package io.github.codingspeedup.execdoc.reporters.folderdiff;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffContainer;
import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffEntry;
import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffContainer;
import io.github.codingspeedup.execdoc.miners.diff.xlsx.XlsxDiffMiner;
import io.github.codingspeedup.execdoc.reporters.xlsxdiff.XlsxDiffReporter;
import io.github.codingspeedup.execdoc.toolbox.documents.docx.DocxUtil;
import io.github.codingspeedup.execdoc.toolbox.documents.docx.OleData;
import io.github.codingspeedup.execdoc.toolbox.utilities.FileUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.MimeUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.StringUtility;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import java.io.IOException;
import java.util.List;

public class FolderDiffReporter {

    private final FolderDiffReportPreferences prefs;

    public FolderDiffReporter() {
        this.prefs = new FolderDiffReportPreferences();
    }

    public FolderDiffReporter(FolderDiffReportPreferences prefs) {
        this.prefs = prefs;
    }

    public FolderDiffReport buildReport(FolderDiffContainer diffs) {
        FolderDiffReport report = new FolderDiffReport();
        report.insertTableOfContents();
        renderCoordinates(report);
        renderNotes(report);
        if (!diffs.getDiffs().isEmpty()) {
            diffs.getDiffs().sort(new ForlderDiffComparator());
            renderDifferences(report, diffs);
        }
        return report;
    }

    private void renderCoordinates(FolderDiffReport report) {
        XWPFRun run = report.createRun(report.createParagraph(FolderDiffReport.H1));
        run.addBreak(BreakType.PAGE);
        run.setText("Locations");
    }

    private void renderNotes(FolderDiffReport report) {
        XWPFRun run = report.createRun(report.createParagraph(FolderDiffReport.H1));
        run.setText("Notes");
    }

    private void renderDifferences(FolderDiffReport report, FolderDiffContainer diffs) {
        XWPFRun run = report.createRun(report.createParagraph(FolderDiffReport.H1));
        run.addBreak(BreakType.PAGE);
        run.setText("Differences");
        String currentFolder = null;
        for (FolderDiffEntry diff : diffs.getDiffs()) {
            if (diff.isRootFile()) {
                if (!FolderDiffReport.ROOT_FOLDER_HEADING.equals(currentFolder)) {
                    currentFolder = FolderDiffReport.ROOT_FOLDER_HEADING;
                    renderFolderHeading(report, currentFolder);
                }
            } else {
                if (!diff.getPath()[0].equals(currentFolder)) {
                    currentFolder = diff.getPath()[0];
                    renderFolderHeading(report, currentFolder);
                }
            }
            renderDiff(report, diff);
        }
    }

    private void renderFolderHeading(FolderDiffReport report, String currentFolder) {
        report.createParagraph();
        XWPFRun run = report.createRun(report.createParagraph(FolderDiffReport.H2));
        run.setText(currentFolder);
    }

    @SneakyThrows
    private void renderDiff(FolderDiffReport report, FolderDiffEntry diff) {
        report.createParagraph();
        XWPFParagraph par = report.createParagraph(FolderDiffReport.H3);
        XWPFRun run = report.createRun(par);
        if (diff.isRightOnly()) {
            run.setText(FolderDiffReport.ADDED_MARKER);
        } else if (diff.isLeftOnly()) {
            run.setText(FolderDiffReport.DELETED_MARKER);
        } else {
            run.setText(FolderDiffReport.MODIFIED_MARKER);
        }
        run.setText(" ");
        if (diff.isLeftOnly()) {
            run = report.createHyperlinkRun(par, diff.getLeft().toURI().toString());
        } else {
            run = report.createHyperlinkRun(par, diff.getRight().toURI().toString());
        }
        run.setBold(true);
        run.setText(diff.getName());
        if (diff.isLeftOnly()) {
            run.setStrikeThrough(true);
        }
        if (diff.isFolder()) {
            run = report.createRun(par);
            run.setColor(FolderDiffReport.PATH_SEP_COLOR);
            run.setText("  ");
            run.setText(FolderDiffReport.FOLDER_MARKER);
        }

        par = report.createParagraph();
        for (int i = 0; i < diff.getPath().length - 1; ++i) {
            if (i > 0) {
                run = report.createRun(par);
                run.setFontSize(FolderDiffReport.PATH_FONT_SIZE - 2);
                run.setColor(FolderDiffReport.PATH_SEP_COLOR);
                run.setText(" ");
                run.setText(FolderDiffReport.PATH_SEP);
                run.setText(" ");
            }
            if (i == diff.getPath().length - 2) {
                if (diff.isLeftOnly()) {
                    run = report.createHyperlinkRun(par, diff.getLeft().getParentFile().toURI().toString());
                } else {
                    run = report.createHyperlinkRun(par, diff.getRight().getParentFile().toURI().toString());
                }
            } else {
                run = report.createRun(par);
            }
            run.setFontSize(FolderDiffReport.PATH_FONT_SIZE);
            run.setColor(FolderDiffReport.PATH_COLOR);
            run.setItalic(true);
            run.setText(diff.getPath()[i]);
        }
        run = report.createRun(par);
        run.setFontSize(FolderDiffReport.PATH_FONT_SIZE);
        run.setColor(FolderDiffReport.PATH_SEP_COLOR);
        run.setText(" ");
        run.setText(FolderDiffReport.PATH_BACK_SEP);

        par = report.createParagraph();
        par.setAlignment(ParagraphAlignment.END);
        run = report.createRun(par);
        run.setFontSize(8);

        if (diff.isFile() && diff.isRight()) {
            if (prefs.isEmbedResources()) {
                OleData oleData = OleData.from(diff.getRight());
                if (oleData.isEmbeddable()) {
                    DocxUtil.embed(report.getDocument(), run, oleData);
                }
            }
            String mimeType = MimeUtility.guessMimeType(diff.getRight());
            if (MimeUtility.isText(mimeType)) {
                renderTextFileDiff(report, diff);
            } else if (diff.isBoth() && MimeUtility.isXlsx(mimeType)) {
                renderXlsxFileDiff(report, diff);
            }
        }
    }

    private void renderTextFileDiff(FolderDiffReport report, FolderDiffEntry diff) throws IOException {
        XWPFParagraph par;
        XWPFRun run;
        List<String> rightLines = FileUtility.readLines(diff.getRight());
        FolderDiffUtility.cleanupLines(rightLines);
        if (diff.isRightOnly()) {
            if (!prefs.isEmbedResources()) {
                par = report.createPlainTextParagraph();
                for (String line : rightLines) {
                    run = report.insertedRun(par);
                    run.setText(line);
                    run.addCarriageReturn();
                }
            }
        } else {
            List<String> leftLines = FileUtility.readLines(diff.getLeft());
            int leftIndex = FolderDiffUtility.cleanupLines(leftLines);

            DiffRowGenerator generator = DiffRowGenerator.create()
                    .showInlineDiffs(true)
                    .inlineDiffByWord(true)
                    .lineNormalizer(s -> s)
                    .oldTag(f -> "")
                    .newTag(f -> "")
                    .build();

            List<DiffRow> rows = generator.generateDiffRows(leftLines, rightLines);

            par = report.createPlainTextParagraph();
            int equalLines = 0;
            for (DiffRow row : rows) {
                if (row.getTag() != DiffRow.Tag.INSERT) {
                    ++leftIndex;
                }
                if (equalLines > 10 && row.getTag() != DiffRow.Tag.EQUAL) {
                    run = report.createPlainTextRun(par);
                    CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
                    cTShd.setVal(STShd.CLEAR);
                    cTShd.setColor("auto");
                    cTShd.setFill("FFFF66");
                    run.addCarriageReturn();
                    run.setText("\u3010 " + (leftIndex + 1) + " \u27A4\u202F");
                    run.addCarriageReturn();
                    run.addCarriageReturn();
                }
                switch (row.getTag()) {
                    case EQUAL:
                        ++equalLines;
                        run = report.unchangedRun(par);
                        run.setText(row.getOldLine());
                        run.addCarriageReturn();
                        break;
                    case DELETE:
                        equalLines = 0;
                        run = report.deletedRun(par);
                        run.setText(row.getOldLine());
                        run.addCarriageReturn();
                        break;
                    case INSERT:
                        equalLines = 0;
                        run = report.insertedRun(par);
                        run.setText(row.getNewLine());
                        run.addCarriageReturn();
                        break;
                    default:
                        equalLines = 0;
                        run = report.deletedRun(par);
                        run.setText(row.getOldLine());
                        run.addCarriageReturn();
                        run = report.insertedRun(par);
                        run.setText(row.getNewLine());
                        run.addCarriageReturn();
                }
            }
        }
    }

    private void renderXlsxFileDiff(FolderDiffReport report, FolderDiffEntry diff) {
        XlsxDiffMiner miner = new XlsxDiffMiner();
        XlsxDiffContainer diffs = miner.compare(diff.getLeft(), diff.getRight());
        XlsxDiffReporter reporter = new XlsxDiffReporter();
        String[] reportLines = StringUtility.splitLines(reporter.getReport(diffs));

        XWPFParagraph par = report.createPlainTextParagraph();
        for (String line : reportLines) {
            char op = StringUtils.stripStart(line, null).charAt(0);
            XWPFRun run;
            switch (op) {
                case '+':
                    run = report.insertedRun(par);
                    break;
                case '-':
                    run = report.deletedRun(par);
                    break;
                case '*':
                    run = report.changedRun(par);
                    break;
                default:
                    run = report.unchangedRun(par);
            }
            run.setText(line);
            run.addCarriageReturn();
        }
    }

}
