package com.example.schedulertracker;

public class Assessment {
    Integer assessmentID, dueDate, goalDate, courseID;
    String title, assessmentType;

    public Assessment(){
        this.assessmentID = 0;
        this.dueDate = 0;
        this.goalDate = 0;
        this.courseID = 0;
        this.title = "";
        this.assessmentType = "";
    }

    public Assessment(Integer assessmentID, Integer dueDate, Integer goalDate, String title, String assessmentType, Integer courseID){
        this.assessmentID = assessmentID;
        this.dueDate = dueDate;
        this.goalDate = goalDate;
        this.courseID = courseID;
        this.title = title;
        this.assessmentType = assessmentType;
    }

    public Integer getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(Integer assessmentID) {
        this.assessmentID = assessmentID;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(Integer goalDate) {
        this.goalDate = goalDate;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
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
