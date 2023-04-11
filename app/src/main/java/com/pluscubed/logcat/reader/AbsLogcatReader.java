package com.pluscubed.logcat.reader;


public abstract class AbsLogcatReader implements LogcatReader {

    protected boolean recordingMode;

    public AbsLogcatReader(boolean recordingMode) {
        this.recordingMode = recordingMode;
    }

    @SuppressWarnings("unused")
    public boolean isRecordingMode() {
        return recordingMode;
    }
}
