package com.example.bijesh.mp3.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bijesh.mp3.DB.MusicContract;

import static com.example.bijesh.mp3.DB.MusicContract.TABLE_NAME_MUSIC;

public class MusicDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "musics.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_MUSIC_ENTRIES =
            "CREATE TABLE " + MusicContract.TABLE_NAME_MUSIC + " (" +
                    MusicContract._ID + " INTEGER " + " PRIMARY KEY " + COMMA_SEP +
                    MusicContract.COLUMN_NAME_TITLE + TEXT_TYPE + " UNIQUE " + COMMA_SEP +
                    MusicContract.COLUMN_NAME_ARTIST+ TEXT_TYPE + COMMA_SEP +
                    MusicContract.COLUMN_NAME_ALBUM + TEXT_TYPE + ")";


    private static final String DELETE_MUSIC_ENTRIES = "DROP TABLE IF EXISTS " +
            TABLE_NAME_MUSIC;


    public MusicDBHelper(Context context) {
        super(context, DATABASE_NAME,
                null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MUSIC_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_MUSIC_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
