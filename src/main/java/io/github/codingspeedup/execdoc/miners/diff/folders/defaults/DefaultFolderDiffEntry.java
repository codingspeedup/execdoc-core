package io.github.codingspeedup.execdoc.miners.diff.folders.defaults;

import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor
@Getter
public class DefaultFolderDiffEntry implements FolderDiffEntry {

    private File left;
    private File right;
    private String[] leftPath;
    private String[] rightPath;

    public DefaultFolderDiffEntry(File left, File right, String[] leftPath, String[] rightPath) {
        this.left = left;
        this.right = right;
        this.leftPath = leftPath;
        this.rightPath = rightPath;
    }

}
