package io.github.codingspeedup.execdoc.miners.logs;

import io.github.codingspeedup.execdoc.miners.logs.defaults.DefaultLogMinerListener;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogMiner {

    private final ResourceGroup group;
    private final List<LogMinerListener> mineListeners = new ArrayList<>();
    private boolean stopped;

    public LogMiner(ResourceGroup group, LogMinerListener... listeners) {
        this.group = group;
        for (LogMinerListener listener : listeners) {
            if (listener != null) {
                mineListeners.add(listener);
            }
        }
        if (ArrayUtils.isEmpty(listeners)) {
            mineListeners.add(new DefaultLogMinerListener());
        }
    }

    public void browse(FileFilter filter, LogFileMatcher matcher) {
        stopped = false;
        for (LogMinerListener listener : mineListeners) {
            listener.onStart();
        }
        for (Iterator<File> fileItearator = group.fileIterator(filter); fileItearator.hasNext(); ) {
            File file = fileItearator.next();
            if (stopped) {
                break;
            }
            for (LogMinerListener listener : mineListeners) {
                listener.onLocationDiscovered(file);
            }
            if (!file.isFile()) {
                continue;
            }
            for (LogMinerListener listener : mineListeners) {
                listener.onFileAccepted(file);
            }
            int firstMatchLineIndex = matcher.matches(file);
            if (firstMatchLineIndex >= 0) {
                for (LogMinerListener listener : mineListeners) {
                    listener.onMatchFound(new LogMinerFinding(file, firstMatchLineIndex));
                }
            }
        }
        for (LogMinerListener listener : mineListeners) {
            listener.onEnd();
        }
    }

    public void stop() {
        this.stopped = true;
    }

}
