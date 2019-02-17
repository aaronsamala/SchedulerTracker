package com.example.schedulertracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private Button btnAddTermButton;
    private DBDataSource datasource;
    List<Term> termValues;
    TermController termController;
    Term termToPass = new Term();
    private final static int TERM_REQUEST_CODE = 1;
    ListView termListView;
    String[] termArray;
    ArrayAdapter<String> termArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //termController = new TermController();
        //termController.setMainActivity(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        btnAddTermButton = (Button) findViewById(R.id.btnAddTermButton);

        btnAddTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { launchTermScreen(); }
        });

        //Add DB stuff 20190210
        datasource = new DBDataSource(this);
        datasource.open();
        //datasource.createTerm("TermTest");

        //datasource.createTerm("TermTest", "testStartDate", "testEndDate");
        Term tmpTerm = new Term();
        tmpTerm.setTitle("TESTTITLE");
        tmpTerm.setStartDate("TESTstart");
        tmpTerm.setEndDate("TESTend");
        //addTerm(tmpTerm);
        termValues = datasource.getAllTerms();
        for (Term term : termValues)
        {
            String tmp = term.getTitle() + ", " + term.getTermID() + ", " + term.getStartDate() + ", " + term.getEndDate();
            Log.d("ListTest",tmp);
            //datasource.deleteTerm(term);

        }
        Log.d("ListTestEnd","End Of List Test");


        //start
        //String[] courses = new String[] {"C169", "C188", "C196", "C482", "EDV1", "TXC1", "TXP1", "TYC1", "TYP1"};
        setTermList();

        termListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                LinearLayout ll = (LinearLayout) view; // get the parent layout view
                TextView tv = (TextView) ll.findViewById(R.id.textView); // get the child text view
                //final String item = tv.getText().toString() + " int position= " + position;
                //final String item = tv.getText().toString() + " int position= " + position;

                Term tmpTerm = (Term) termValues.get(position);
                final String item = tmpTerm.getTitle() + " - " + tmpTerm.getTermID();
                //String item = ((TextView)view).getText().toString();

                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT).show();
                launchTermScreen(tmpTerm, position);

            }
        });


    }

    public void setTermList() {
        termArray = new String[termValues.size()];
        for(int i = 0; i < termValues.size(); i++) termArray[i] = termValues.get(i).getTitle();

        termListView = (ListView)findViewById(R.id.listTerms);
        termArrayAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview, R.id.textView, termArray);
        termListView.setAdapter(termArrayAdapter);
    }

    private void launchTermScreen() {
        Intent intent = new Intent(this, TermController.class);
        intent.putExtra("isNew", true);
        startActivityForResult(intent, TERM_REQUEST_CODE);
    }
    private void launchTermScreen(Term term, int position) {
        Intent intent = new Intent(this, TermController.class);
        intent.putExtra("isNew", false);
        intent.putExtra("title", term.getTitle());
        intent.putExtra("termID", term.getTermID());
        intent.putExtra("startDate", term.getStartDate());
        intent.putExtra("endDate", term.getEndDate());
        intent.putExtra("position", position);
        startActivityForResult(intent, TERM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        Log.d("ActResult", "Started");
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case TERM_REQUEST_CODE:
                //TextView textView = (TextView)findViewById(R.id.resultDataTextView);
                if(resultCode == RESULT_OK)
                {
                    boolean isNew = dataIntent.getBooleanExtra("isNew", true);
                    if (isNew) {
                        Log.d("ActResult", "Received");
                        String termTitle = dataIntent.getStringExtra("termTitle");
                        String termStartDate = dataIntent.getStringExtra("termStartDate");
                        String termEndDate = dataIntent.getStringExtra("termEndDate");
                        Term term = new Term();
                        term.setTitle(termTitle);
                        term.setStartDate(termStartDate);
                        term.setEndDate(termEndDate);
                        //termArrayAdapter.add(termTitle);
                        termValues.add(term);
                        setTermList();
                        addTerm(term);
                        //textView.setText(messageReturn);
                    }else if (!isNew) {
                        String action = dataIntent.getStringExtra("action");
                        Log.d("actionEquals", action);
                        if (action.equals("modify")){
                            Log.d("ActResult", "Received_!isNew");
                            String termTitle = dataIntent.getStringExtra("termTitle");
                            String termStartDate = dataIntent.getStringExtra("termStartDate");
                            String termEndDate = dataIntent.getStringExtra("termEndDate");
                            Long termID = dataIntent.getLongExtra("termID", 999);
                            int position = dataIntent.getIntExtra("position", 999);
                            Term term = new Term();
                            term.setTitle(termTitle);
                            term.setStartDate(termStartDate);
                            term.setEndDate(termEndDate);
                            term.setTermID(termID);
                            //termArrayAdapter.add(termTitle);
                            termValues.remove(position);
                            termValues.add(position, term);
                            setTermList();
                            modTerm(term);
                            //textView.setText(messageReturn);
                        } else if (action.equals("delete")){
                            Log.d("ActResult", "Received_!isNewDelete");
                            Long termID = dataIntent.getLongExtra("termID", 999);
                            int position = dataIntent.getIntExtra("position", 999);
                            datasource.deleteTerm(termID);
                            termValues.remove(position);
                            setTermList();
                        }

                    }
                }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void addTerm(Term term){
        datasource.createTerm(term.getTitle(), term.getStartDate(), term.getEndDate());
        String tmp = term.getTitle() + ", " + term.getTermID() + ", " + term.getStartDate();
        Log.d("AddedTerm",tmp);
    }
    void modTerm(Term term){
        String tmpString = term.getTermID().toString();
        Integer tmpInteger = Integer.valueOf(tmpString);
        datasource.modTerm(tmpInteger, term.getTitle(), term.getStartDate(), term.getEndDate());
        String tmp = term.getTitle() + ", " + term.getTermID() + ", " + term.getStartDate();
        Log.d("moddedTerm",tmp);
    }

    /*
    void setTermController(TermController termController){
        this.termController = termController;
    }
    */

    @Override
    protected void onResume() {
        datasource.open();

        termValues = datasource.getAllTerms();
        for (Term term : termValues)
        {
            String tmp = term.getTitle() + ", " + term.getTermID() + ", " + term.getStartDate() + ", " + term.getEndDate();
            Log.d("ListTest",tmp);
            //datasource.deleteTerm(term);

        }
        Log.d("ListTestEnd","End Of List Test_Redo");

        super.onResume();
    }

    @Override
    protected void onPause() {
        //datasource.close();
        super.onPause();
    }
}
