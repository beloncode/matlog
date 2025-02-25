package com.pluscubed.logcat.reader;

import android.os.AsyncTask;

import com.pluscubed.logcat.util.UtilLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Combines multiple buffered readers into a single reader that merges all input synchronously.
 *
 * @author nolan
 */
public class MultipleLogcatReader extends AbsLogcatReader {

    private static final String DUMMY_NULL = "";
    private static final UtilLogger log = new UtilLogger(MultipleLogcatReader.class);
    private final List<ReaderThread> readerThreads = new LinkedList<>();
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

    public MultipleLogcatReader(boolean recordingMode,
                                Map<String, String> lastLines) throws IOException {
        super(recordingMode);
        // read from all three buffers at once
        for (Entry<String, String> entry : lastLines.entrySet()) {
            String logBuffer = entry.getKey();
            String lastLine = entry.getValue();
            ReaderThread readerThread = new ReaderThread(logBuffer, lastLine);
            readerThread.start();
            readerThreads.add(readerThread);
        }
    }

    public String readLine() throws IOException {

        try {
            String value = queue.take();
            if (!value.equals(DUMMY_NULL)) {
                return value;
            }
        } catch (InterruptedException e) {
            log.d(e, "");
        }
        return null;
    }


    @Override
    public boolean readyToRecord() {
        for (ReaderThread thread : readerThreads) {
            if (!thread.reader.readyToRecord()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void killQuietly() {
        for (ReaderThread thread : readerThreads) {
            thread.killed = true;
        }

        // do in background, because otherwise we might hang
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                for (ReaderThread thread : readerThreads) {
                    thread.reader.killQuietly();
                }
                queue.offer(DUMMY_NULL);
                return null;
            }
        }.execute((Void) null);
    }


    @Override
    public List<Process> getProcesses() {
        List<Process> result = new ArrayList<>();
        for (ReaderThread thread : readerThreads) {
            result.addAll(thread.reader.getProcesses());
        }
        return result;
    }

    private class ReaderThread extends Thread {

        SingleLogcatReader reader;

        private boolean killed;

        public ReaderThread(String logBuffer, String lastLine) throws IOException {
            this.reader = new SingleLogcatReader(recordingMode, logBuffer, lastLine);
        }

        @Override
        public void run() {
            String line;

            try {
                while (!killed && (line = reader.readLine()) != null && !killed) {
                    queue.put(line);
                }
            } catch (IOException | InterruptedException e) {
                log.d(e, "exception");
            }
            log.d("thread died");
        }
    }
}
