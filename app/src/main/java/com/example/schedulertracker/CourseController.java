package com.example.schedulertracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class CourseController  extends AppCompatActivity {
    private Button btnCancel;
    private Button btnSave;
    DBDataSource datasource;
    Long termID;
    boolean isNew = true;
    List<Course> courseValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        datasource = new DBDataSource(this);
        datasource.open();

        courseValues = datasource.getAllCourses();
        for (Course course : courseValues){
            String tmp = course.getTitle() + ", " + course.getTermID() + ", " + course.getStartDate() + ", " + course.getAnticipatedEndDate();
            Log.d("CourseListTest",tmp);
        }

        Log.d("ListCourseEnd","End Of Course List Test");
        Intent intent = getIntent();

        isNew = intent.getBooleanExtra("isNew", true);
        termID = intent.getLongExtra("TermID", 0l);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCourse();

            }
        });
    }

    void addCourse(){

        /*
        Long termID,
                           String courseTitle,
                           String startDate,
                           String endDate,
                           String status)
         */
        //editCourseTitle
        //editStartDate
        //editEndDate
        //editCourseStatus
        EditText edit = (EditText) findViewById(R.id.editCourseTitle);
        String tmpCourseTitle = edit.getText().toString();
        edit = (EditText) findViewById(R.id.editStartDate);
        String tmpStartDate = edit.getText().toString();
        edit = (EditText) findViewById(R.id.editEndDate);
        String tmpEndDate = edit.getText().toString();
        edit = (EditText) findViewById(R.id.editCourseStatus);
        String tmpCourseStatus = edit.getText().toString();


        datasource.createCourse(
                termID,
                tmpCourseTitle,
                tmpStartDate,
                tmpEndDate,
                tmpCourseStatus
        );
    }
}
