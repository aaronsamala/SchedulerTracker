package com.example.schedulertracker;



public class Note {
    private long noteID;
    private long note_TermID;
    private String note;

    public long getNoteID() {
        return noteID;
    }

    public void setNoteID(long noteID) {
        this.noteID = noteID;
    }

    public long getNote_TermID() {
        return note_TermID;
    }

    public void setNote_TermID(long note_TermID) {
        this.note_TermID = note_TermID;
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
