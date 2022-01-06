package io.github.codingspeedup.execdoc.miners.logs.defaults;

import java.io.File;
import java.io.FileFilter;

public class DefaultLogFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.isFile();
    }

}
