package io.github.codingspeedup.execdoc.miners.diff.folders;

import java.io.File;
import java.util.List;

public interface FolderDiffContainer {

    FolderDiffEntry add(File left, File right);

    List<FolderDiffEntry> getDiffs();

}
