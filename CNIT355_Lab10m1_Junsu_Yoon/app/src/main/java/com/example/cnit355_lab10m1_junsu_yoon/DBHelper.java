package com.example.cnit355_lab10m1_junsu_yoon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "lab10.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COL_ID = "_id";     // PK
    public static final String COL_NAME = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_EMAIL = "email";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 4 columns: _id, name, phone, email (컬럼 구성은 예시 – 추측입니다)
        String sql = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_PHONE + " TEXT, " +
                COL_EMAIL + " TEXT" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simple strategy: drop and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // INSERT
    public long insertContact(String name, String phone, String email) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_EMAIL, email);
        return db.insert(TABLE_CONTACTS, null, values);
    }

    // UPDATE by id
    public int updateContact(long id, String name, String phone, String email) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_EMAIL, email);
        String where = COL_ID + "=?";
        String[] args = { String.valueOf(id) };
        return db.update(TABLE_CONTACTS, values, where, args);
    }

    // DELETE by id
    public int deleteContact(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String where = COL_ID + "=?";
        String[] args = { String.valueOf(id) };
        return db.delete(TABLE_CONTACTS, where, args);
    }

    // SELECT all → Cursor
    public Cursor getAllContacts() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(
                TABLE_CONTACTS,
                null,   // all columns
                null, null, null, null,
                COL_ID + " ASC"
        );
    }
}
