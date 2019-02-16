package com.example.schedulertracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TermController  extends AppCompatActivity {
    private Button btnCancel;
    private Button btnSubmit;
    MainActivity mainActivity;
    boolean isNew = true;
    Long termID;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        //mainActivity = new MainActivity();
        //mainActivity.setTermController(this);
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
