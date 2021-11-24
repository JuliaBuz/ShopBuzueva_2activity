package com.example.magaz;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUsers extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2 ;
    public static final String DATABASE_NAME = "authorizdb";
    public static final String TABLE_CONTACTS = "user";

    public static final String KEY_ID = "_id";
    public static final String KEY_USERNAME = "login";
    public static final String KEY_PASSWORD = "parol";
    public DBUsers(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_USERNAME + " text,"  + KEY_PASSWORD + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);

        onCreate(db);

    }
}


