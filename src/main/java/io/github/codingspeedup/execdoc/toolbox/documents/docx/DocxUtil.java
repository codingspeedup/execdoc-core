package io.github.codingspeedup.execdoc.toolbox.documents.docx;

import com.microsoft.schemas.office.office.*;
import com.microsoft.schemas.vml.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTrueFalse;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTrueFalseBlank;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.UUID;

/**
 * https://user.poi.apache.narkive.com/auiQi7tm/embed-files-into-word
 * https://pastebin.com/wsGqBDi0
 * https://www.docx4java.org/forums/docx-java-f6/how-to-create-an-oleobject-bin-for-video-or-pdf-t72.html
 * https://stackoverflow.com/questions/62398450/apache-poi-library-how-to-read-excel-sheet-embedded-in-word-document
 * <p>
 * "application/vnd.openxmlformats-officedocument.oleObject"
 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
 * "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
 */
public class DocxUtil {

    @SneakyThrows
    public static OleData embed(XWPFDocument document, XWPFRun run, OleData oleData) {
        if (StringUtils.isBlank(oleData.getDataRel())) {
            PackagePartName ppName = createPackagePartName(document.getPackage());
            document.getPackage().createPart(ppName, oleData.getMimeType(), oleData.getDataStream());
            oleData.setDataRel(document.getPackagePart().addRelationship(ppName, TargetMode.INTERNAL, POIXMLDocument.PACK_OBJECT_REL_TYPE).getId());
        }

        int iconSize = run.getFontSizeAsDouble() == null ? 12 : run.getFontSizeAsDouble().intValue();
        OleData.IconData iconData = OleData.getIconData(oleData.getExtension(), iconSize);
        if (iconData.getIcon() == null) {
            iconData.setIcon(generateIcon(oleData.getExtension(), oleData.getColor(), iconSize));
            ByteArrayOutputStream iconStream = new ByteArrayOutputStream();
            ImageIO.write(iconData.getIcon(), "png", iconStream);
            iconData.setIconRel(document.addPictureData(iconStream.toByteArray(), Document.PICTURE_TYPE_PNG));
        }

        String idSuffix = UUID.randomUUID().toString().replace("-", "");
        String typeId = "_x0000_t_" + idSuffix;
        String shapeId = "_x0000_i_" + idSuffix;

        CTR ctr = run.getCTR();

        CTObject obj = ctr.addNewObject();
        // obj.setDxaOrig(BigInteger.valueOf(iconData.getIcon().getWidth() * 20));
        // obj.setDyaOrig(BigInteger.valueOf(iconData.getIcon().getHeight() * 20));

        CTGroup grp = CTGroup.Factory.newInstance();
        CTShapetype st = grp.addNewShapetype();
        st.setId(typeId);
        st.setCoordsize("21600,21600");
        st.setFilled(STTrueFalse.F);
        st.setPreferrelative(STTrueFalse.T);
        st.setPath2("m@4@5l@4@11@9@11@9@5xe");
        st.setStroked(STTrueFalse.F);
        st.setSpt(75);
        st.addNewStroke().setJoinstyle(STStrokeJoinStyle.ROUND);

        CTFormulas form = st.addNewFormulas();
        String[] clumsyRect = {
                "if lineDrawn pixelLineWidth 0",
                "sum @0 1 0",
                "sum 0 0 @1",
                "prod @2 1 2",
                "prod @3 21600 pixelWidth",
                "prod @3 21600 pixelHeight",
                "sum @0 0 1",
                "prod @6 1 2",
                "prod @7 21600 pixelWidth",
                "sum @8 21600 0",
                "prod @7 21600 pixelHeight",
                "sum @10 21600 0"
        };
        for (String cr : clumsyRect) {
            form.addNewF().setEqn(cr);
        }

        CTPath path = st.addNewPath();
        path.setGradientshapeok(STTrueFalse.T);
        path.setConnecttype(STConnectType.RECT);
        path.setExtrusionok(STTrueFalse.F);

        CTLock lock = st.addNewLock();
        lock.setAspectratio(STTrueFalse.T);
        lock.setExt(STExt.EDIT);

        CTShape shape = grp.addNewShape();
        shape.setId(shapeId);
        double shapeScaleFactor = 1;
        int shapeWidth = (int) (iconData.getIcon().getWidth() * shapeScaleFactor);
        int shapeHeight = (int) (iconData.getIcon().getHeight() * shapeScaleFactor);
        shape.setStyle(String.format("width:%dpt;height:%dpt", shapeWidth, shapeHeight));
        shape.setType("#" + typeId);
        shape.setOle(STTrueFalseBlank.X);

        CTImageData imgDat = shape.addNewImagedata();
        imgDat.setId2(iconData.getIconRel());
        imgDat.setTitle("");

        OLEObjectDocument oleParent = OLEObjectDocument.Factory.newInstance();
        CTOLEObject ole = oleParent.addNewOLEObject();
        ole.setDrawAspect(STOLEDrawAspect.ICON);
        ole.setObjectID("_" + idSuffix);
        ole.setProgID(oleData.getProgId());
        ole.setShapeID(shapeId);
        ole.setType(STOLEType.EMBED);
        ole.setId(oleData.getDataRel());

        XmlCursor objCur = obj.newCursor();
        objCur.toFirstContentToken();

        XmlCursor grpCur = grp.newCursor();
        grpCur.copyXmlContents(objCur);
        grpCur.dispose();

        XmlCursor oleCur = oleParent.newCursor();
        oleCur.copyXmlContents(objCur);
        oleCur.dispose();

        objCur.dispose();

        return oleData;
    }

    private static PackagePartName createPackagePartName(OPCPackage opcPackage) throws InvalidFormatException {
        for (int id = 1; id < Integer.MAX_VALUE; ++id) {
            PackagePartName ppName = PackagingURIHelper.createPartName(String.format("/word/embeddings/oleObject%d.bin", id));
            if (!opcPackage.containPart(ppName)) {
                return ppName;
            }
        }
        throw new UnsupportedOperationException("Could not create package part name");
    }

    private static BufferedImage generateIcon(String extension, Color bgColor, int iconSize) {
        String iconText = "." + extension.toLowerCase(Locale.ROOT);

        int hMargin = 3;
        int vMargin = 1;
        Font iconFont = new Font(Font.MONOSPACED, Font.BOLD, iconSize);

        BufferedImage icon = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = icon.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(iconFont);
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getHeight() + vMargin;
        int width = hMargin + fm.stringWidth("M" + iconText) + hMargin;

        Color fgColor = Color.WHITE;

        icon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = icon.createGraphics();

        g.setBackground(bgColor);
        g.clearRect(0, 0, width, height);

        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, iconSize + 6));
        g.setColor(fgColor);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawString("\u279A", hMargin - 1, iconFont.getSize() + 2);
        g.setFont(iconFont);
        g.drawString(iconText, hMargin + fm.stringWidth("M"), iconFont.getSize());

        return icon;
    }

}
