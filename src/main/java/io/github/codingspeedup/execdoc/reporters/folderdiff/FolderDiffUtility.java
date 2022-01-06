package io.github.codingspeedup.execdoc.reporters.folderdiff;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class FolderDiffUtility {

    public static int cleanupLines(List<String> lines) {
        int removedFromTop = 0;
        while (!CollectionUtils.isEmpty(lines) && StringUtils.isBlank(lines.get(0))) {
            lines.remove(0);
            ++removedFromTop;
        }
        while (!CollectionUtils.isEmpty(lines) && StringUtils.isBlank(lines.get(lines.size() - 1))) {
            lines.remove(lines.size() - 1);
        }
        for (int i = 0; i < lines.size(); ++i) {
            lines.set(i, StringUtils.stripEnd(lines.get(i), null));
        }
        return removedFromTop;
    }

}
