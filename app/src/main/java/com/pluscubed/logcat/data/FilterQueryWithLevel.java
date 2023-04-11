package com.pluscubed.logcat.data;

public class FilterQueryWithLevel {

    private final String filterQuery;
    private final String logLevel;

    public FilterQueryWithLevel(String filterQuery, String logLevel) {
        this.filterQuery = filterQuery;
        this.logLevel = logLevel;
    }

    public String getFilterQuery() {
        return filterQuery;
    }

    public String getLogLevel() {
        return logLevel;
    }
}
