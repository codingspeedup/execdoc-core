package io.github.codingspeedup.execdoc.miners.diff.folders.defaults;

import java.io.File;
import java.io.FileFilter;

public class DefaultFileFilter implements FileFilter {

    public static final String[] IGNORER_FOLDERS = new String[]{".git", ".svn"};

    @Override
    public boolean accept(File pathname) {
        String name = pathname.getName();
        if (pathname.isDirectory()) {
            for (String ignored : IGNORER_FOLDERS) {
                if (ignored.equalsIgnoreCase(name)) {
                    return false;
                }
            }
        }
        return true;
    }

}
