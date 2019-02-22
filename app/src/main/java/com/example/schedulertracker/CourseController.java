package com.example.schedulertracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class CourseController  extends AppCompatActivity {
    private Button btnCancel;
    private Button btnSave;
    DBDataSource datasource;
    Long termID;
    Long courseID;
    boolean isNew = true;
    List<Course> courseValues;
    //ListView courseListView;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        datasource = new DBDataSource(this);
        datasource.open();

        courseValues = datasource.getAllCourses();
        for (Course course : courseValues){
            String tmp = course.getTitle() + ", " + course.getTermID() + ", " + course.getCourseID() + ", " + course.getAnticipatedEndDate();
            Log.d("CourseListTest",tmp);
        }

        Log.d("ListCourseEnd","End Of Course List Test");
        Intent intent = getIntent();

        isNew = intent.getBooleanExtra("isNew", true);

        termID = intent.getLongExtra("TermID", 0L);
        if (!isNew){

            courseID = intent.getLongExtra("courseID", 0L);
            for (Course course : courseValues){
                if (course.getCourseID().equals(courseID)){
                    /*
                    EditText edit = (EditText)findViewById(R.id.editTermName);
            edit.setText(intent.getStringExtra("title"));
            edit = (EditText)findViewById(R.id.editStartDate);
            edit.setText(intent.getStringExtra("startDate"));
            edit = (EditText)findViewById(R.id.editEndDate);
            edit.setText(intent.getStringExtra("endDate"));
                    //editCourseTitle
        //editStartDate
        //editEndDate
        //editCourseStatus
                     */
                    EditText edit = (EditText)findViewById(R.id.editCourseTitle);
                    edit.setText(course.getTitle());
                    edit = (EditText)findViewById(R.id.editEndDate);
                    edit.setText(course.getTitle());
                    edit = (EditText)findViewById(R.id.editCourseStatus);
                    edit.setText(course.getTitle());
                    edit = (EditText)findViewById(R.id.editStartDate);
                    edit.setText(course.getStartDate());
                    termID = course.getTermID();
                    Log.d("TermID_ONCOURSELOAD", termID.toString());
                }
            }
        }




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
        if (isNew) {
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
        } else if (!isNew){
            EditText edit = (EditText) findViewById(R.id.editCourseTitle);
            String tmpCourseTitle = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editStartDate);
            String tmpStartDate = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editEndDate);
            String tmpEndDate = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editCourseStatus);
            String tmpCourseStatus = edit.getText().toString();

            /*
            (Integer courseID,
                        Integer termID,
                        String courseTitle,
                        String startDate,
                        String endDate,
                        String status
             */
            String tmpCourse = courseID.toString();
            Integer tmpCourseInt = Integer.valueOf(tmpCourse);
            String tmpString = termID.toString();
            Integer tmpInteger = Integer.valueOf(tmpString);
            datasource.modCourse(
                    tmpCourseInt,
                    tmpInteger,
                    tmpCourseTitle,
                    tmpStartDate,
                    tmpEndDate,
                    tmpCourseStatus
            );

        }
    }
}
