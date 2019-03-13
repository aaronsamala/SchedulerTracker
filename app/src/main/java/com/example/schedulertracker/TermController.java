package com.example.schedulertracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TermController  extends AppCompatActivity {
    MainActivity mainActivity;
    boolean isNew = true;
    Long termID;
    int position;

    private DBDataSource datasource;
    List<Course> courseValues;
    private final static int COURSE_REQUEST_CODE = 2;
    ListView courseListView;
    String[] courseArray;
    ArrayAdapter<String> courseArrayAdapter;
    Calendar calendar;
    EditText termStartDate, termEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();

        calendar= Calendar.getInstance();
        termStartDate = (EditText) findViewById(R.id.editStartDate);

        final DatePickerDialog.OnDateSetListener termStartDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setTermStartDate();
            }

        };

        termStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermController.this, termStartDatePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener termEndDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setTermEndDate();
            }

        };
        termEndDate = (EditText) findViewById(R.id.editEndDate);
        termEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermController.this, termEndDatePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Intent intent = getIntent();

        isNew = intent.getBooleanExtra("isNew", true);

        if (!isNew){
            EditText edit = (EditText)findViewById(R.id.editTermName);
            edit.setText(intent.getStringExtra("title"));
            edit = (EditText)findViewById(R.id.editStartDate);
            edit.setText(intent.getStringExtra("startDate"));
            edit = (EditText)findViewById(R.id.editEndDate);
            edit.setText(intent.getStringExtra("endDate"));
            termID = intent.getLongExtra("termID",999);
            position = intent.getIntExtra("position",999);

            datasource = new DBDataSource(this);
            datasource.open();
            courseValues = datasource.getAllCoursesForTerm(termID);

            setCourseList();
        }

        if (courseValues!=null){

            courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                LinearLayout ll = (LinearLayout) view;
                TextView tv = (TextView) ll.findViewById(R.id.textView);

                Course tmpCourse = (Course) courseValues.get(position);
                final String item = tmpCourse.getTitle() + " - " + tmpCourse.getTermID();
                launchCourseScreen(tmpCourse, position);
                }

            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_term, menu);
        menu.findItem(R.id.save_submit).setVisible(true);
        menu.findItem(R.id.delete_term).setVisible(true);
        menu.findItem(R.id.add_course).setVisible(true);

        menu.findItem(R.id.set_course_alarm).setVisible(false);
        menu.findItem(R.id.send_note).setVisible(false);
        menu.findItem(R.id.set_assessment_alarm).setVisible(false);
        if (!isNew) {
            menu.findItem(R.id.add_course).setEnabled(true);
            menu.findItem(R.id.delete_term).setEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_submit:
                addTerm();
                break;
            case R.id.add_course:
                btnAddCourse();
                break;
            case R.id.delete_term:
                deleteTerm();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
    private void launchCourseScreen(Course course, int position) {
        Intent intent = new Intent(this, CourseController.class);
        intent.putExtra("isNew", false);
        intent.putExtra("courseID", course.getCourseID());
        intent.putExtra("position", position);
        startActivityForResult(intent, COURSE_REQUEST_CODE);
    }
    void filterCourses(){
        if (!courseValues.isEmpty()) {
            int tmpInt = 0;
            for(int i = 0; i >= courseValues.size()-1; i++)
                tmpInt = i;
                if (!courseValues.get(tmpInt).getTermID().toString().equals(termID.toString())) {
                    courseValues.remove(tmpInt);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        switch (requestCode)
        {
            case COURSE_REQUEST_CODE:
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
        if (courseValues!=null){
            if (courseValues.size()<1) {
                Intent intent = new Intent();
                intent.putExtra("isNew", false);
                intent.putExtra("termID", termID);
                intent.putExtra("position", position);
                intent.putExtra("action", "delete");
                setResult(RESULT_OK, intent);
                finish();
            } else if(courseValues.size()>0){
                String msg = "Error: Unable to delete term because this term has courses.  Delete all courses for this term first.";
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        } else if (courseValues==null) {
            Intent intent = new Intent();
            intent.putExtra("isNew", false);
            intent.putExtra("termID", termID);
            intent.putExtra("position", position);
            intent.putExtra("action", "delete");
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    void addTerm(){
        if (isNew) {
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

    }

    void setTermStartDate(){
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        termStartDate.setText(sdf.format(calendar.getTime()));
    }

    void setTermEndDate(){
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        termEndDate.setText(sdf.format(calendar.getTime()));
    }
}
