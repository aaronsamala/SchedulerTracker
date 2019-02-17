package com.example.schedulertracker;

public class Assessment {
    Long assessmentID, courseID;
    String title, assessmentType, dueDate, goalDate;

    public Assessment(){
        this.assessmentID = 0L;
        this.dueDate = "";
        this.goalDate = "";
        this.courseID = 0L;
        this.title = "";
        this.assessmentType = "";
    }

    public Assessment(Long assessmentID, String dueDate, String goalDate, String title, String assessmentType, Long courseID){
        this.assessmentID = assessmentID;
        this.dueDate = dueDate;
        this.goalDate = goalDate;
        this.courseID = courseID;
        this.title = title;
        this.assessmentType = assessmentType;
    }

    public Long getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(Long assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String goalDate) {
        this.goalDate = goalDate;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }
}
