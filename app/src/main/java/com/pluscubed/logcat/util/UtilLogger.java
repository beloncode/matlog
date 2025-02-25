package com.pluscubed.logcat.util;

import android.util.Log;

import com.pluscubed.logcat.BuildConfig;

import java.util.Arrays;

/**
 * Easier way to interact with logcat.
 *
 * @author nolan
 */
public class UtilLogger {

	public static final boolean DEBUG_MODE = BuildConfig.DEBUG;

	private final String tag;

	public UtilLogger(String tag) {
		this.tag = tag;
	}

	public UtilLogger(Class<?> clazz) {
		this.tag = clazz.getSimpleName();
	}

	public void i(String format, Object... more) {
		Log.i(tag, String.format(format, more));
	}

	public void i(Exception e, String format, Object... more) {
		Log.i(tag, String.format(format, more), e);
	}

	public void w(Exception e, String format, Object... more) {
		Log.w(tag, String.format(format, more), e);
	}

	@SuppressWarnings("unused")
	public void w(String format, Object... more) {
		Log.w(tag, String.format(format, more));
	}

	public void e(String format, Object... more) {
		Log.e(tag, String.format(format, more));
	}

	public void e(Exception e, String format, Object... more) {
		Log.e(tag, String.format(format, more), e);
	}

	public void d(String format, Object... more) {
		if (DEBUG_MODE) {
			for (int i = 0; i < more.length; i++) {
				if (more[i] instanceof int[]) {
					more[i] = Arrays.toString((int[]) more[i]);
				}
			}
			Log.d(tag, String.format(format, more));
		}
	}

	public void d(Exception e, String format, Object... more) {
		if (DEBUG_MODE) {
			for (int i = 0; i < more.length; i++) {
				if (more[i] instanceof int[]) {
					more[i] = Arrays.toString((int[]) more[i]);
				}
			}
			Log.d(tag, String.format(format, more), e);
		}
	}
}
