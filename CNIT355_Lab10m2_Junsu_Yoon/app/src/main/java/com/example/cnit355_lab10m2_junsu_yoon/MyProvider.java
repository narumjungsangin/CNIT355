package com.example.cnit355_lab10m2_junsu_yoon;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyProvider extends ContentProvider {

    public static final String AUTHORITY =
            "com.example.cnit355_lab10m2_junsu_yoon.provider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/contacts");

    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(AUTHORITY, "contacts", CONTACTS);
        matcher.addURI(AUTHORITY, "contacts/#", CONTACT_ID);
    }

    private DBHelper helper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        db = helper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        switch (matcher.match(uri)) {
            case CONTACTS:
                return db.query("contacts", projection, selection,
                        selectionArgs, null, null, sortOrder);

            case CONTACT_ID:
                long id = ContentUris.parseId(uri);
                return db.query("contacts", projection, "_id=?",
                        new String[]{String.valueOf(id)}, null, null, sortOrder);

            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert("contacts", null, values);
        if (rowID > 0) {
            return ContentUris.withAppendedId(CONTENT_URI, rowID);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case CONTACTS:
                return db.delete("contacts", selection, selectionArgs);
            case CONTACT_ID:
                long id = ContentUris.parseId(uri);
                return db.delete("contacts", "_id=?", new String[]{String.valueOf(id)});
            default:
                return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        switch (matcher.match(uri)) {
            case CONTACTS:
                return db.update("contacts", values, selection, selectionArgs);
            case CONTACT_ID:
                long id = ContentUris.parseId(uri);
                return db.update("contacts", values, "_id=?", new String[]{String.valueOf(id)});
            default:
                return 0;
        }
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.example.contacts";
    }
}
