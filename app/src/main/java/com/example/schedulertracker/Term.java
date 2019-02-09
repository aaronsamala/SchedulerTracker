package com.example.schedulertracker;

import java.util.ArrayList;

public class Term {
    String title;
    Integer termID, startDate, endDate;
    ArrayList<Course> courses;
    public Term(){
        this.title = "";
        this.termID = 0;
        this.startDate = 0;
        this.endDate = 0;
        this.courses = new ArrayList<Course>();
    }
    public Term(String title, Integer termID, Integer startDate, Integer endDate, ArrayList<Course> courses){
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

    public Integer getTermID() {
        return termID;
    }

    public void setTermID(Integer termID) {
        this.termID = termID;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
