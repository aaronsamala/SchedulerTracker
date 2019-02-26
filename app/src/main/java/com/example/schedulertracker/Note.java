package com.example.schedulertracker;



public class Note {
    private long noteID;
    private long note_CourseID;
    private String note;

    public long getNoteID() {
        return noteID;
    }

    public void setNoteID(long noteID) {
        this.noteID = noteID;
    }

    public long getNote_CourseID() {
        return note_CourseID;
    }

    public void setNote_CourseID(long note_CourseID) {
        this.note_CourseID = note_CourseID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return note;
    }
}
