package io.github.codingspeedup.execdoc.miners.logs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogMinerFinding {

    private File file;
    private int firstMatchLineIndex;

    @Override
    public String toString() {
        return file.getAbsolutePath() + ": " + (firstMatchLineIndex + 1);
    }

}
