package com.example.bijesh.mp3.DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.bijesh.mp3.DB.MusicContract;
import com.example.bijesh.mp3.DB.MusicDBHelper;

public class MusicProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.alimb.music";
    public static final String BASE_PATH = MusicContract.TABLE_NAME_MUSIC;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int MUSICS = 0;
    private static final int MUSIC_ID = 1;

    private MusicDBHelper helper;
    private UriMatcher urImatcher;

    public MusicProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowsDeleted = 0;
        int uriType = urImatcher.match(uri);
        switch (uriType) {
            case MUSICS:
                rowsDeleted = db.delete(MusicContract.TABLE_NAME_MUSIC,
                        selection, selectionArgs);
                break;
            case MUSIC_ID:
                String newSelection = appendToSelection(uri, selection);
                rowsDeleted = db.delete(MusicContract.TABLE_NAME_MUSIC,
                        newSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    private String appendToSelection(Uri uri, String selection) {
        String id = uri.getLastPathSegment();
        StringBuilder newSelection = new StringBuilder(MusicContract._ID + "=" + id);
        if (selection != null && !selection.isEmpty()) {
            newSelection.append(" AND " + selection);
        }
        return newSelection.toString();
    }


    @Override
    public String getType(Uri uri) {
        switch (urImatcher.match(uri)) {
            case MUSICS:
                return "vnd.android.cursor.dir/vnd.brookes.music";
            case MUSIC_ID:
                return "vnd.android.cursor.item/vnd.brookes.music";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int uriType = urImatcher.match(uri);
        Uri resultUri = null;
        if (uriType == MUSICS) {
            long rowId = db.insert(MusicContract.TABLE_NAME_MUSIC, null, values);
            resultUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(resultUri, null);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return resultUri;

    }

    @Override
    public boolean onCreate() {
        helper = new MusicDBHelper(getContext());
        urImatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urImatcher.addURI(AUTHORITY, BASE_PATH, MUSICS);
        urImatcher.addURI(AUTHORITY, BASE_PATH + "/#", MUSIC_ID);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MusicContract.TABLE_NAME_MUSIC);
        int uriType = urImatcher.match(uri);
        switch (uriType) {
            case MUSICS:
                break;
            case MUSIC_ID:
                builder.appendWhere(MusicContract._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unrecognised URI");
        }

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowsUpdated = 0;
        int uriType = urImatcher.match(uri);
        switch (uriType) {
            case MUSICS:
                rowsUpdated = db.update(MusicContract.TABLE_NAME_MUSIC,
                        values, selection, selectionArgs);
                break;
            case MUSIC_ID:
                String newSelection = appendToSelection(uri, selection);
                rowsUpdated = db.update(MusicContract.TABLE_NAME_MUSIC,
                        values, newSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
