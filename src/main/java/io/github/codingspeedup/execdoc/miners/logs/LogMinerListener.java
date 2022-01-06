package io.github.codingspeedup.execdoc.miners.logs;

import java.io.File;

public interface LogMinerListener {

    void onStart();

    void onLocationDiscovered(File file);

    void onFileAccepted(File file);

    void onMatchFound(LogMinerFinding finding);

    void onEnd();

}
