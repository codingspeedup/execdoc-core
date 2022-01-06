package io.github.codingspeedup.execdoc.miners.diff.folders.defaults;

import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffContainer;
import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffEntry;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DefaultFolderDiffContainer implements FolderDiffContainer {

    private final String leftRootPath;
    private final String rightRootPath;
    private final List<FolderDiffEntry> diffs = new ArrayList<>();

    @SneakyThrows
    public DefaultFolderDiffContainer(File leftRoot, File rightRoot) {
        leftRootPath = leftRoot.getCanonicalPath();
        rightRootPath = rightRoot.getCanonicalPath();
    }

    @SneakyThrows
    @Override
    public DefaultFolderDiffEntry add(File left, File right) {
        String[] leftPath = null;
        if (left != null) {
            leftPath = splitPath(left.getCanonicalPath().substring(leftRootPath.length()));
        }
        String[] rightPath = null;
        if (right != null) {
            rightPath = splitPath(right.getCanonicalPath().substring(rightRootPath.length()));
        }
        DefaultFolderDiffEntry entry = new DefaultFolderDiffEntry(left, right, leftPath, rightPath);
        diffs.add(entry);
        return entry;
    }

    private String[] splitPath(String path) {
        path = path.replace(File.separator, "/");
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path.split("/");
    }

    @Override
    public List<FolderDiffEntry> getDiffs() {
        return diffs;
    }

}
