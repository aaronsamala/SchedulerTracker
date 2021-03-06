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

    //Begin Course add
    private String[] allCourseCols = {
            DBHelper.COL_COURSE_ID,
            DBHelper.COL_COURSE_TERM_ID,
            DBHelper.COL_COURSE_START_DATE,
            DBHelper.COL_COURSE_END_DATE,
            DBHelper.COL_COURSE_TITLE,
            DBHelper.COL_COURSE_STATUS
    };

    //Begin CourseMentor add
    private String[] allCourseMentorCols = {
            DBHelper.COL_COURSE_MENTOR_ID,
            DBHelper.COL_COURSE_MENTOR_COURSE_ID,
            DBHelper.COL_COURSE_MENTOR_NAME,
            DBHelper.COL_COURSE_MENTOR_PHONE_NUMBER,
            DBHelper.COL_COURSE_MENTOR_EMAIL_ADDRESS,
    };

    //Begin Assessment add
    private String[] allAssessmentCols = {
            DBHelper.COL_ASSESSMENT_ID,
            DBHelper.COL_ASSESSMENT_COURSE_ID,
            DBHelper.COL_TITLE,
            DBHelper.COL_TYPE,
            DBHelper.COL_DUE_DATE,
            DBHelper.COL_GOAL_DATE,
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
 
    public Note createNote(String note, Long courseID) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_NOTE_COURSE_ID, courseID);
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
    }

    public Note modNote(Long id, Long courseID, String note) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_NOTE, note);
        values.put(DBHelper.COL_NOTE_ID, id);
        values.put(DBHelper.COL_NOTE_COURSE_ID, courseID);
        db.update(DBHelper.TBL_NOTES, values, DBHelper.COL_NOTE_ID
                + "=" + id, null);
        Note newNote = getNote(id);
        return newNote;
    }

    public void deleteNote(Note note) {
        long id = note.getNoteID();
        System.out.println("Note deleted with id: " + id);
        db.delete(DBHelper.TBL_NOTES, DBHelper.COL_NOTE_ID
                + " = " + id, null);
    }

    public void deleteNote(Long id) {
        System.out.println("Note deleted with id: " + id);
        db.delete(DBHelper.TBL_NOTES, DBHelper.COL_NOTE_ID
                + " = " + id, null);
    }


    public Note getNote(Long id){
        Cursor cursor = db.query(DBHelper.TBL_NOTES,allNoteCols,DBHelper.COL_NOTE_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
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
        cursor.close();
        return notes;
    }

    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setNoteID(cursor.getLong(0));
        note.setNote_CourseID(cursor.getLong(1));
        note.setNote(cursor.getString(2));
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
                allCourseCols, DBHelper.COL_COURSE_ID + " = " + insertId, null,
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
        cursor.close();
        return courses;
    }

    public List<Course> getAllCoursesForTerm(Long id) {
        List<Course> courses = new ArrayList<Course>();

        Cursor cursor = db.query(DBHelper.TBL_COURSES,
                allCourseCols, DBHelper.COL_COURSE_TERM_ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Course course = cursorToCourse(cursor);
            courses.add(course);
            cursor.moveToNext();
        }
        cursor.close();
        return courses;
    }

    private Course cursorToCourse(Cursor cursor) {
        Course course = new Course();
        course.setCourseID(cursor.getLong(0));
        course.setTermID(cursor.getLong(1));
        course.setStartDate(cursor.getString(2));
        course.setAnticipatedEndDate(cursor.getString(3));
        course.setTitle(cursor.getString(4));
        course.setStatus(cursor.getString(5));
        return course;
    }
    //End add Course items

    //Add CourseMentor items
   public CourseMentor createCourseMentor(Long courseID, String name, String phoneNumber, String emailAddress) {
       ContentValues values = new ContentValues();
       //values.put(DBHelper.COL_COURSE_MENTOR_ID, courseMentorID);
       values.put(DBHelper.COL_COURSE_MENTOR_COURSE_ID, courseID);
       values.put(DBHelper.COL_COURSE_MENTOR_NAME, name);
       values.put(DBHelper.COL_COURSE_MENTOR_PHONE_NUMBER, phoneNumber);
       values.put(DBHelper.COL_COURSE_MENTOR_EMAIL_ADDRESS, emailAddress);
       long insertId = db.insert(DBHelper.TBL_COURSE_MENTORS, null,
               values);
       Cursor cursor = db.query(DBHelper.TBL_COURSE_MENTORS,
               allCourseMentorCols, DBHelper.COL_COURSE_MENTOR_ID + " = " + insertId, null,
               null, null, null);
       cursor.moveToFirst();
       CourseMentor newCourseMentor = cursorToCourseMentor(cursor);
       cursor.close();
       return newCourseMentor;
   }
   
   public CourseMentor modCourseMentor(Long courseMentorID, Long courseID, String name, String phoneNumber, String emailAddress) {
       ContentValues values = new ContentValues();
       values.put(DBHelper.COL_COURSE_MENTOR_ID, courseMentorID);
       values.put(DBHelper.COL_COURSE_MENTOR_COURSE_ID, courseID);
       values.put(DBHelper.COL_COURSE_MENTOR_NAME, name);
       values.put(DBHelper.COL_COURSE_MENTOR_PHONE_NUMBER, phoneNumber);
       values.put(DBHelper.COL_COURSE_MENTOR_EMAIL_ADDRESS, emailAddress);

       db.update(DBHelper.TBL_COURSE_MENTORS, values, DBHelper.COL_COURSE_MENTOR_ID
               + "=" + courseMentorID, null);

       CourseMentor newCourseMentor = new CourseMentor();
       return newCourseMentor;
   }

    public void deleteCourseMentor(Long id) {
        System.out.println("CourseMentor deleted with id: " + id);
        db.delete(DBHelper.TBL_COURSE_MENTORS, DBHelper.COL_COURSE_MENTOR_ID
                + " = " + id, null);
    }

    public List<CourseMentor> getAllCourseMentors() {
        List<CourseMentor> courseMentors = new ArrayList<CourseMentor>();

        Cursor cursor = db.query(DBHelper.TBL_COURSE_MENTORS,
                allCourseMentorCols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CourseMentor courseMentor = cursorToCourseMentor(cursor);
            courseMentors.add(courseMentor);
            cursor.moveToNext();
        }
        cursor.close();
        return courseMentors;
    }
    public CourseMentor getCourseMentor(Long id){

        Cursor cursor = db.query(DBHelper.TBL_COURSE_MENTORS,
                allCourseMentorCols, DBHelper.COL_COURSE_MENTOR_COURSE_ID + " = " + id, null, null, null, null);
        CourseMentor courseMentor = new CourseMentor();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            courseMentor = cursorToCourseMentor(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return courseMentor;

    }

    private CourseMentor cursorToCourseMentor(Cursor cursor) {
        CourseMentor courseMentor = new CourseMentor();
        courseMentor.setCourseMentorID(cursor.getLong(0));
        courseMentor.setCourseID(cursor.getLong(1));
        courseMentor.setName(cursor.getString(2));
        courseMentor.setPhoneNumber(cursor.getString(3));
        courseMentor.setEmailAddress(cursor.getString(4));
        return courseMentor;
    }
    //End add CourseMentor items

    //Add Assessment items
    public Assessment createAssessment(Long courseID, String title, String assessmentType, String dueDate, String goalDate) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_ASSESSMENT_COURSE_ID, courseID);
        values.put(DBHelper.COL_TITLE, title);
        values.put(DBHelper.COL_TYPE, assessmentType);
        values.put(DBHelper.COL_DUE_DATE, dueDate);
        values.put(DBHelper.COL_GOAL_DATE, goalDate);
        long insertId = db.insert(DBHelper.TBL_ASSESSMENTS, null,
                values);
        Cursor cursor = db.query(DBHelper.TBL_ASSESSMENTS,
                allAssessmentCols, DBHelper.COL_ASSESSMENT_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Assessment newAssessment = cursorToAssessment(cursor);
        cursor.close();
        return newAssessment;
    }

    public Assessment modAssessment(Long assessmentID, Long courseID, String title, String assessmentType, String dueDate, String goalDate) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_ASSESSMENT_COURSE_ID, courseID);
        values.put(DBHelper.COL_TITLE, title);
        values.put(DBHelper.COL_TYPE, assessmentType);
        values.put(DBHelper.COL_DUE_DATE, dueDate);
        values.put(DBHelper.COL_GOAL_DATE, goalDate);

        db.update(DBHelper.TBL_ASSESSMENTS, values, DBHelper.COL_ASSESSMENT_ID
                + "=" + assessmentID, null);

        Assessment newAssessment = new Assessment();
        return newAssessment;
    }

    public void deleteAssessment(Long id) {
        System.out.println("Assessment deleted with id: " + id);
        db.delete(DBHelper.TBL_ASSESSMENTS, DBHelper.COL_ASSESSMENT_ID
                + " = " + id, null);
    }

    public List<Assessment> getAllAssessments() {
        List<Assessment> assessments = new ArrayList<Assessment>();

        Cursor cursor = db.query(DBHelper.TBL_ASSESSMENTS,
                allAssessmentCols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Assessment assessment = cursorToAssessment(cursor);
            assessments.add(assessment);
            cursor.moveToNext();
        }
        cursor.close();
        return assessments;
    }
    public List<Assessment> getAllAssessments(Long id) {
        List<Assessment> assessments = new ArrayList<Assessment>();

        Cursor cursor = db.query(DBHelper.TBL_ASSESSMENTS,
                allAssessmentCols, DBHelper.COL_ASSESSMENT_COURSE_ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Assessment assessment = cursorToAssessment(cursor);
            assessments.add(assessment);
            cursor.moveToNext();
        }
        cursor.close();
        return assessments;
    }

    private Assessment cursorToAssessment(Cursor cursor) {
        Assessment assessment = new Assessment();
        assessment.setAssessmentID(cursor.getLong(0));
        assessment.setCourseID(cursor.getLong(1));
        assessment.setTitle(cursor.getString(2));
        assessment.setAssessmentType(cursor.getString(3));
        assessment.setDueDate(cursor.getString(4));
        assessment.setGoalDate(cursor.getString(5));
        return assessment;
    }
    //End add Assessment items

}
