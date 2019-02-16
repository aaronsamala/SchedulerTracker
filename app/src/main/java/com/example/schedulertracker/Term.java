package com.example.schedulertracker;

import java.util.ArrayList;

public class Term {
    String title;
    String startDate;
    String endDate;
    Long termID;
    ArrayList<Course> courses;
    public Term(){
        this.title = "";
        this.termID = 0L;
        this.startDate = "";
        this.endDate = "";
        this.courses = new ArrayList<Course>();
    }
    public Term(String title, Long termID, String startDate, String endDate, ArrayList<Course> courses){
        this.title = title;
        this.termID = termID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courses = courses;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTermID() {
        return termID;
    }

    public void setTermID(Long termID) {
        this.termID = termID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
