package io.github.codingspeedup.execdoc.miners.logs;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.util.Locale;

public class LogFileMatcher {

    private final String pattern;
    private final boolean caseSensitive;

    public LogFileMatcher(String pattern, boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        this.pattern = caseSensitive ?  pattern : pattern.toUpperCase(Locale.ROOT);
    }

    @SneakyThrows
    public int matches(File theFile) {
        try (LineIterator it = FileUtils.lineIterator(theFile, "UTF-8")) {
            int lineNumber = 0;
            while (it.hasNext()) {
                String line = it.nextLine();
                if (!caseSensitive) {
                    line = line.toUpperCase(Locale.ROOT);
                }
                if (line.contains(pattern)) {
                    return lineNumber;
                }
                ++lineNumber;
            }
        }
        return -1;
    }

}
