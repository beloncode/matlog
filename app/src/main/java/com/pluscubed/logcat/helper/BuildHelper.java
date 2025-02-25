package com.pluscubed.logcat.helper;

import android.os.Build;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class BuildHelper {

    // public static final Strings of android.os.Build
    private static final List<String> BUILD_FIELDS = Arrays.asList(
            "BOARD", "BOOTLOADER", "BRAND", "CPU_ABI", "CPU_ABI2",
            "DEVICE", "DISPLAY", "FINGERPRINT", "HARDWARE", "HOST",
            "ID", "MANUFACTURER", "MODEL", "PRODUCT", "RADIO",
            "SERIAL", "TAGS", "TIME", "TYPE", "USER");

    // public static final Strings of android.os.Build.Version
    private static final List<String> BUILD_VERSION_FIELDS = Arrays.asList(
            "CODENAME", "INCREMENTAL", "RELEASE", "SDK_INT");

    public static String getBuildInformationAsString() {
        final SortedMap<String, String> keysToValues = new TreeMap<>();

        for (String buildField : BUILD_FIELDS) {
            putKeyValue(Build.class, buildField, keysToValues);
        }
        for (String buildVersionField : BUILD_VERSION_FIELDS) {
            putKeyValue(Build.VERSION.class, buildVersionField, keysToValues);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Entry<String, String> entry : keysToValues.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
        }
        return stringBuilder.toString();
    }

    private static void putKeyValue(Class<?> clazz, String buildField, SortedMap<String, String> keysToValues) {
        try {
            final Field field = clazz.getField(buildField);
            final Object value = field.get(null);
            final String key = clazz.getSimpleName().toLowerCase() + "." + buildField.toLowerCase();
            keysToValues.put(key, String.valueOf(value));
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException e) {
            // ignore
        }
    }

}
