package com.pluscubed.logcat.helper;

import com.pluscubed.logcat.util.UtilLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogcatHelper {

    public static final String BUFFER_MAIN = "main";
    @SuppressWarnings("unused")
    public static final String BUFFER_EVENTS = "events";
    @SuppressWarnings("unused")
    public static final String BUFFER_RADIO = "radio";

    private static final UtilLogger log = new UtilLogger(LogcatHelper.class);

    public static Process getLogcatProcess(String buffer) throws IOException {

        final List<String> args = getLogcatArgs(buffer);

        return RuntimeHelper.exec(args);
    }

    private static List<String> getLogcatArgs(String buffer) {
        final List<String> args = new ArrayList<>(Arrays.asList("logcat", "-v", "time"));

        // for some reason, adding -b main excludes log output from AndroidRuntime runtime exceptions,
        // whereas just leaving it blank keeps them in.  So do not specify the buffer if it is "main"
        if (!buffer.equals(BUFFER_MAIN)) {
            args.add("-b");
            args.add(buffer);
        }

        return args;
    }

    public static String getLastLogLine(String buffer) {
        Process dumpLogcatProcess = null;
        BufferedReader reader = null;
        String result = null;
        try {

            List<String> args = getLogcatArgs(buffer);
            args.add("-d"); // -d just dumps the whole thing

            dumpLogcatProcess = RuntimeHelper.exec(args);
            reader = new BufferedReader(new InputStreamReader(dumpLogcatProcess.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                result = line;
            }
        } catch (IOException e) {
            log.e(e, "unexpected exception");
        } finally {
            if (dumpLogcatProcess != null) {
                RuntimeHelper.destroy(dumpLogcatProcess);
                log.d("destroyed 1 dump logcat process");
            }
            // post-jellybean, we just kill the process, so there's no need
            // to close the bufferedReader.  Anyway, it just hangs.
            if (VersionHelper.getVersionSdkIntCompat() < VersionHelper.VERSION_JELLYBEAN
                    && reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.e(e, "unexpected exception");
                }
            }
        }

        return result;
    }
}
