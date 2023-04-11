package com.pluscubed.logcat.reader;

import java.io.IOException;
import java.util.List;

public interface LogcatReader {

    /**
     * Read a single log line, ala BufferedReader.readLine().
     *
     */
    String readLine() throws IOException;

    /**
     * Kill the reader and close all resources without throwing any exceptions.
     */
    void killQuietly();

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean readyToRecord();

    List<Process> getProcesses();

}
