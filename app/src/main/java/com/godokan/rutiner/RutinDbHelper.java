package com.godokan.rutiner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RutinDbHelper extends SQLiteOpenHelper {
    private static RutinDbHelper rutinDbHelper = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Rutiner.db";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS "+ TableInfo.TABLE_NAME + " (" +
                    TableInfo.COLUMN_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TableInfo.COLUMN_NAME_TYPE+" TEXT NOT NULL, " +
                    TableInfo.COLUMN_NAME_NAME+" TEXT, " +
                    TableInfo.COLUMN_NAME_CONTEXT+" TEXT, " +
                    TableInfo.COLUMN_NAME_DATE+" TEXT, " +
                    TableInfo.COLUMN_NAME_FLAG+" TEXT)";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TableInfo.TABLE_NAME;

    public RutinDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static RutinDbHelper getInstance(Context context) {
        if(rutinDbHelper == null) rutinDbHelper = new RutinDbHelper(context);
        return rutinDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(SQL_CREATE_TABLE); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
