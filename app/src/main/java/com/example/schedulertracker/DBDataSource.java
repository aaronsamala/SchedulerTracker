package com.example.schedulertracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBDataSource {

    // Database fields
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private String[] allNoteCols = {
            DBHelper.COL_NOTE_ID,
            DBHelper.COL_NOTE_COURSE_ID,
            DBHelper.COL_NOTE
    };

    //Begin Term add
    private String[] allTermCols = {
            DBHelper.COL_TERM_ID,
            DBHelper.COL_TERM_TITLE,
            DBHelper.COL_TERM_START_DATE,
            DBHelper.COL_TERM_END_DATE
    };

    //Begin Term add
    private String[] allCourseCols = {
            DBHelper.COL_COURSE_ID,
            DBHelper.COL_COURSE_TERM_ID,
            DBHelper.COL_COURSE_START_DATE,
            DBHelper.COL_COURSE_END_DATE,
            DBHelper.COL_COURSE_TITLE,
            DBHelper.COL_COURSE_STATUS
    };




    public DBDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
 
    public Note createNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_NOTE, note);
        long insertId = db.insert(DBHelper.TBL_NOTES, null,
                values);
        Cursor cursor = db.query(DBHelper.TBL_NOTES,
                allNoteCols, DBHelper.COL_NOTE_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
        //GitHubTest
    }


    public void deleteNote(Note note) {
        long id = note.getNoteID();
        System.out.println("Note deleted with id: " + id);
        db.delete(DBHelper.TBL_NOTES, DBHelper.COL_NOTE_ID
                + " = " + id, null);
    }


    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<Note>();

        Cursor cursor = db.query(DBHelper.TBL_NOTES,
                allNoteCols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = cursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return notes;
    }

    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setNoteID(cursor.getLong(0));
        note.setNote(cursor.getString(1));
        return note;
    }
    //Add Term items
    public Term createTerm(String termTitle, String startDate, String endDate) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_TERM_TITLE, termTitle);
        values.put(DBHelper.COL_TERM_START_DATE, startDate);
        values.put(DBHelper.COL_TERM_END_DATE, endDate);
        long insertId = db.insert(DBHelper.TBL_TERMS, null,
                values);
        Cursor cursor = db.query(DBHelper.TBL_TERMS,
                allTermCols, DBHelper.COL_TERM_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Term newTerm = cursorToTerm(cursor);
        cursor.close();
        return newTerm;
    }
    public Term modTerm(Integer termID, String termTitle, String startDate, String endDate) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_TERM_TITLE, termTitle);
        values.put(DBHelper.COL_TERM_START_DATE, startDate);
        values.put(DBHelper.COL_TERM_END_DATE, endDate);
        values.put(DBHelper.COL_TERM_ID, termID);
        db.update(DBHelper.TBL_TERMS, values, DBHelper.COL_TERM_ID
                + "=" + termID, null);
        /*
        long insertId = db.insert(DBHelper.TBL_TERMS, null,
                values);
        Cursor cursor = db.query(DBHelper.TBL_TERMS,
                allTermCols, DBHelper.COL_TERM_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Term newTerm = cursorToTerm(cursor);
        cursor.close();
        */
        Term newTerm = new Term();
        return newTerm;
    }

    public void deleteTerm(Long id) {
        //long id = note.getNoteID();
        System.out.println("Term deleted with id: " + id);
        db.delete(DBHelper.TBL_TERMS, DBHelper.COL_TERM_ID
                + " = " + id, null);
    }

    public void deleteTerm(Term term) {
        Log.d("DeleteTest",term.getTermID().toString());
        long id = term.getTermID();
        System.out.println("Term deleted with id: " + id);
        db.delete(DBHelper.TBL_TERMS, DBHelper.COL_TERM_ID
                + " = " + id, null);
    }

    public List<Term> getAllTerms() {
        List<Term> terms = new ArrayList<Term>();

        Cursor cursor = db.query(DBHelper.TBL_TERMS,
                allTermCols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Term term = cursorToTerm(cursor);
            terms.add(term);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return terms;
    }

    private Term cursorToTerm(Cursor cursor) {
        Term term = new Term();
        term.setTermID(cursor.getLong(0));
        term.setTitle(cursor.getString(1));
        term.setStartDate(cursor.getString(2));
        term.setEndDate(cursor.getString(3));
        return term;
    }
    //End add Term items

    /*
        public static final String COL_COURSE_ID = "courseID";
    public static final String COL_COURSE_TERM_ID = "course_TermID";
    public static final String COL_COURSE_START_DATE = "startDate";
    public static final String COL_COURSE_END_DATE = "endDate";
    public static final String COL_COURSE_TITLE = "title";
    public static final String COL_COURSE_STATUS = "status";
     */
    //Add Course items
    public Course createCourse(Long termID, String courseTitle, String startDate, String endDate, String status) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_COURSE_TERM_ID, termID);
        values.put(DBHelper.COL_COURSE_START_DATE, startDate);
        values.put(DBHelper.COL_COURSE_END_DATE, endDate);
        values.put(DBHelper.COL_COURSE_TITLE, courseTitle);
        values.put(DBHelper.COL_COURSE_STATUS, status);
        long insertId = db.insert(DBHelper.TBL_COURSES, null,
                values);
        Cursor cursor = db.query(DBHelper.TBL_COURSES,
                allTermCols, DBHelper.COL_COURSE_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Course newCourse = cursorToCourse(cursor);
        cursor.close();
        return newCourse;
    }

    public Course modCourse(Integer courseID, Integer termID, String courseTitle, String startDate, String endDate, String status) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_COURSE_ID, courseID);
        values.put(DBHelper.COL_COURSE_TERM_ID, termID);
        values.put(DBHelper.COL_COURSE_START_DATE, startDate);
        values.put(DBHelper.COL_COURSE_END_DATE, endDate);
        values.put(DBHelper.COL_COURSE_TITLE, courseTitle);
        values.put(DBHelper.COL_COURSE_STATUS, status);

        db.update(DBHelper.TBL_COURSES, values, DBHelper.COL_COURSE_ID
                + "=" + courseID, null);


        Course newCourse = new Course();
        return newCourse;
    }

    public void deleteCourse(Long id) {
        System.out.println("Course deleted with id: " + id);
        db.delete(DBHelper.TBL_COURSES, DBHelper.COL_COURSE_ID
                + " = " + id, null);
    }
    /*




    public void deleteTerm(Term term) {
        Log.d("DeleteTest",term.getTermID().toString());
        long id = term.getTermID();
        System.out.println("Term deleted with id: " + id);
        db.delete(DBHelper.TBL_TERMS, DBHelper.COL_TERM_ID
                + " = " + id, null);
    }




     */


    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<Course>();

        Cursor cursor = db.query(DBHelper.TBL_COURSES,
                allCourseCols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Course course = cursorToCourse(cursor);
            courses.add(course);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return courses;
    }

    private Course cursorToCourse(Cursor cursor) {
        Course course = new Course();
        course.setCourseID(cursor.getLong(0));
        course.setTermID(cursor.getLong(1));
        course.setStartDate(cursor.getString(2));
        course.setAnticipatedEndDate(cursor.getString(3));
        course.setTitle(cursor.getString(1));
        course.setStatus(cursor.getString(1));
        return course;
    }
//End add Term items
}
