package com.example.schedulertracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TBL_COMMENTS = "comments";
    public static final String COL_COMMENT_ID = "commentID";
    public static final String COL_COMMENT = "comment";

    //Begin Term DB items
    public static final String TBL_TERMS = "terms";
    public static final String COL_TERM_ID = "termID";
    public static final String COL_TERM_TITLE = "title";
    public static final String COL_TERM_START_DATE = "startDate";
    public static final String COL_TERM_END_DATE = "endDate";
    //End Term DB items

    private static final String DB_NAME = "schedulertracker.db";
    private static final int DB_VERSION = 1;

    // Database creation sql statement
    private static final String DB_CREATE = "create table "
            + TBL_COMMENTS + "( " + COL_COMMENT_ID
            + " integer primary key autoincrement, " + COL_COMMENT
            + " text not null);"
            ;
    private static final String DB_TERMS_CREATE =
            //Begin Term create
            " create table "
                    + TBL_TERMS + "( " + COL_TERM_ID + " integer primary key autoincrement, "
            + COL_TERM_TITLE + " text not null, "
            + COL_TERM_START_DATE + " string, "
            + COL_TERM_END_DATE + " string" +
            ");";
            //End Term create

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
        db.execSQL(DB_TERMS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_TERMS);

        onCreate(db);
    }
}
