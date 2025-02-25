package com.pluscubed.logcat.reader;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.pluscubed.logcat.helper.LogcatHelper;
import com.pluscubed.logcat.helper.PreferenceHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LogcatReaderLoader implements Parcelable {

    public static final Parcelable.Creator<LogcatReaderLoader> CREATOR = new Parcelable.Creator<LogcatReaderLoader>() {
        public LogcatReaderLoader createFromParcel(Parcel in) {
            return new LogcatReaderLoader(in);
        }

        public LogcatReaderLoader[] newArray(int size) {
            return new LogcatReaderLoader[size];
        }
    };
    private final Map<String, String> lastLines = new HashMap<>();
    private final boolean recordingMode;
    private final boolean multiple;

    private LogcatReaderLoader(Parcel in) {
        this.recordingMode = in.readInt() == 1;
        this.multiple = in.readInt() == 1;
        final Bundle bundle = in.readBundle(getClass().getClassLoader());
        for (String key : bundle.keySet()) {
            lastLines.put(key, bundle.getString(key));
        }
    }

    private LogcatReaderLoader(List<String> buffers, boolean recordingMode) {
        this.recordingMode = recordingMode;
        this.multiple = buffers.size() > 1;
        for (String buffer : buffers) {
            // no need to grab the last line if this isn't recording mode
            final String lastLine = recordingMode ? LogcatHelper.getLastLogLine(buffer) : null;
            lastLines.put(buffer, lastLine);
        }
    }

    public static LogcatReaderLoader create(Context context, boolean recordingMode) {
        final List<String> buffers = PreferenceHelper.getBuffers(context);
        return new LogcatReaderLoader(buffers, recordingMode);
    }

    public LogcatReader loadReader() throws IOException {
        LogcatReader reader;
        if (!multiple) {
            // single reader
            final String buffer = lastLines.keySet().iterator().next();
            final String lastLine = lastLines.values().iterator().next();
            reader = new SingleLogcatReader(recordingMode, buffer, lastLine);
        } else {
            // multiple reader
            reader = new MultipleLogcatReader(recordingMode, lastLines);
        }

        return reader;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, int flags) {
        dest.writeInt(recordingMode ? 1 : 0);
        dest.writeInt(multiple ? 1 : 0);
        Bundle bundle = new Bundle();
        for (Entry<String, String> entry : lastLines.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        dest.writeBundle(bundle);
    }
}
