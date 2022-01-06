package io.github.codingspeedup.execdoc.toolbox.documents.docx;

import lombok.SneakyThrows;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DocxWrapper {

    public static final String TEXT_FONT = "Courier";
    public static final long TEXT_FONT_SIZE = 10L;
    public static final long MARGIN_SIZE = 100L;

    public static DocxDocument wrapText(String text) {
        String[] lines = text.split("\\R");
        DocxDocument wrapper = new DocxDocument();
        XWPFDocument docx = wrapper.getDocument();

        DescriptiveStatistics dStat = new DescriptiveStatistics();

        XWPFParagraph par = docx.createParagraph();
        par.setSpacingBetween(TEXT_FONT_SIZE, LineSpacingRule.EXACT);
        for (String line : lines) {
            XWPFRun run = createPlainTextRun(par);
            run.setText(line);
            run.addBreak();
            dStat.addValue(line.length());
        }

        CTBody body = docx.getDocument().getBody();
        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();
        if (!section.isSetPgSz()) {
            section.addNewPgSz();
        }

        int lineLength = (int) dStat.getPercentile(90);

        CTPageSz pageSize = section.getPgSz();
        pageSize.setW(BigInteger.valueOf((lineLength + 1) * TEXT_FONT_SIZE * 20 + MARGIN_SIZE * 2));

        CTPageMar pageMar = section.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(MARGIN_SIZE));
        pageMar.setTop(BigInteger.valueOf(MARGIN_SIZE));
        pageMar.setRight(BigInteger.valueOf(MARGIN_SIZE));
        pageMar.setBottom(BigInteger.valueOf(MARGIN_SIZE));

        return wrapper;
    }

    private static XWPFRun createPlainTextRun(XWPFParagraph par) {
        XWPFRun run = par.createRun();
        run.setFontFamily(TEXT_FONT);
        run.setFontSize(TEXT_FONT_SIZE);
        return run;
    }

    @SneakyThrows
    public static DocxDocument wrapImage(String extension, byte[] data) {
        int pictureType = -1;
        Dimension dim = null;
        switch (extension) {
            case "EPS":
                pictureType = Document.PICTURE_TYPE_EPS;
                dim = getImageDim(extension, data);
                break;
            case "JPG":
            case "JPEG":
                pictureType = Document.PICTURE_TYPE_JPEG;
                dim = getImageDim(extension, data);
                break;
            case "PNG":
                pictureType = Document.PICTURE_TYPE_PNG;
                dim = getImageDim(extension, data);
                break;

        }
        if (dim == null) {
            return null;
        }

        DocxDocument wrapper = new DocxDocument();
        XWPFDocument docx = wrapper.getDocument();
        CTBody body = docx.getDocument().getBody();
        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();
        if (!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        CTPageSz pageSize = section.getPgSz();
        pageSize.setW(BigDecimal.valueOf(20 * Units.toPoints(Units.pixelToEMU(dim.width))));
        pageSize.setH(BigDecimal.valueOf(20 * Units.toPoints(Units.pixelToEMU(dim.height))));
        CTPageMar pageMar = section.addNewPgMar();
        pageMar.setLeft(BigDecimal.ZERO);
        pageMar.setTop(BigDecimal.ZERO);
        pageMar.setRight(BigDecimal.ZERO);
        pageMar.setBottom(BigDecimal.ZERO);

        XWPFRun run = wrapper.createRun(wrapper.createParagraph());
        run.addPicture(new ByteArrayInputStream(data), pictureType, "", Units.pixelToEMU(dim.width), Units.pixelToEMU(dim.height));
        return wrapper;
    }

    private static Dimension getImageDim(String extension, byte[] data) throws IOException {
        if ("eps".equalsIgnoreCase(extension)) {
            String epsSrc = new String(data);
            int bbStart = epsSrc.indexOf("%%BoundingBox");
            if (bbStart >= 0) {
                int bbEnd = epsSrc.indexOf("\n", bbStart + 5);
                String bb = epsSrc.substring(bbStart, bbEnd).trim();
                String[] bbValues = bb.split(" ");
                return new Dimension(Integer.parseInt(bbValues[bbValues.length - 2]), Integer.parseInt(bbValues[bbValues.length - 1]));
            }
            return new Dimension(320, 240);
        }
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
        return new Dimension(image.getWidth(), image.getHeight());
    }

}
