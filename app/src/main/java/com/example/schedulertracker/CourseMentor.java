package com.example.schedulertracker;

public class CourseMentor {
    Long courseMentorID;
    Long courseID;
    String name, phoneNumber, emailAddress;

    public CourseMentor(){
    	this.courseMentorID=0L;
    	this.courseID=0L;
        this.name="";
        this.phoneNumber="";
        this.emailAddress="";
    }

    public CourseMentor(Long courseMentorID, Long courseID, String name, String phoneNumber, String emailAddress){
        this.courseMentorID=courseMentorID;
        this.courseID = courseID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public Long getCourseMentorID() {
        return courseMentorID;
    }

    public void setCourseMentorID(Long courseMentorID) {
        this.courseMentorID = courseMentorID;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
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
