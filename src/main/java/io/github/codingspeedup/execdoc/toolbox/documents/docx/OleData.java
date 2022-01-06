package io.github.codingspeedup.execdoc.toolbox.documents.docx;

import io.github.codingspeedup.execdoc.toolbox.utilities.MimeUtility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Setter
public class OleData {

    private static final Map<String, IconData> icons = new HashMap<>();
    public static Charset TEXT_CHARSET = StandardCharsets.UTF_8;
    @Getter
    private final String fileName;
    @Getter
    private final ByteArrayOutputStream dataStream;
    @Getter
    private final String extension;
    @Getter
    private final String mimeType;
    @Getter
    private final Color color;
    @Getter
    private final String progId;
    @Getter
    @Setter
    private String dataRel;

    private OleData(String fileName, String extension, byte[] data) throws IOException {
        this.fileName = fileName;
        this.extension = extension;
        dataStream = new ByteArrayOutputStream();
        dataStream.write(data);
        switch (FilenameUtils.getExtension(this.fileName).toUpperCase(Locale.ROOT)) {
            case "DOCX":
                mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                progId = "Word.Document.12";
                color = new Color(0x18, 0x5B, 0xBD);
                break;
            case "XLSX":
                mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                progId = "Excel.Sheet.12";
                color = new Color(0x12, 0x7E, 0x43);
                break;
            default:
                mimeType = "application/octet-stream";
                progId = "";
                color = Color.BLACK;
        }
    }

    public static IconData getIconData(String extension, int size) {
        String key = extension + "*" + size;
        return icons.computeIfAbsent(key, k -> new IconData());
    }

    @SneakyThrows
    public static OleData from(File file) {
        return from(file.getName(), FileUtils.readFileToByteArray(file));
    }

    @SneakyThrows
    public static OleData from(String fileName, byte[] data) {
        String extension = FilenameUtils.getExtension(fileName).trim().toUpperCase(Locale.ROOT);
        String mimeType = MimeUtility.guessMimeType(fileName, data);
        System.out.println(mimeType);
        if (MimeUtility.isText(mimeType)) {
            data = DocxWrapper.wrapText(new String(data, TEXT_CHARSET)).toByteArray();
            fileName = FilenameUtils.getBaseName(fileName) + ".docx";
        } else if (MimeUtility.isImage(mimeType)) {
            DocxDocument wrapper = DocxWrapper.wrapImage(extension, data);
            if (wrapper != null) {
                data = wrapper.toByteArray();
                fileName = FilenameUtils.getBaseName(fileName) + ".docx";
            }
        }
        return new OleData(fileName, extension, data);
    }

    public boolean isEmbeddable() {
        return StringUtils.isNotBlank(progId);
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class IconData {

        private BufferedImage icon;
        private String iconRel;

    }

}
