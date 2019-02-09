package com.example.schedulertracker;

public class CourseMentor {
    Integer courseMentorID, courseID;
    String name, phoneNumber, emailAddress;

    public CourseMentor(){
        this.courseMentorID=0;
        this.name="";
        this.phoneNumber="";
        this.emailAddress="";
    }

    public CourseMentor(Integer courseMentorID, Integer courseID, String name, String phoneNumber, String emailAddress){
        this.courseMentorID=courseMentorID;
        this.courseID = courseID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public Integer getCourseMentorID() {
        return courseMentorID;
    }

    public void setCourseMentorID(Integer courseMentorID) {
        this.courseMentorID = courseMentorID;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
