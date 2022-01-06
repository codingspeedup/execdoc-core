package io.github.codingspeedup.execdoc.toolbox.processes;

import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OsProcess {

    private final long timeout;

    @Getter
    private Integer exitValue;

    public OsProcess() {
        this(-1);
    }

    public OsProcess(long timeout) {
        this.timeout = timeout;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Object... args) {
        Map<String, String> sysEnv = null;
        Folder workingDirectory = null;
        String command = null;
        List<String> params = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(args)) {
            for (Object arg : args) {
                if (arg == null) {
                    continue;
                }
                if (arg instanceof Map && sysEnv == null) {
                    sysEnv = (Map<String, String>) arg;
                } else if (arg instanceof Folder && workingDirectory == null) {
                    workingDirectory = (Folder) arg;
                } else if (arg instanceof String) {
                    if (command == null) {
                        command = (String) arg;
                    } else {
                        params.add((String) arg);
                    }
                } else if (arg instanceof List) {
                    for (Object subArg : (List) arg) {
                        if (subArg instanceof String) {
                            params.add((String) subArg);
                        }
                    }
                }
            }
        }
        execute(sysEnv, workingDirectory, command, params);
    }

    @SneakyThrows
    public void execute(Map<String, String> sysEnv, Folder workingDirectory, String command, List<String> params) {
        List<String> commandLine = new ArrayList<>();
        commandLine.add(command);
        if (CollectionUtils.isNotEmpty(params)) {
            commandLine.addAll(params);
        }
        ProcessBuilder builder = new ProcessBuilder(commandLine);
        if (workingDirectory != null) {
            builder.directory(workingDirectory);
        }
        if (MapUtils.isNotEmpty(sysEnv)) {
            for (Map.Entry<String, String> entry : sysEnv.entrySet()) {
                builder.environment().put(entry.getKey(), entry.getValue());
            }
        }
        Process process = builder.inheritIO().start();
        if (timeout < 0) {
            process.waitFor();
            exitValue = process.exitValue();
        } else if (timeout > 0) {
            process.waitFor(timeout, TimeUnit.MILLISECONDS);
            exitValue = process.exitValue();
        }
    }

}
