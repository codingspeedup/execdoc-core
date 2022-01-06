package io.github.codingspeedup.execdoc.miners.diff.folders;

import java.io.File;

public interface FolderDiffEntry {

    File getLeft();

    String[] getLeftPath();

    File getRight();

    String[] getRightPath();

    default String[] getPath() {
        return getLeftPath() != null ? getLeftPath() : getRightPath();
    }

    default boolean isLeft() {
        return getLeft() != null;
    }

    default boolean isRight() {
        return getRight() != null;
    }

    default boolean isLeftOnly() {
        return !isRight();
    }

    default boolean isRightOnly() {
        return !isLeft();
    }

    default boolean isBoth() {
        return isLeft() && isRight();
    }

    default boolean isRootFile() {
        return (isLeft() && getLeft().isFile() && getLeftPath().length == 1)
                       || (isRight() && getRight().isFile() && getRightPath().length == 1);
    }

    default boolean isFolder() {
        return isLeft() && getLeft().isDirectory() || isRight() && getRight().isDirectory();
    }

    default boolean isFile() {
        return isLeft() && getLeft().isFile() || isRight() && getRight().isFile();
    }

    default String getName() {
        String[] path = getPath();
        return path[path.length - 1];
    }

}
