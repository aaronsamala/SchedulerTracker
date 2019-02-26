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
    List<Course> courseValues;
    //ListView courseListView;
    CourseMentor courseMentor;
    Note note;
    int position;

/*


************NEED TO add ModNote feature; need to do assessments
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        datasource = new DBDataSource(this);
        datasource.open();

        courseMentor = new CourseMentor();
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

            }
        });

        btnDeleteAssessment = (Button) findViewById(R.id.btnDeleteAssessment);

        btnDeleteAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAssessment();

            }
        });

        if (!isNew) {
            loadCourseMentor();
            loadNote();
            loadAssessments();
        }
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
            Log.d("Note", "loadNote: Is not empty");
            Log.d("Note", "notes.size = " + notes.size());

            int tmpInt = 0;
            for(int i = 0; i <= notes.size()-1; i++) {
                tmpInt = i;

                Log.d("Note", "tmpInt = " + tmpInt);
                //if (String.valueOf(notes.get(tmpInt).getNote_CourseID()).equals(courseID.toString())) {
                //String tmpString = notes.get(tmpInt).getNote_CourseID();
                Long tmpLong = notes.get(tmpInt).getNote_CourseID();
                Log.d("Note", "tmpLong = " + tmpLong + " & courseID = " + courseID + " & note = " + notes.get(tmpInt).getNote());
                if (tmpLong.equals(courseID)) {
                        Log.d("Note", "tmpInt = " + tmpInt + " matches courseID");
                    note=notes.get(tmpInt);
                    break;
                }
            }
        }
        if (note!=null) {
            Log.d("Note", "note!=null");
            if (!note.getNote().isEmpty()) {
                Log.d("Note", "Note = " + note.getNote());
                EditText editNote = (EditText) findViewById(R.id.editNote);
                editNote.setText(note.getNote().toString());
            }
        }

    }

    void loadAssessments(){

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
    }

    void deleteCourseMentor(){

    }

    void saveNote(){
        if (note == null) {
            EditText editNote = (EditText) findViewById(R.id.editNote);
            String noteString = editNote.getText().toString();
            note = datasource.createNote(noteString, courseID);
        }

    }

    void deleteNote(){

    }

    void saveAssessment(){

    }

    void deleteAssessment(){

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
