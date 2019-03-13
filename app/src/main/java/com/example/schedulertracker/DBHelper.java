package com.example.schedulertracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TBL_NOTES = "notes";
    public static final String COL_NOTE_ID = "noteID";
    public static final String COL_NOTE_COURSE_ID = "note_CourseID";
    public static final String COL_NOTE = "note";

    //Begin Term DB items
    public static final String TBL_TERMS = "terms";
    public static final String COL_TERM_ID = "termID";
    public static final String COL_TERM_TITLE = "title";
    public static final String COL_TERM_START_DATE = "startDate";
    public static final String COL_TERM_END_DATE = "endDate";
    //End Term DB items

  //Begin Course DB items
    public static final String TBL_COURSES = "courses";
    public static final String COL_COURSE_ID = "courseID";
    public static final String COL_COURSE_TERM_ID = "course_TermID";
    public static final String COL_COURSE_START_DATE = "startDate";
    public static final String COL_COURSE_END_DATE = "endDate";
    public static final String COL_COURSE_TITLE = "title";
    public static final String COL_COURSE_STATUS = "status";
    //End Course DB items

    //Begin CourseMentor DB items
    public static final String TBL_COURSE_MENTORS = "courseMentors";
    public static final String COL_COURSE_MENTOR_ID = "courseMentorID";
    public static final String COL_COURSE_MENTOR_COURSE_ID = "courseMentor_CourseID";
    public static final String COL_COURSE_MENTOR_NAME = "courseMentorName";
    public static final String COL_COURSE_MENTOR_PHONE_NUMBER = "courseMentorPhoneNumber";
    public static final String COL_COURSE_MENTOR_EMAIL_ADDRESS = "courseMentorEmailAddress";
    //End CourseMentor DB items

    //Begin Assessment DB items
    public static final String TBL_ASSESSMENTS = "assessments";
    public static final String COL_ASSESSMENT_ID = "assessmentID";
    public static final String COL_ASSESSMENT_COURSE_ID = "assessment_CourseID";
    public static final String COL_TITLE = "title";
    public static final String COL_TYPE = "type";
    public static final String COL_DUE_DATE = "dueDate";
    public static final String COL_GOAL_DATE = "goalDate";
    //End Assessment DB items
    
    private static final String DB_NAME = "schedulertracker.db";
    private static final int DB_VERSION = 2;

    // Database creation sql statement
    private static final String DB_NOTES_CREATE = "create table "
            + TBL_NOTES + "( " + COL_NOTE_ID
            + " integer primary key autoincrement, "
            + COL_NOTE_COURSE_ID + " integer not null,"
            + COL_NOTE + " text not null)"
            ;
    private static final String DB_TERMS_CREATE =
            //Begin Term create
            " create table "
                    + TBL_TERMS + "( " + COL_TERM_ID + " integer primary key autoincrement, "
            + COL_TERM_TITLE + " string not null, "
            + COL_TERM_START_DATE + " string, "
            + COL_TERM_END_DATE + " string" +
            ")";
            //End Term create

    private static final String DB_COURSES_CREATE =
            //Begin Course create
            " create table "
                    + TBL_COURSES + "( " + COL_COURSE_ID + " integer primary key autoincrement, "
                    + COL_COURSE_TERM_ID + " integer, "
                    + COL_COURSE_START_DATE + " string, "
                    + COL_COURSE_END_DATE + " string, "
                    + COL_COURSE_TITLE + " string, "
                    + COL_COURSE_STATUS + " string"
                    + ")";
            //End Course create

    private static final String DB_COURSE_MENTORS_CREATE =
            //Begin CourseMentors create
            " create table "
                    + TBL_COURSE_MENTORS + "( " + COL_COURSE_MENTOR_ID + " integer primary key autoincrement, "
                    + COL_COURSE_MENTOR_COURSE_ID + " integer, "
                    + COL_COURSE_MENTOR_NAME + " string, "
                    + COL_COURSE_MENTOR_PHONE_NUMBER + " string, "
                    + COL_COURSE_MENTOR_EMAIL_ADDRESS + " string "
                    + ")";
            //End CourseMentors create
    
    private static final String DB_ASSESSMENTS_CREATE =
            //Begin CourseMentors create
            " create table "
                    + TBL_ASSESSMENTS + "( " + COL_ASSESSMENT_ID + " integer primary key autoincrement, "
                    + COL_ASSESSMENT_COURSE_ID + " integer, "
                    + COL_TITLE + " string, "
                    + COL_TYPE + " string, "
                    + COL_DUE_DATE + " string, "
                    + COL_GOAL_DATE + " string "
                    + ")";
            //End CourseMentors create
    
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public void addExtraTables(SQLiteDatabase db){
        db.execSQL(DB_TERMS_CREATE);

        db.execSQL(DB_COURSES_CREATE);
        db.execSQL(DB_TERMS_CREATE);
        db.execSQL(DB_COURSE_MENTORS_CREATE);
        db.execSQL(DB_ASSESSMENTS_CREATE);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DB_NOTES_CREATE);
        db.execSQL(DB_TERMS_CREATE);
        db.execSQL(DB_COURSES_CREATE);
        db.execSQL(DB_COURSE_MENTORS_CREATE);
        db.execSQL(DB_ASSESSMENTS_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSE_MENTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ASSESSMENTS);

        onCreate(db);
    }
}
