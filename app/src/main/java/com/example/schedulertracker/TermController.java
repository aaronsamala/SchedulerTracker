package com.example.schedulertracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class TermController  extends AppCompatActivity {
    private Button btnCancel;
    private Button btnSubmit;
    private Button btnDelete;
    MainActivity mainActivity;
    boolean isNew = true;
    Long termID;
    int position;

    private Button btnAddCourse;
    private DBDataSource datasource;
    List<Course> courseValues;
    CourseController courseController;
    Course courseToPass = new Course();
    private final static int COURSE_REQUEST_CODE = 2;
    ListView courseListView;
    String[] courseArray;
    ArrayAdapter<String> courseArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        //mainActivity = new MainActivity();
        //mainActivity.setTermController(this);
        //datasource = new DBDataSource(this);
        //datasource.open();


        Intent intent = getIntent();

        isNew = intent.getBooleanExtra("isNew", true);

        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { deleteTerm(); }
        });
        btnDelete.setEnabled(false);

        if (!isNew){
            EditText edit = (EditText)findViewById(R.id.editTermName);
            edit.setText(intent.getStringExtra("title"));
            edit = (EditText)findViewById(R.id.editStartDate);
            edit.setText(intent.getStringExtra("startDate"));
            edit = (EditText)findViewById(R.id.editEndDate);
            edit.setText(intent.getStringExtra("endDate"));
            termID = intent.getLongExtra("termID",999);
            Log.d("TermID_ONLOAD", termID.toString());
            position = intent.getIntExtra("position",999);
            btnDelete.setEnabled(true);


            //MainActivity getContext = (MainActivity)getApplicationContext();
            //getCallingActivity();
            //datasource = getContext.getDatasource();
            datasource = new DBDataSource(this);
            datasource.open();
            courseValues = datasource.getAllCoursesForTerm(termID);

            //filter for only the applicable courses
            //filterCourses();
            setCourseList();
            btnAddCourse = (Button) findViewById(R.id.btnAddCourse);

            btnAddCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { btnAddCourse(); }
            });
        }

        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addTerm();

            }
        });





        if (courseValues!=null){



            courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                LinearLayout ll = (LinearLayout) view; // get the parent layout view
                TextView tv = (TextView) ll.findViewById(R.id.textView); // get the child text view
                //final String item = tv.getText().toString() + " int position= " + position;
                //final String item = tv.getText().toString() + " int position= " + position;

                Course tmpCourse = (Course) courseValues.get(position);
                final String item = tmpCourse.getTitle() + " - " + tmpCourse.getTermID();
                //String item = ((TextView)view).getText().toString();

                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT).show();
                launchCourseScreen(tmpCourse, position);


                }

            });
        }
    }
    private void launchCourseScreen(Course course, int position) {
        Intent intent = new Intent(this, CourseController.class);
        intent.putExtra("isNew", false);
        //intent.putExtra("title", term.getTitle());
        intent.putExtra("courseID", course.getCourseID());
        intent.putExtra("position", position);
        startActivityForResult(intent, COURSE_REQUEST_CODE);

    }
    void filterCourses(){

        if (!courseValues.isEmpty()) {
            /*
            for (Course course : courseValues) {
                if (!course.getTermID().toString().equals(termID.toString())) {
                    courseValues.remove(course);
                }*/
            int tmpInt = 0;
            Log.d("TermID", termID.toString());
            Log.i("TermID_courseValue.size", String.valueOf(courseValues.size()));
            for(int i = 0; i >= courseValues.size()-1; i++)
                tmpInt = i;
                Log.d("TermID_FOR", courseValues.get(tmpInt).getTermID().toString());
                if (!courseValues.get(tmpInt).getTermID().toString().equals(termID.toString())) {
                    courseValues.remove(tmpInt);
                    Log.d("TermID_Removing: ", courseValues.get(tmpInt).getTitle().toString());
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        Log.d("ActResult", "Started");
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case COURSE_REQUEST_CODE:
                //filterCourses();
                courseValues = datasource.getAllCoursesForTerm(termID);
                setCourseList();
        }
    }

    void setCourseList(){
        courseArray = new String[courseValues.size()];
        for(int i = 0; i < courseValues.size(); i++) courseArray[i] = courseValues.get(i).getTitle();

        courseListView = (ListView)findViewById(R.id.listCourses);
        courseArrayAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview, R.id.textView, courseArray);
        courseListView.setAdapter(courseArrayAdapter);
    }
    void btnAddCourse(){

        Intent intent = new Intent(this, CourseController.class);
        intent.putExtra("isNew", true);
        intent.putExtra("TermID", termID);
        startActivityForResult(intent, COURSE_REQUEST_CODE);
    }
    void deleteTerm(){
        Log.d("TermController", "addTermStart_!isNew_delete");
        Intent intent = new Intent();
        intent.putExtra("isNew", false);
        intent.putExtra("termID", termID);
        intent.putExtra("position", position);
        intent.putExtra("action", "delete");
        setResult(RESULT_OK, intent);
        finish();
    }
    void addTerm(){
        if (isNew) {
            Log.d("TermController", "addTermStart");
            Intent intent = new Intent();
            intent.putExtra("isNew", true);
            EditText edit = (EditText) findViewById(R.id.editTermName);
            intent.putExtra("termTitle", edit.getText().toString());
            edit = (EditText) findViewById(R.id.editStartDate);
            intent.putExtra("termStartDate", edit.getText().toString());
            edit = (EditText) findViewById(R.id.editEndDate);
            intent.putExtra("termEndDate", edit.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        } else if (!isNew){
            Log.d("TermController", "addTermStart_!isNew");
            Intent intent = new Intent();
            intent.putExtra("isNew", false);
            EditText edit = (EditText) findViewById(R.id.editTermName);
            intent.putExtra("termTitle", edit.getText().toString());
            edit = (EditText) findViewById(R.id.editStartDate);
            intent.putExtra("termStartDate", edit.getText().toString());
            edit = (EditText) findViewById(R.id.editEndDate);
            intent.putExtra("termEndDate", edit.getText().toString());
            intent.putExtra("termID", termID);
            intent.putExtra("position", position);
            intent.putExtra("action", "modify");
            setResult(RESULT_OK, intent);
            finish();
        }
        /*
        EditText edit = (EditText)findViewById(R.id.editTermName);
        Log.d("TermController", edit.getText().toString());
        Term term = new Term();
        term.setTitle(edit.getText().toString());
        edit = (EditText)findViewById(R.id.editStartDate);
        Log.d("TermController", edit.getText().toString());
        term.setStartDate(edit.getText().toString());
        edit = (EditText)findViewById(R.id.editEndDate);
        Log.d("TermController", edit.getText().toString());
        term.setEndDate(edit.getText().toString());
        */
        /*

        Log.d("TermController", "addTermStart");
        EditText edit = (EditText)findViewById(R.id.editTermName);
        term.setTitle(edit.getText().toString());
        edit = (EditText)findViewById(R.id.editStartDate);
        term.setStartDate(edit.getText().toString());
        edit = (EditText)findViewById(R.id.editEndDate);
        term.setEndDate(edit.getText().toString());
        mainActivity.addTerm(term);
        */
        //finish();
        //String result = edit.getText().toString();
    }
    void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

}
