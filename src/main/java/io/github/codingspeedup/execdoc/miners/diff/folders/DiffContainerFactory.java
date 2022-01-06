package io.github.codingspeedup.execdoc.miners.diff.folders;

import java.io.File;

public interface DiffContainerFactory {

    FolderDiffContainer createDiffContainer(File leftRoot, File rightRoot);

}
