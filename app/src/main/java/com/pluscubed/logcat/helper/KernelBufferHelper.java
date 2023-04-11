package com.pluscubed.logcat.helper;

import com.pluscubed.logcat.util.UtilLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KernelBufferHelper {

    private static final UtilLogger log = new UtilLogger(KernelBufferHelper.class);

    private static List<String> getKernelBufferArgs() {

        return new ArrayList<>(Collections.singletonList("dmesg"));
    }

    public static List<CharSequence> getKernelBuffer() {
        Process dmesgProcess = null;
        BufferedReader reader = null;
        List<CharSequence> lines = new ArrayList<>();
        try {

            List<String> args = getKernelBufferArgs();

            dmesgProcess = RuntimeHelper.exec(args);
            reader = new BufferedReader(new InputStreamReader(dmesgProcess
                    .getInputStream()), 8192);

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            log.e(e, "unexpected exception");
        } finally {
            if (dmesgProcess != null) {
                RuntimeHelper.destroy(dmesgProcess);
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

        return lines;
    }
}
