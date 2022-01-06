package io.github.codingspeedup.execdoc.miners.logs.defaults;

import io.github.codingspeedup.execdoc.miners.logs.LogMinerFinding;
import io.github.codingspeedup.execdoc.miners.logs.LogMinerListener;
import lombok.SneakyThrows;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultLogMinerListener implements LogMinerListener {

    private final List<LogMinerFinding> findings = new ArrayList<>();

    @Override
    public void onStart() {
        System.out.println("Search started at " + LocalDateTime.now());
    }

    @SneakyThrows
    @Override
    public void onLocationDiscovered(File file) {
        if (file.isDirectory()) {
            System.out.println(">>> " + file.getCanonicalPath());
        }
    }

    @SneakyThrows
    @Override
    public void onFileAccepted(File file) {
        System.out.println("... " + file.getCanonicalPath());
    }

    public void onMatchFound(LogMinerFinding finding) {
        findings.add(finding);
    }

    @SneakyThrows
    @Override
    public void onEnd() {
        System.out.println("Search ended at " + LocalDateTime.now());
        for (LogMinerFinding f : findings) {
            System.out.format("Found at line %6d in " + f.getFile().getCanonicalPath() + "\n", f.getFirstMatchLineIndex() + 1);
        }
    }

}
