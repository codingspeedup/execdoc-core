package io.github.codingspeedup.execdoc.miners.diff.folders.defaults;

import io.github.codingspeedup.execdoc.miners.diff.folders.DiffContainerFactory;
import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffContainer;

import java.io.File;

public class DefaultDiffContainerFactory implements DiffContainerFactory {

    @Override
    public FolderDiffContainer createDiffContainer(File leftRoot, File rightRoot) {
        return new DefaultFolderDiffContainer(leftRoot, rightRoot);
    }

}
