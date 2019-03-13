package com.example.schedulertracker;

import android.content.Intent;
import android.os.Bundle;
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
    DBDataSource datasource;
    List<Term> termValues;
    private final static int TERM_REQUEST_CODE = 1;
    ListView termListView;
    String[] termArray;
    ArrayAdapter<String> termArrayAdapter;


    DBDataSource getDatasource(){

        return datasource;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datasource = new DBDataSource(this);
        datasource.open();
        btnAddTermButton = (Button) findViewById(R.id.btnAddTermButton);

        btnAddTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { launchTermScreen(); }
        });

        termValues = datasource.getAllTerms();

        setTermList();

        termListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                LinearLayout ll = (LinearLayout) view;
                TextView tv = (TextView) ll.findViewById(R.id.textView);

                Term tmpTerm = (Term) termValues.get(position);
                final String item = tmpTerm.getTitle() + " - " + tmpTerm.getTermID();
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
        switch (requestCode)
        {
            case TERM_REQUEST_CODE:

                if(resultCode == RESULT_OK)
                {
                    boolean isNew = dataIntent.getBooleanExtra("isNew", true);
                    if (isNew) {
                        String termTitle = dataIntent.getStringExtra("termTitle");
                        String termStartDate = dataIntent.getStringExtra("termStartDate");
                        String termEndDate = dataIntent.getStringExtra("termEndDate");
                        Term term = new Term();
                        term.setTitle(termTitle);
                        term.setStartDate(termStartDate);
                        term.setEndDate(termEndDate);
                        termValues.add(term);
                        setTermList();
                        addTerm(term);
                    }else if (!isNew) {
                        String action = dataIntent.getStringExtra("action");
                        if (action.equals("modify")){
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
                            termValues.remove(position);
                            termValues.add(position, term);
                            setTermList();
                            modTerm(term);
                        } else if (action.equals("delete")){
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    void addTerm(Term term){
        datasource.createTerm(term.getTitle(), term.getStartDate(), term.getEndDate());
    }
    void modTerm(Term term){
        String tmpString = term.getTermID().toString();
        Integer tmpInteger = Integer.valueOf(tmpString);
        datasource.modTerm(tmpInteger, term.getTitle(), term.getStartDate(), term.getEndDate());
    }

    @Override
    protected void onResume() {
        datasource.open();

        termValues = datasource.getAllTerms();

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
