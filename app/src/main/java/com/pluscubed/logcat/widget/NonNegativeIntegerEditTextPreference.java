package com.pluscubed.logcat.widget;

import android.content.Context;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.prefs.MaterialEditTextPreference;

/**
 * EditTextPreference that only allows inputting integer numbers.
 *
 * @author nlawson
 */
public class NonNegativeIntegerEditTextPreference extends MaterialEditTextPreference {
    @SuppressWarnings("unused")
    public NonNegativeIntegerEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpEditText();
    }

    @SuppressWarnings("unused")
    public NonNegativeIntegerEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpEditText();
    }

    @SuppressWarnings("unused")
    public NonNegativeIntegerEditTextPreference(Context context) {
        super(context);
        setUpEditText();
    }

    private void setUpEditText() {
        getEditText().setKeyListener(DigitsKeyListener.getInstance(false, false));
    }
}
