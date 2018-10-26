package com.example.vilash.moneymanagement;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.database.Cursor;
import android.widget.TextView;
import java.util.Calendar;


public class MainScreen extends AppCompatActivity {
    private DBHelper myDB ;

    TextView myCredit ;
    TextView myDebit;
    TextView myReminder;
    TextView myBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        GlobalVariable.currentYearInt = Calendar.getInstance().get(Calendar.YEAR);
        GlobalVariable.currentYear = "Year" + GlobalVariable.currentYearInt;
        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");

        myCredit = (TextView) findViewById(R.id.myCredit);
        myDebit = (TextView) findViewById(R.id.myDebit);
        myReminder = (TextView) findViewById(R.id.myReminder);
        myBalance = (TextView) findViewById(R.id.myBalance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating Button Click Event: Call Add Transaction Screen
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.myFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, AddTransaction.class);
                startActivity(intent);
            }
        });

        getBalance();
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
                Intent intentStatical = new  Intent(this, StatisticalView.class);
                startActivity(intentStatical);
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

    @Override
    protected void onDestroy() {
        myDB.close();
        super.onDestroy();
    }

    protected void onRestart() {
        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");
        getBalance();
        super.onRestart();
    }

    private void getBalance() {
        myDB = new DBHelper(this);

        Cursor rs = myDB.runReadOnlyQuery("SELECT SUM(RecMoney) FROM " + GlobalVariable.currentYear + " WHERE TypeCode=1");
        int totalCredit = 0;
        if(rs != null && rs.getCount() > 0) {
            rs.moveToFirst();
            totalCredit = rs.getInt(0);
        }
        rs.close();


        rs = myDB.runReadOnlyQuery("SELECT SUM(RecMoney) FROM " + GlobalVariable.currentYear + " WHERE TypeCode=2");
        int totalDebit = 0;
        if(rs != null && rs.getCount() > 0) {
            rs.moveToFirst();
            totalDebit = rs.getInt(0);
        }
        rs.close();


        rs = myDB.runReadOnlyQuery("SELECT SUM(RecMoney) FROM " + GlobalVariable.currentYear + " WHERE TypeCode=3");
        int totalReminder = 0;
        if(rs != null && rs.getCount() > 0) {
            rs.moveToFirst();
            totalReminder = rs.getInt(0);
        }
        rs.close();

        String tmpStr;

        tmpStr = GlobalVariable.getCurrencyString(String.valueOf(totalCredit));
        myCredit.setText("Credit: " + tmpStr + " Rs.");

        tmpStr = GlobalVariable.getCurrencyString(String.valueOf(totalDebit));
        myDebit.setText("Debit: " + tmpStr + " Rs.");

        tmpStr = GlobalVariable.getCurrencyString(String.valueOf(totalReminder));
        myReminder.setText("Reminder: " + tmpStr + " Rs.");

        tmpStr = GlobalVariable.getCurrencyString(String.valueOf(totalCredit - totalDebit));
        myBalance.setText("Balance: " + tmpStr + " Rs.");
    }
}

