package com.example.schedulertracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TBL_COMMENTS = "comments";
    public static final String COL_COMMENTS_ID = "_id";
    public static final String COL_COMMENT = "comment";


    private static final String DB_NAME = "schedulertracker.db";
    private static final int DB_VERSION = 1;

    // Database creation sql statement
    private static final String DB_CREATE = "create table "
            + TBL_COMMENTS + "( " + COL_COMMENTS_ID
            + " integer primary key autoincrement, " + COL_COMMENT
            + " text not null);" +
            ""
            ;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COMMENTS);
        onCreate(db);
    }
}
