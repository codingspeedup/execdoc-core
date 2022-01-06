package io.github.codingspeedup.execdoc.toolbox.processes;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JavaProcess {

    private final long timeout;

    @Getter
    private Integer exitValue;

    public JavaProcess() {
        this(-1);
    }

    public JavaProcess(long timeout) {
        this.timeout = timeout;
    }

    public void execute(Class<?> clazz, List<String> jvmArgs, List<String> args)
            throws IOException, InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = clazz.getName();

        List<String> command = new ArrayList<>();
        command.add(javaBin);
        if (CollectionUtils.isNotEmpty(jvmArgs)) {
            command.addAll(jvmArgs);
        }
        command.add(className);
        if (CollectionUtils.isNotEmpty(args)) {
            command.addAll(args);
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.environment().put("CLASSPATH", classpath);

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
