package com.example.schedulertracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class DBDataSource {

    // Database fields
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private String[] allCommentCols = { DBHelper.COL_COMMENT_ID,
            DBHelper.COL_COMMENT};


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
}
