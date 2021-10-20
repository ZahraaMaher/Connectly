package com.example.connectly.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class SeekersContactsProvider extends ContentProvider {

    SQLiteDatabase seekerSQLiteDatabase;
    private static final String AUTHORITY = "com.example.connectly";
    private static final String BASE_PATH = "seekers";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int SEEKER = 0;
//    private static final String SEEKER_EMAIL = "email";
    private static final int SEEKER_EMAIL = 1;

    private static final String SEEKER_TABLE_NAME = "seeker";

    Cursor cursor;
    public static final String[] ALL_COLUMNS =
            {"email", "password", "firstname", "lastname","phone","age","dob","gender"};

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, SEEKER);
//        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", EMAIL);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/" + SEEKER_TABLE_NAME, SEEKER_EMAIL);
    }

    @Override
    public boolean onCreate() {

        seekerSQLiteDatabase = getContext().openOrCreateDatabase("Users", MODE_PRIVATE, null);
        seekerSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS seeker (email VARCHAR, password VARCHAR, firstname VARCHAR, lastname VARCHAR, phone VARCHAR, age VARCHAR, dob VARCHAR, gender VARCHAR)");

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        switch (uriMatcher.match(uri)) {

            case SEEKER:

                cursor = seekerSQLiteDatabase.query(SEEKER_TABLE_NAME, ALL_COLUMNS,selection
                        , null, null, null, "firstname" + " ASC");


                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {
            case SEEKER:
                return "com.example.connectly/seeker";
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long id = seekerSQLiteDatabase.insert(SEEKER_TABLE_NAME, null, contentValues);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case SEEKER:
                delCount = seekerSQLiteDatabase.delete(SEEKER_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {

            case SEEKER:
                updCount = seekerSQLiteDatabase.update(SEEKER_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }
}
