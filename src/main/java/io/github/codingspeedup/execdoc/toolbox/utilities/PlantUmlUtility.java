package io.github.codingspeedup.execdoc.toolbox.utilities;

import lombok.SneakyThrows;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class PlantUmlUtility {

    @SneakyThrows
    public static byte[] asSvgBytes(String diagram) {
        System.out.println(diagram);
        FileFormatOption ff = new FileFormatOption(FileFormat.SVG);
        SourceStringReader reader = new SourceStringReader(diagram);
        ByteArrayOutputStream svgBytes = new ByteArrayOutputStream();
        DiagramDescription description = reader.outputImage(svgBytes, ff);
        System.out.println(description);
        svgBytes.close();
        return svgBytes.toByteArray();
    }

    public static String asSvgXml(String diagram) {
        byte[] svgBytes = asSvgBytes(diagram);
        return new String(svgBytes, StandardCharsets.UTF_8);
    }

    public static String asSvgXhtml(String diagram) {
        String svgXml = asSvgXml(diagram);
        int cmtStart, cmtEnd;
        while ((cmtStart = svgXml.indexOf("<!--")) >= 0) {
            if ((cmtEnd = svgXml.indexOf("-->", cmtStart + 4)) >= 0) {
                svgXml = svgXml.substring(0, cmtStart) + svgXml.substring(cmtEnd + 3);
            }
        }
        svgXml = StringUtils.trimToEmpty(svgXml);
        if (svgXml.startsWith("<?xml")) {
            svgXml = StringUtils.trimToEmpty(svgXml.substring(svgXml.indexOf(">") + 1));
        }
        if (svgXml.startsWith("<!DOCTYPE")) {
            svgXml = StringUtils.trimToEmpty(svgXml.substring(svgXml.indexOf(">") + 1));
        }
        return svgXml;
    }

}
