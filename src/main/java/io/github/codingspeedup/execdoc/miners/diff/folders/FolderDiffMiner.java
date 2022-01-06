package io.github.codingspeedup.execdoc.miners.diff.folders;

import io.github.codingspeedup.execdoc.miners.diff.folders.defaults.DefaultDiffContainerFactory;
import io.github.codingspeedup.execdoc.miners.diff.folders.defaults.DefaultFileComparator;
import io.github.codingspeedup.execdoc.miners.diff.folders.defaults.DefaultFileFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class FolderDiffMiner {

    private FileFilter leftFilter = new DefaultFileFilter();
    private FileFilter rigtFilter = new DefaultFileFilter();
    private boolean caseSensitive = false;
    private Comparator<File> fileComparator = new DefaultFileComparator(true);
    private DiffContainerFactory diffContainerFactory = new DefaultDiffContainerFactory();

    @SneakyThrows
    public FolderDiffContainer compare(File leftRoot, File rightRoot) {
        if (leftRoot == null || !leftRoot.isDirectory()) {
            throw new UnsupportedOperationException(leftRoot + " is not a directory");
        }
        if (rightRoot == null || !rightRoot.isDirectory()) {
            throw new UnsupportedOperationException(rightRoot + " is not a directory");
        }
        FolderDiffContainer diffs = diffContainerFactory.createDiffContainer(leftRoot, rightRoot);
        compareFolders(diffs, leftRoot, rightRoot);
        return diffs;
    }

    private void compareFolders(FolderDiffContainer diffs, File leftFolder, File rightFolder) {
        Set<String> leftNames = Arrays.stream(Objects.requireNonNull(leftFolder.listFiles()))
                .filter(leftFilter::accept)
                .map(File::getName)
                .map(name -> caseSensitive ? name : name.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        Set<String> rightNames = Arrays.stream(Objects.requireNonNull(rightFolder.listFiles()))
                .filter(rigtFilter::accept)
                .map(File::getName)
                .map(name -> caseSensitive ? name : name.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        Set<String> common = new HashSet<>(leftNames);

        common.retainAll(rightNames);
        for (String name : common) {
            compareEntries(diffs, new File(leftFolder, name), new File(rightFolder, name));
        }

        leftNames.removeAll(common);
        for (String name : leftNames) {
            add(diffs, new File(leftFolder, name), null);
        }

        rightNames.removeAll(common);
        for (String name : rightNames) {
            add(diffs, null, new File(rightFolder, name));
        }
    }

    private void compareEntries(FolderDiffContainer diffs, File leftEntry, File rightEntry) {
        if (leftEntry.isDirectory() ^ rightEntry.isDirectory()) {
            add(diffs, leftEntry, null);
            add(diffs, null, rightEntry);
        } else if (leftEntry.isDirectory()) {
            compareFolders(diffs, leftEntry, rightEntry);
        } else if (fileComparator.compare(leftEntry, rightEntry) != 0) {
            add(diffs, leftEntry, rightEntry);
        }
    }

    private void add(FolderDiffContainer diffs, File leftEntry, File rightEntry) {
        if (leftEntry == null && rightEntry != null && rightEntry.isDirectory()) {
            File[] entries = rightEntry.listFiles();
            if (entries == null || entries.length == 0) {
                diffs.add(null, rightEntry);
            } else {
                for (File entry : entries) {
                    add(diffs, null, entry);
                }
            }
        } else if (rightEntry == null && leftEntry != null && leftEntry.isDirectory()) {
            File[] entries = leftEntry.listFiles();
            if (entries == null || entries.length == 0) {
                diffs.add(leftEntry, rightEntry);
            } else {
                for (File entry : entries) {
                    add(diffs, entry, null);
                }
            }
        } else {
            diffs.add(leftEntry, rightEntry);
        }
    }

}
