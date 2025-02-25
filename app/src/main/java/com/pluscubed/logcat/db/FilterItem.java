package com.pluscubed.logcat.db;

import androidx.annotation.NonNull;

import java.util.Comparator;


public class FilterItem implements Comparable<FilterItem> {

    public static final Comparator<FilterItem> DEFAULT_COMPARATOR = (lhs, rhs) -> {
        final String leftText = lhs.text != null ? lhs.text : "";
        final String rightText = rhs.text != null ? rhs.text : "";
        return leftText.compareToIgnoreCase(rightText);
    };
    private int id;
    private String text;

    private FilterItem() {
    }

    public static FilterItem create(int id, String text) {
        final FilterItem filterItem = new FilterItem();
        filterItem.id = id;
        filterItem.text = text;
        return filterItem;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(@NonNull FilterItem another) {
        return DEFAULT_COMPARATOR.compare(this, another);
    }
}
