package com.example.vilash.moneymanagement;

import static com.example.vilash.moneymanagement.StatisticalColumns.FIRST_COLUMN;
import static com.example.vilash.moneymanagement.StatisticalColumns.SECOND_COLUMN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class StatisticalView extends AppCompatActivity {
    private DBHelper myDB;
    private Spinner txtCurrentMonth;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_view);

        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize all controls
        txtCurrentMonth = (Spinner)findViewById(R.id.txtCurrentMonth);
        txtCurrentMonth.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        listView=(ListView)findViewById(R.id.listvwStatistical);


        //Load all Months in Database
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtCurrentMonth.setAdapter(adapter);

        //Set to current month
        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        txtCurrentMonth.setSelection(currentMonth);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String myCat = listView.getItemAtPosition(position).toString();
                myCat = myCat.substring(myCat.indexOf("Categories=")+11, myCat.indexOf('}'));

                int selectedMonth = (int)txtCurrentMonth.getSelectedItemId();

                GlobalVariable.globalQuery = "SELECT (CASE TypeCode WHEN 1 THEN 'Credit' WHEN 2 THEN 'Debit' WHEN 3 THEN 'Reminder' END) As " +
                                            "TransType, RecDate, (Select CategoryType From MyCategories Where _id = RecSubject) As RecSubjects, " +
                                            "RecMoney, Comments FROM " + GlobalVariable.currentYear + " Where RecMonth=" + (selectedMonth+1) + " AND " +
                                            "RecSubject=(SELECT _id From MyCategories Where CategoryType='" + myCat + "') Order By RecDate Desc";

                Intent intentSummaryView = new  Intent(StatisticalView.this, SummaryView.class);
                startActivity(intentSummaryView);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
    }

    protected void onRestart() {
        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");
        super.onRestart();
    }

    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        String firstItem = String.valueOf(txtCurrentMonth.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(txtCurrentMonth.getSelectedItem()))) {
                //Toast.makeText(StatisticalView.this, txtCurrentMonth.getSelectedItem() +" selected", Toast.LENGTH_SHORT).show();
                //addDataInListView();
            } else {
                addDataInListView();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {
        }
    }

    //-----------------Toolbar Menu item procedures start here -------------------------//
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miAddNew:
                Intent intentAddTrans = new  Intent(this, AddTransaction.class);
                startActivity(intentAddTrans);
                return true;
            case R.id.miStatical:
                return true;
            case R.id.miDetail:
                Intent intentDetail = new  Intent(this, DetailView.class);
                startActivity(intentDetail);
                return true;
            case R.id.miChangeYear:
                Intent intentChangeYear = new  Intent(this, ChangeYear.class);
                startActivity(intentChangeYear);
                return true;
            case R.id.miBackupDB:
                Intent intentBackupDB = new  Intent(this, BackupDatabase.class);
                startActivity(intentBackupDB);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //-----------------Toolbar Menu item procedures ends here -------------------------//


    public void addDataInListView() {

        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();

        //Show all Categories and money
        int selectedMonth = (int)txtCurrentMonth.getSelectedItemId();

        myDB = new DBHelper(this);
        Cursor rs = myDB.runReadOnlyQuery("SELECT (SELECT CategoryType From MyCategories WHERE _ID = RecSubject) AS MYSubject, SUM(RecMoney) AS MYMONEY FROM " +
                      GlobalVariable.currentYear + " WHERE RecMonth = " + (selectedMonth+1) + " GROUP BY RecSubject");

        while(rs.moveToNext()) {
            HashMap<String,String> temp = new HashMap<String, String>();
            temp.put(FIRST_COLUMN, rs.getString(0));
            temp.put(SECOND_COLUMN, rs.getString(1) + " Rs.");
            myList.add(temp);
        }
        rs.close();

        StatisticalListViewAdapters myLSTAdapter =  new StatisticalListViewAdapters(this, myList);
        listView.setAdapter(myLSTAdapter);
    }
}
