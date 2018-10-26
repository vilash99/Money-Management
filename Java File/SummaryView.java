package com.example.vilash.moneymanagement;

import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_DATE;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_MONEY;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_SUBJECT;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_TYPE;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_COMMENTS;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class SummaryView extends AppCompatActivity {

    private DBHelper myDB;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_view);

        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.lstvwSummaryView);

        //Category is chosen
        if(GlobalVariable.globalQuery.length() > 0) {
            showSummaryOfCurrentMonth(GlobalVariable.globalQuery);
        }
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


    private void showSummaryOfCurrentMonth(String myCat) {
        myDB = new DBHelper(this);
        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();

        Cursor rs = myDB.runReadOnlyQuery(GlobalVariable.globalQuery);

        while(rs.moveToNext()) {
            HashMap<String,String> temp = new HashMap<String, String>();
            temp.put(COLUMN_DATE, rs.getString(1));
            temp.put(COLUMN_MONEY, rs.getString(3) + " Rs. ");
            temp.put(COLUMN_SUBJECT, rs.getString(2));
            temp.put(COLUMN_TYPE, rs.getString(0));
            temp.put(COLUMN_COMMENTS, rs.getString(4));

            myList.add(temp);
        }
        rs.close();

        SummaryViewAdapters myLSTAdapter =  new SummaryViewAdapters(this, myList);
        listView.setAdapter(myLSTAdapter);
    }

    //-----------------Toolbar Menu item procedures start here -------------------------//
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

    //-----------------Toolbar Menu item procedures Ends here -------------------------//
}
