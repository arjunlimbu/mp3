package com.example.bijesh.mp3.DB;

import android.provider.BaseColumns;

public class MusicContract implements  BaseColumns {
    public static final String TABLE_NAME_MUSIC = "music";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_ARTIST = "artist";
    public static final String COLUMN_NAME_ALBUM = "album";
    public static final String COLUMN_NAME_art = "art";
    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_TITLE = 1;
    public static final int COLUMN_INDEX_ARTIST = 2;
    public static final int COLUMN_INDEX_ALBUM = 3;
    public static final int COLUMN_INDEX_ART = 4;
}
