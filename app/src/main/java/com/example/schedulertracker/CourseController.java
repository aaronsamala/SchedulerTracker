package com.example.schedulertracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class CourseController  extends AppCompatActivity {
    private Button btnCancel;
    private Button btnSave;
    private Button btnSaveCourseMentor;
    private Button btnDeleteCourseMentor;
    private Button btnSaveNote;
    private Button btnSendNote;
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
    Assessment assessment;
    List<Assessment> assessmentValues;
    ListView assessmentListView;
    String[] assessmentArray;
    ArrayAdapter<String> assessmentArrayAdapter;

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
            setAssessmentArrayList();
            loadCourseMentor();
            loadNote();
            loadAssessments();
        }




        Log.d("ListCourseEnd","End Of Course List Test");


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

        btnSendNote = (Button) findViewById(R.id.btnSendNotes);

        btnSendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendNote();

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
        for (Assessment assessment : assessmentValues){
            String tmp = assessment.getTitle() + ", " + assessment.getCourseID() + ", " + "courseId = " + courseID + assessment.getAssessmentType();
            Log.d("AssessmentListTest",tmp);
        }
        assessmentArray = new String[assessmentValues.size()];
        for(int i = 0; i < assessmentValues.size(); i++) {
            assessmentArray[i] = assessmentValues.get(i).getTitle();
        }
        Log.d("AssessmentTest", "setAssessmentArrayList: 1, " + assessmentArray.length);
        assessmentListView = (ListView)findViewById(R.id.listAssessments);
        assessmentArrayAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview, R.id.textView, assessmentArray);
        assessmentListView.setAdapter(assessmentArrayAdapter);
        ListView list = (ListView) findViewById(R.id.listAssessments);
        setListViewHeightBasedOnChildren(list);
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

        if (assessmentValues!=null){
            assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    LinearLayout ll = (LinearLayout) view; // get the parent layout view
                    TextView tv = (TextView) ll.findViewById(R.id.textView); // get the child text view
                    //final String item = tv.getText().toString() + " int position= " + position;
                    //final String item = tv.getText().toString() + " int position= " + position;

                    assessment = (Assessment) assessmentValues.get(position);
                    loadAssessment();
                    //final String item = assessment.getTitle() + " - " + assessment.getTermID();
                    //String item = ((TextView)view).getText().toString();

                    //Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT).show();
                    //launchCourseScreen(tmpCourse, position);
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

        }

    }

    void saveNote(){
        if (note == null) {
            EditText editNote = (EditText) findViewById(R.id.editNote);
            String noteString = editNote.getText().toString();
            note = datasource.createNote(noteString, courseID);
        }

    }

    void deleteNote(){
        if (note!=null){
            datasource.modNote(note.getNoteID(), note.getNote_CourseID(), "");
            EditText editNote = (EditText) findViewById(R.id.editNote);
            editNote.setText("");
        }

    }
/*
this.assessmentID = 0L;
        this.dueDate = "";
        this.goalDate = "";
        this.courseID = 0L;
        this.title = "";
        this.assessmentType = "";
 */
    void saveAssessment(){
        if (assessment==null) {


            EditText editTitle = (EditText) findViewById(R.id.editAssessmentTitle);
            String title = editTitle.getText().toString();
            EditText editType = (EditText) findViewById(R.id.editAssessmentType);
            String type = editType.getText().toString();
            EditText editDueDate = (EditText) findViewById(R.id.editDueDate);
            String dueDate = editDueDate.getText().toString();
            EditText editGoalDate = (EditText) findViewById(R.id.editGoalDate);
            String goalDate = editGoalDate.getText().toString();

            datasource.createAssessment(courseID, title, type, dueDate, goalDate);

        } else if (assessment!=null){
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
        assessment = null;
    }

    void deleteAssessment(){

        if (assessment != null) {
            datasource.deleteAssessment(assessment.getAssessmentID());
        }
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
