package com.example.vishwasdamle.quicknote.model;

/**
 * Created by vishwasdamle on 04/04/15.
 */
public class Constants {
    public static final String FILENAME_KEY = "filename";
    public static final String REQUEST_CODE_KEY = "RequestCode";

    public static final String FILENAME_PREFIX = "QuickNoteRecord";
    public static final String CSV_EXTENSION = ".csv";

    public static final String DATE_TIME_PATTERN_FILE = "yyyy-MM-dd_HH.mm.ss";
    public static final String DATE_TIME_PATTERN_DISPLAY = "dd MMM yyyy, HH:mmaa";
    public static final String REGEX_DOUBLE = "[-+]?[0-9]*\\.?[0-9]+";

    public static final int EXPORT_REQUEST_CODE = 1;
    public static final int EXPORT_TO_SHARE_REQUEST_CODE = 2;
    public static final int SHARE_REQUEST_CODE = 3;
    public static final int LIST_REQUEST_CODE = 4;

    public static final String FILE_TYPE_KEY = "fileType";
    public static final String FILE_TYPE_CSV = "CSV";
    public static final String DB_NAME = "QuickNote";
}
