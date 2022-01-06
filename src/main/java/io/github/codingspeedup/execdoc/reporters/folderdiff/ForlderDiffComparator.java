package io.github.codingspeedup.execdoc.reporters.folderdiff;

import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffEntry;

import java.util.Comparator;

public class ForlderDiffComparator implements Comparator<FolderDiffEntry> {

    @Override
    public int compare(FolderDiffEntry e1, FolderDiffEntry e2) {
        String[] p1 = e1.getPath();
        int p1Length = p1.length;
        if (!e1.isFolder()) {
            --p1Length;
        }
        String[] p2 = e2.getPath();
        int p2Length = p2.length;
        if (!e2.isFolder()) {
            --p2Length;
        }
        for (int i = 0; i < Math.min(p1Length, p2Length); ++i) {
            int cmp = p1[i].compareTo(p2[i]);
            if (cmp != 0) {
                return cmp;
            }
        }
        int cmp = Integer.compare(p1.length, p2.length);
        if (cmp != 0) {
            return cmp;
        }

        // same folder
        if (e1.isFolder() && e2.isFile()) {
            return 1;
        }
        if (e1.isFile() && e2.isFolder()) {
            return -1;
        }
        return compareARM(e1, e2);
    }

    private int compareARM(FolderDiffEntry e1, FolderDiffEntry e2) {
        if (e1.isRightOnly()) {
            return e2.isRightOnly() ? 0 : -1;
        }
        if (e1.isLeftOnly()) {
            if (e2.isRightOnly()) {
                return 1;
            }
            return e2.isLeftOnly() ? 0 : -1;
        }
        return e2.isBoth() ? 0 : 1;
    }

}
