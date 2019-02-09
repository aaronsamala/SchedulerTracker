package com.example.schedulertracker;

import java.util.ArrayList;

public class Course {
    ArrayList<Assessment> assessments;
    ArrayList<CourseMentor> courseMentors;
    Integer courseID, termID, startDate, anticipatedEndDate;
    String title, status, notes;

    public Course(){
        this.assessments = new ArrayList<>();
        this.courseMentors = new ArrayList<>();
        this.courseID = 0;
        this.termID = 0;
        this.startDate = 0;
        this.anticipatedEndDate = 0;
        this.title = "";
        this.status = "";
        this.notes = "";
    }
    public Course(ArrayList assessments, ArrayList courseMentors, Integer courseID, Integer termID, Integer startDate, Integer anticipatedEndDate,
     String title, String status, String notes){
        this.assessments = assessments;
        this.courseMentors = courseMentors;
        this.courseID = courseID;
        this.termID = termID;
        this.startDate = startDate;
        this.anticipatedEndDate = anticipatedEndDate;
        this.title = title;
        this.status = status;
        this.notes = notes;
    }

    public ArrayList<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(ArrayList<Assessment> assessments) {
        this.assessments = assessments;
    }

    public ArrayList<CourseMentor> getCourseMentors() {
        return courseMentors;
    }

    public void setCourseMentors(ArrayList<CourseMentor> courseMentors) {
        this.courseMentors = courseMentors;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
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

    public Integer getAnticipatedEndDate() {
        return anticipatedEndDate;
    }

    public void setAnticipatedEndDate(Integer anticipatedEndDate) {
        this.anticipatedEndDate = anticipatedEndDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

