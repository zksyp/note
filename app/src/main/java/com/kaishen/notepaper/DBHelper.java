package com.kaishen.notepaper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kaishen on 16/6/30.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_NOTE_LIST = "note_list";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIME = "time";
    public static final String DATABASE_NAME = "note.db";
    public static final int DATABASE_VERSION = 1;

    private static DBHelper dbHelper;

    private static final String DATABASE_CREATE_NOTE
            ="CREATE TABLE " + TABLE_NAME_NOTE_LIST + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CONTENT + " VARCHAR(4000), " +
            COLUMN_TIME + " DATE);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getDbHelper(Context context)
    {
        if(dbHelper == null)
        {
            synchronized (DBHelper.class){
                if(dbHelper == null){
                    dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME_NOTE_LIST);
        onCreate(db);
    }
}
