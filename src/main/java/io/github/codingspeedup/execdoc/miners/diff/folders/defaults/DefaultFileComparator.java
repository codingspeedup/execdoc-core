package io.github.codingspeedup.execdoc.miners.diff.folders.defaults;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Comparator;

public class DefaultFileComparator implements Comparator<File> {

    private final boolean ignoreEOL;

    public DefaultFileComparator(boolean ignoreEOL) {
        this.ignoreEOL = ignoreEOL;
    }

    @Override
    public int compare(File left, File right) {
        try (Reader lr = new FileReader(left); Reader rr = new FileReader(right)) {
            if (ignoreEOL) {
                return IOUtils.contentEqualsIgnoreEOL(lr, rr) ? 0 : 2;
            } else {
                return IOUtils.contentEquals(lr, rr) ? 0 : 1;
            }
        } catch (IOException e) {
            try (InputStream ls = new FileInputStream(left); InputStream rs = new FileInputStream(right)) {
                return IOUtils.contentEquals(ls, rs) ? 0 : -1;
            } catch (IOException e1) {
                return -2;
            }
        }
    }

}
