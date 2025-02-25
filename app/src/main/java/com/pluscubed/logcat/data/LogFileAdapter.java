package com.pluscubed.logcat.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pluscubed.logcat.R;
import com.pluscubed.logcat.helper.SaveLogHelper;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class LogFileAdapter extends ArrayAdapter<CharSequence> {
    private final List<CharSequence> objects;
    private final int checked;
    private final boolean multiMode;
    private boolean[] checkedItems;
    private final int resId;

    public LogFileAdapter(Context context, List<CharSequence> objects, int checked, boolean multiMode) {

        super(context, -1, objects);
        this.objects = objects;
        this.checked = checked;
        this.multiMode = multiMode;
        if (multiMode) {
            checkedItems = new boolean[objects.size()];
        }
        resId = multiMode ? R.layout.list_item_logfilename_multi : R.layout.list_item_logfilename_single;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        final Context context = parent.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resId, parent, false);
        }

        final CheckBox box = view.findViewById(android.R.id.checkbox);
        final RadioButton button = view.findViewById(android.R.id.button1);
        final TextView text1 = view.findViewById(android.R.id.text1);
        final TextView text2 = view.findViewById(android.R.id.text2);

        final CharSequence filename = objects.get(position);

        text1.setText(filename);


        if (multiMode) {
            box.setChecked(checkedItems[position]);
        } else {
            button.setChecked(checked == position);
        }

        final Date lastModified = SaveLogHelper.getLastModifiedDate(filename.toString());
        final DateFormat dateFormat = DateFormat.getDateTimeInstance();

        text2.setText(dateFormat.format(lastModified));

        return view;
    }

    public void checkOrUncheck(int position) {
        checkedItems[position] = !checkedItems[position];
        notifyDataSetChanged();
    }

    public boolean[] getCheckedItems() {
        return checkedItems;
    }
}
