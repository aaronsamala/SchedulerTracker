package com.example.schedulertracker;

import java.util.ArrayList;

public class Course {
    ArrayList<Assessment> assessments;
    ArrayList<CourseMentor> courseMentors;
    Long courseID;
    Long termID;
    String startDate;
    String anticipatedEndDate;
    String title, status, notes;

    public Course(){
        this.assessments = new ArrayList<>();
        this.courseMentors = new ArrayList<>();
        this.courseID = 0L;
        this.termID = 0L;
        this.startDate = "";
        this.anticipatedEndDate = "";
        this.title = "";
        this.status = "";
        this.notes = "";
    }
    public Course(ArrayList assessments, ArrayList courseMentors, Long courseID, Long termID, String startDate, String anticipatedEndDate,
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

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
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

    public String getAnticipatedEndDate() {
        return anticipatedEndDate;
    }

    public void setAnticipatedEndDate(String anticipatedEndDate) {
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

