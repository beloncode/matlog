package com.pluscubed.logcat.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pluscubed.logcat.R;
import com.pluscubed.logcat.helper.PackageHelper;
import com.pluscubed.logcat.util.UtilLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutDialogActivity extends BaseActivity {

    private static final UtilLogger log = new UtilLogger(AboutDialogActivity.class);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DialogFragment fragment = new AboutDialog();
        fragment.show(getFragmentManager(), "aboutDialog");

    }

    public static class AboutDialog extends DialogFragment {

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            getActivity().finish();
        }


        public void initializeWebView(WebView view) {
            String text = "";
            try {
                text = loadTextFile("about_body.html");

                final String changelog = loadTextFile("changelog.html");
                final String css = loadTextFile("about_css.css");

                final String version = PackageHelper.getVersionName(getActivity());

                text = String.format(text, version, changelog, css);
            } catch (IOException io) {
                log.e(io, "fixes this");
            }

            WebSettings settings = view.getSettings();
            settings.setDefaultTextEncodingName("utf-8");

            view.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
        }

        private String loadTextFile(final String resName) throws IOException {

            InputStream is = null;
            AssetManager assetManager = getContext().getAssets();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                is = assetManager.open(resName);
            }
            StringBuilder sb = new StringBuilder();
            try (BufferedReader buff = new BufferedReader(new InputStreamReader(is))) {
                while (buff.ready()) {
                    sb.append(buff.readLine()).append("\n");
                }
            } catch (IOException e) {
                log.e(e, "This should not happen");
            }

            return sb.toString();

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            WebView view = new WebView(getActivity());
/*
            view.setWebViewClient(new AboutWebClient());*/
            initializeWebView(view);

            return new MaterialDialog.Builder(getActivity())
                    .customView(view, false)
                    .title(R.string.about_matlog)
                    .iconRes(R.mipmap.ic_launcher)
                    .positiveText(android.R.string.ok)
                    .build();
        }


    }
}
