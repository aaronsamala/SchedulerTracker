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
    private String[] allCommentCols = {
            DBHelper.COL_COMMENT_ID,
            DBHelper.COL_COMMENT
    };

    //Begin Term add
    private String[] allTermCols = {
            DBHelper.COL_TERM_ID,
            DBHelper.COL_TERM_TITLE,
            DBHelper.COL_TERM_START_DATE,
            DBHelper.COL_TERM_END_DATE
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

    public Comment createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_COMMENT, comment);
        long insertId = db.insert(DBHelper.TBL_COMMENTS, null,
                values);
        Cursor cursor = db.query(DBHelper.TBL_COMMENTS,
                allCommentCols, DBHelper.COL_COMMENT_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        db.delete(DBHelper.TBL_COMMENTS, DBHelper.COL_COMMENT_ID
                + " = " + id, null);
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<Comment>();

        Cursor cursor = db.query(DBHelper.TBL_COMMENTS,
                allCommentCols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        return comment;
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
}
