package com.example.schedulertracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CourseController  extends AppCompatActivity {
    private Button btnSave, btnDeleteCourse;
    private Button btnSaveCourseMentor;
    private Button btnDeleteCourseMentor;
    private Button btnSaveNote;
    private Button btnDeleteNote;
    private Button btnSaveAssessment;
    private Button btnDeleteAssessment;


    DBDataSource datasource;
    Long termID;
    Long courseID;
    boolean isNew = true;
    boolean isNewAssessment = true;
    List<Course> courseValues;
    CourseMentor courseMentor;
    Note note;
    Assessment assessment;
    List<Assessment> assessmentValues;
    ListView assessmentListView;
    String[] assessmentArray;
    ArrayAdapter<String> assessmentArrayAdapter;
    Calendar calendar;
    EditText courseStartDate, courseEndDate, assessmentDueDate, assessmentGoalDate;
    String myFormat = "MM/dd/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    Intent intent;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        invalidateOptionsMenu();
        datasource = new DBDataSource(this);
        datasource.open();
        calendar= Calendar.getInstance();

        courseStartDate = (EditText) findViewById(R.id.editStartDate);
        courseEndDate  = (EditText) findViewById(R.id.editEndDate);
        assessmentDueDate  = (EditText) findViewById(R.id.editDueDate);
        assessmentGoalDate  = (EditText) findViewById(R.id.editGoalDate);

        setDatePickers();


        btnDeleteCourse = (Button) findViewById(R.id.btnDelete);

        btnDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCourse();

            }
        });

        courseMentor = new CourseMentor();
        courseValues = datasource.getAllCourses();
        intent = getIntent();

        isNew = intent.getBooleanExtra("isNew", true);

        termID = intent.getLongExtra("TermID", 0L);
        if (!isNew){

            courseID = intent.getLongExtra("courseID", 0L);
            for (Course course : courseValues){
                if (course.getCourseID().equals(courseID)){

                    EditText edit = (EditText)findViewById(R.id.editCourseTitle);
                    edit.setText(course.getTitle());
                    edit = (EditText)findViewById(R.id.editEndDate);
                    edit.setText(course.getAnticipatedEndDate());
                    edit = (EditText)findViewById(R.id.editCourseStatus);
                    edit.setText(course.getStatus());
                    edit = (EditText)findViewById(R.id.editStartDate);
                    edit.setText(course.getStartDate());
                    termID = course.getTermID();
                    Log.d("TermID_ONCOURSELOAD", termID.toString());
                }
            }
            setAssessmentArrayList();
            loadCourseMentor();
            loadNote();
            loadAssessments();
            btnDeleteCourse.setEnabled(true);
        }

        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCourse();

            }
        });

        btnSaveCourseMentor = (Button) findViewById(R.id.btnSaveCourseMentor);

        btnSaveCourseMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveCourseMentor();

            }
        });

        btnDeleteCourseMentor = (Button) findViewById(R.id.btnDeleteCourseMentor);

        btnDeleteCourseMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCourseMentor();

            }
        });

        btnSaveNote = (Button) findViewById(R.id.btnSaveNotes);

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveNote();

            }
        });



        btnDeleteNote = (Button) findViewById(R.id.btnClearNotes);

        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteNote();

            }
        });

        btnSaveAssessment = (Button) findViewById(R.id.btnSaveAssessment);

        btnSaveAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveAssessment();
                setAssessmentArrayList();

            }
        });

        btnDeleteAssessment = (Button) findViewById(R.id.btnDeleteAssessment);

        btnDeleteAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAssessment();
                setAssessmentArrayList();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_term, menu);
        this.menu = menu;
        menu.findItem(R.id.save_submit).setVisible(false);
        menu.findItem(R.id.delete_term).setVisible(false);
        menu.findItem(R.id.add_course).setVisible(false);
        menu.findItem(R.id.set_course_alarm).setVisible(true);
        menu.findItem(R.id.send_note).setVisible(true);
        menu.findItem(R.id.set_assessment_alarm).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_course_alarm:
                setCourseAlarm();
                break;
            case R.id.send_note:
                sendNote();
                break;
            case R.id.set_assessment_alarm:
                setAssessmentAlarm();
                break;
            case R.id.cancel:
                finish();

                break;
            default:
                break;
        }

        return true;
    }
    void setAssessmentAlarm(){


        if (isNewAssessment){
            String msg = "Error: Please save the assessment or select an existing assessment before attempting to schedule the notification.";

            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        } else if (!isNewAssessment){

            EditText editGoalDate = (EditText) findViewById(R.id.editGoalDate);

            String goalDate = editGoalDate.getText().toString();
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent(CourseController.this, AssessmentGoalAlarmReceiver.class);

            String parseString = String.valueOf(assessment.assessmentID);
            int requestCode = Integer.parseInt(parseString);
            PendingIntent broadcast = PendingIntent.getBroadcast(CourseController.this, requestCode, notificationIntent, 0);

            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(goalDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cal.add(Calendar.DAY_OF_MONTH,-7);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            String msg = "Assessment alarm saved.";
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            saveAssessment();
            setAssessmentArrayList();
        }

    }

    void deleteCourse(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        datasource.deleteCourse(courseID);
        finish();
        String msg = "Course deleted.";
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    void setCourseAlarm(){
        if (isNew){
            String msg = "Error: Please save the course before attempting to schedule the notification.";
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }else if (!isNew) {
            //start
            String startDate = courseStartDate.getText().toString();


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(CourseController.this, CourseStartAlarmReceiver.class);

            String parseString = String.valueOf(courseID);
            int requestCode = Integer.parseInt(parseString);
            PendingIntent broadcast = PendingIntent.getBroadcast(CourseController.this, requestCode, notificationIntent, 0);

            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cal.add(Calendar.DAY_OF_MONTH,-7);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

            String endDate = courseEndDate.getText().toString();

            AlarmManager alarmEndManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent notificationEndIntent = new Intent(CourseController.this, CourseEndAlarmReceiver.class);
            notificationEndIntent.setAction("android.media.action.DISPLAY_COURSE_END");
            notificationEndIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent endBroadcast = PendingIntent.getBroadcast(this, requestCode, notificationEndIntent, 0);

            Calendar endCal = Calendar.getInstance();
            try {
                endCal.setTime(sdf.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            endCal.add(Calendar.DAY_OF_MONTH,-7);
            alarmEndManager.setExact(AlarmManager.RTC_WAKEUP, endCal.getTimeInMillis(), endBroadcast);

            String msg = "Course alarms saved.";
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    void setDatePickers(){

        final DatePickerDialog.OnDateSetListener courseStartDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                courseStartDate.setText(sdf.format(calendar.getTime()));
            }

        };

        courseStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CourseController.this, courseStartDatePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener courseEndDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                courseEndDate.setText(sdf.format(calendar.getTime()));
            }

        };

        courseEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CourseController.this, courseEndDatePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener assessmentDueDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                assessmentDueDate.setText(sdf.format(calendar.getTime()));
            }

        };

        assessmentDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CourseController.this, assessmentDueDatePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener assessmentGoalDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                assessmentGoalDate.setText(sdf.format(calendar.getTime()));
            }

        };

        assessmentGoalDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CourseController.this, assessmentGoalDatePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    void sendNote(  ){

        EditText editNotes = (EditText) findViewById(R.id.editNote);
        String notes = editNotes.getText().toString();
        EditText editCourseTitle = (EditText) findViewById(R.id.editCourseTitle);
        String subject = editCourseTitle.getText().toString();
        subject += " Notes";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, notes);

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    void setAssessmentArrayList(){
        assessmentValues = datasource.getAllAssessments(courseID);

        assessmentArray = new String[assessmentValues.size()];
        for(int i = 0; i < assessmentValues.size(); i++) {
            assessmentArray[i] = assessmentValues.get(i).getTitle();
        }
        assessmentListView = (ListView)findViewById(R.id.listAssessments);
        assessmentArrayAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview, R.id.textView, assessmentArray);
        assessmentListView.setAdapter(assessmentArrayAdapter);
        ListView list = (ListView) findViewById(R.id.listAssessments);
        setListViewHeightBasedOnChildren(list);
        loadAssessments();
    }
    void loadCourseMentor(){
        courseMentor = datasource.getCourseMentor(courseID);
        if (!courseMentor.getName().isEmpty()){
            EditText edit = (EditText)findViewById(R.id.editCourseMentorName);
            edit.setText(courseMentor.getName());
            edit = (EditText)findViewById(R.id.editCourseMentorEmail);
            edit.setText(courseMentor.getEmailAddress());
            edit = (EditText)findViewById(R.id.editCourseMentorPhone);
            edit.setText(courseMentor.getPhoneNumber());
        }

    }

    void loadNote(){

        List<Note> notes = datasource.getAllNotes();
        if (!notes.isEmpty()){

            int tmpInt = 0;
            for(int i = 0; i <= notes.size()-1; i++) {
                tmpInt = i;

                Long tmpLong = notes.get(tmpInt).getNote_CourseID();
                if (tmpLong.equals(courseID)) {
                    note=notes.get(tmpInt);
                    break;
                }
            }
        }
        if (note!=null) {
            if (!note.getNote().isEmpty()) {
                EditText editNote = (EditText) findViewById(R.id.editNote);
                editNote.setText(note.getNote().toString());
            }
        }

    }

    void loadAssessments(){

        if (assessmentValues!=null){
            assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    LinearLayout ll = (LinearLayout) view; // get the parent layout view
                    TextView tv = (TextView) ll.findViewById(R.id.textView); // get the child text view

                    assessment = (Assessment) assessmentValues.get(position);
                    loadAssessment();
                    isNewAssessment = false;
                }
            });
        }
        ListView list = (ListView) findViewById(R.id.listAssessments);
        setListViewHeightBasedOnChildren(list);
    }

    void loadAssessment(){
        EditText editTitle = (EditText) findViewById(R.id.editAssessmentTitle);
        editTitle.setText(assessment.getTitle());
        EditText editType = (EditText) findViewById(R.id.editAssessmentType);
        editType.setText(assessment.getAssessmentType());
        EditText editDueDate = (EditText) findViewById(R.id.editDueDate);
        editDueDate.setText(assessment.getDueDate());
        EditText editGoalDate = (EditText) findViewById(R.id.editGoalDate);
        editGoalDate.setText(assessment.getGoalDate());
    }

    void saveCourseMentor(){
        if (courseMentor.getName().isEmpty()) {
            EditText editName = (EditText) findViewById(R.id.editCourseMentorName);
            String name = editName.getText().toString();
            EditText editEmail = (EditText) findViewById(R.id.editCourseMentorEmail);
            String email = editEmail.getText().toString();
            EditText editMentorPhone = (EditText) findViewById(R.id.editCourseMentorPhone);
            String mentorPhone = editMentorPhone.getText().toString();

            courseMentor = datasource.createCourseMentor(courseID, name, mentorPhone, email);
        } else if (!courseMentor.getName().isEmpty()) {
            EditText editName = (EditText) findViewById(R.id.editCourseMentorName);
            String name = editName.getText().toString();
            EditText editEmail = (EditText) findViewById(R.id.editCourseMentorEmail);
            String email = editEmail.getText().toString();
            EditText editMentorPhone = (EditText) findViewById(R.id.editCourseMentorPhone);
            String mentorPhone = editMentorPhone.getText().toString();
            Long courseMentorID = courseMentor.getCourseMentorID();
            courseMentor = datasource.modCourseMentor(courseMentorID, courseMentor.getCourseID(), name, mentorPhone, email);
        }
        String msg = "Course Mentor saved.";
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    void deleteCourseMentor(){
        if (courseMentor!=null){
            courseMentor = datasource.modCourseMentor(courseMentor.getCourseMentorID(), courseMentor.getCourseID(), "", "", "");

            EditText edit = (EditText) findViewById(R.id.editCourseMentorName);
            edit.setText("");
            edit = (EditText) findViewById(R.id.editCourseMentorEmail);
            edit.setText("");
            edit = (EditText) findViewById(R.id.editCourseMentorPhone);
            edit.setText("");
            String msg = "Course Mentor deleted.";
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();

        }

    }

    void saveNote(){
        if (note == null) {
            EditText editNote = (EditText) findViewById(R.id.editNote);
            String noteString = editNote.getText().toString();
            note = datasource.createNote(noteString, courseID);
            String msg = "Note saved.";
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        } else if (note != null) {
            EditText editNote = (EditText) findViewById(R.id.editNote);
            String noteString = editNote.getText().toString();
            datasource.modNote(note.getNoteID(), note.getNote_CourseID(), noteString);
            String msg = "Note saved.";
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }


    }

    void deleteNote(){
        if (note!=null){
            datasource.modNote(note.getNoteID(), note.getNote_CourseID(), "");
            EditText editNote = (EditText) findViewById(R.id.editNote);
            editNote.setText("");
            String msg = "Note deleted.";
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }

    }

    void saveAssessment(){

        if (isNewAssessment) {


            EditText editTitle = (EditText) findViewById(R.id.editAssessmentTitle);
            String title = editTitle.getText().toString();
            EditText editType = (EditText) findViewById(R.id.editAssessmentType);
            String type = editType.getText().toString();
            EditText editDueDate = (EditText) findViewById(R.id.editDueDate);
            String dueDate = editDueDate.getText().toString();
            EditText editGoalDate = (EditText) findViewById(R.id.editGoalDate);
            String goalDate = editGoalDate.getText().toString();

            assessment =  datasource.createAssessment(courseID, title, type, dueDate, goalDate);

        } else if (!isNewAssessment){
            EditText editTitle = (EditText) findViewById(R.id.editAssessmentTitle);
            String title = editTitle.getText().toString();
            EditText editType = (EditText) findViewById(R.id.editAssessmentType);
            String type = editType.getText().toString();
            EditText editDueDate = (EditText) findViewById(R.id.editDueDate);
            String dueDate = editDueDate.getText().toString();
            EditText editGoalDate = (EditText) findViewById(R.id.editGoalDate);
            String goalDate = editGoalDate.getText().toString();

            datasource.modAssessment(assessment.getAssessmentID(), courseID, title, type, dueDate, goalDate);
        }
        isNewAssessment = true;
        String msg = "Assessment saved.";
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        EditText editAssessmentTitle = (EditText) findViewById(R.id.editAssessmentTitle);
        EditText editAssessmentType = (EditText) findViewById(R.id.editAssessmentType);
        EditText editAssessmentDue = (EditText) findViewById(R.id.editDueDate);
        EditText editAssessmentGoal = (EditText) findViewById(R.id.editGoalDate);
        editAssessmentTitle.setText("");
        editAssessmentType.setText("");
        editAssessmentDue.setText("");
        editAssessmentGoal.setText("");
    }

    void deleteAssessment(){

        if (!isNewAssessment) {
            datasource.deleteAssessment(assessment.getAssessmentID());
            EditText editAssessmentTitle = (EditText) findViewById(R.id.editAssessmentTitle);
            EditText editAssessmentType = (EditText) findViewById(R.id.editAssessmentType);
            EditText editAssessmentDue = (EditText) findViewById(R.id.editDueDate);
            EditText editAssessmentGoal = (EditText) findViewById(R.id.editGoalDate);
            editAssessmentTitle.setText("");
            editAssessmentType.setText("");
            editAssessmentDue.setText("");
            editAssessmentGoal.setText("");

            String msg = "Assessment deleted.";
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }

    }

    void addCourse(){


        if (isNew) {
            EditText edit = (EditText) findViewById(R.id.editCourseTitle);
            String tmpCourseTitle = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editStartDate);
            String tmpStartDate = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editEndDate);
            String tmpEndDate = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editCourseStatus);
            String tmpCourseStatus = edit.getText().toString();

            Course tmpCourse = datasource.createCourse(
                    termID,
                    tmpCourseTitle,
                    tmpStartDate,
                    tmpEndDate,
                    tmpCourseStatus
            );
            courseID = tmpCourse.getCourseID();
            isNew = false;
        } else if (!isNew){
            EditText edit = (EditText) findViewById(R.id.editCourseTitle);
            String tmpCourseTitle = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editStartDate);
            String tmpStartDate = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editEndDate);
            String tmpEndDate = edit.getText().toString();
            edit = (EditText) findViewById(R.id.editCourseStatus);
            String tmpCourseStatus = edit.getText().toString();

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

        btnDeleteCourse.setEnabled(true);
        String msg = "Course saved.";
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
