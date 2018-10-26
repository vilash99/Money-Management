package com.example.vilash.moneymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.DatePicker;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class AddTransaction extends AppCompatActivity {
    private int year, selectedMonth, day;

    private AutoCompleteTextView txtTransType;
    private TextView dateView;
    private AutoCompleteTextView txtCategory;
    private EditText txtMoney;
    private EditText txtComments;
    private Button btnSave;

    private DBHelper myDB;
    private int selectedCategoryID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");

        /*Get all fields asscoiated with correspondig variables*/
        txtTransType = (AutoCompleteTextView)findViewById(R.id.txtTransType);
        dateView = (TextView) findViewById(R.id.txtCurrentDate);
        txtCategory = (AutoCompleteTextView)findViewById(R.id.txtCategory);
        txtMoney = (EditText) findViewById(R.id.txtMoney);
        txtComments = (EditText) findViewById(R.id.txtComments);
        btnSave = (Button) findViewById(R.id.btnSave);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Add items in Transaction Type
        String[] transactionType ={"Credit","Debit","Reminder"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, transactionType);

        //Getting the instance of AutoCompleteTextView
        txtTransType.setThreshold(1);       //will start working from first character
        txtTransType.setAdapter(adapter);   //setting the adapter data into the AutoCompleteTextView

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        selectedMonth = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, selectedMonth+1, day);


        //Show all Categories on txtCategory
        myDB = new DBHelper(this);
        Cursor rs = myDB.runReadOnlyQuery("SELECT CategoryType From MyCategories");

        List itemIds = new ArrayList<>();
        while(rs.moveToNext()) {
            String mCat = rs.getString(0);
            itemIds.add(mCat);
        }
        rs.close();

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, itemIds);
        txtCategory.setThreshold(1);       //will start working from first character
        txtCategory.setAdapter(adp);   //setting the adapter data into the AutoCompleteTextView*/

        if(GlobalVariable.editMode) {
            btnSave.setText(R.string.button_edit);

            //Fetch old records associated with GlobalVariable.selectedID
            rs = myDB.runReadOnlyQuery("SELECT _id, (CASE TypeCode WHEN 1 THEN 'Credit' WHEN 2 THEN 'Debit' WHEN 3 THEN 'Reminder' END) As " +
                    "TransType, RecDate, (Select CategoryType From MyCategories Where _id = RecSubject) As RecSubjects, RecMoney, " +
                    "Comments, RecMonth FROM " + GlobalVariable.currentYear + " Where _id = " + GlobalVariable.selectedID );

            if(rs != null && rs.getCount() > 0) {
                rs.moveToFirst();

                //Store data on associated controls
                txtTransType.setText(rs.getString(1));
                dateView.setText(rs.getString(2));
                txtCategory.setText(rs.getString(3));
                txtMoney.setText(rs.getString(4));
                txtComments.setText(rs.getString(5));
                selectedMonth = rs.getInt(6);
            }
            else {
                Toast.makeText(AddTransaction.this, "Selected record is not found!", Toast.LENGTH_LONG).show();
                btnSave.setText(R.string.button_save);
                GlobalVariable.editMode = false;
            }
            rs.close();
        }
        else {
            btnSave.setText(R.string.button_save);
        }


        btnSave.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Check if all fields is filled
                if (validateData()) {

                    //Check if Category is selected from dropdown
                    Cursor RS = myDB.runReadOnlyQuery("SELECT " + DatabaseContext.FeedEntry._ID + " From MyCategories WHERE CategoryType='" +
                            txtCategory.getText().toString()  + "'");

                    if(RS != null && RS.getCount() > 0) {
                        //Store Category ID
                        RS.moveToFirst();
                        selectedCategoryID = RS.getInt(0);
                        RS.close();

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTransaction.this);

                        builder.setMessage(R.string.save_dialog_message)
                                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked Yes button
                                        saveNewTransaction();
                                    }
                                })

                                .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        //Toast.makeText(AddTransaction.this, "Clicked on No button", Toast.LENGTH_LONG).show();
                                    }
                                });

                        AlertDialog dialog =  builder.create();
                        dialog.show();
                    }
                    else {
                        Toast.makeText(AddTransaction.this, "Please choose category from list!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(AddTransaction.this, "Please fill all fields before saving!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalVariable.editMode=false;
        myDB.close();
    }

    protected void onRestart() {
        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");
        super.onRestart();
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

    @SuppressWarnings("deprecation")
    public void myDatePicker(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT) .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, selectedMonth, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                showDate(arg1, arg2+1, arg3);
            }
    };

    private void showDate(int year, int month, int day) {
        selectedMonth = month;
        String tmpMonth = String.valueOf(month);

        if(tmpMonth.length() == 1) { tmpMonth = "0"+tmpMonth; }

        dateView.setText(new StringBuilder().append(day).append("/")
                .append(tmpMonth).append("/").append(year));
    }

    /*Check if any fields is left empty*/
    private boolean validateData() {
        if(txtTransType.getText().toString().equals(""))
            return false;
        else if(txtCategory.getText().toString().equals(""))
            return false;
        else if(txtMoney.getText().toString().equals(""))
            return false;
        else if(txtComments.getText().toString().equals("") )
            return false;
        else
            return true;
    }


    /*Save new Transaction*/
    private void saveNewTransaction() {
        int transType = 1;
        if(txtTransType.getText().toString().equals("Credit"))
            transType = 1;
        else if(txtTransType.getText().toString().equals("Debit"))
            transType = 2;
        else if(txtTransType.getText().toString().equals("Reminder"))
            transType = 3;
        else
            transType = 3;  // By Default all data are reminder

        int myMoney = Integer.parseInt( txtMoney.getText().toString() );

        if(GlobalVariable.editMode) {
            myDB.updateTransaction(transType, selectedMonth, dateView.getText().toString(), selectedCategoryID, myMoney, txtComments.getText().toString());
            GlobalVariable.editMode = false;
            Toast.makeText(AddTransaction.this, "Transaction is updated successfully!", Toast.LENGTH_LONG).show();
        }
        else {
            myDB.insertTransaction(transType, selectedMonth, dateView.getText().toString(), selectedCategoryID, myMoney, txtComments.getText().toString());
            Toast.makeText(AddTransaction.this, "New Transaction is saved successfully!", Toast.LENGTH_LONG).show();
        }

        GlobalVariable.editMode = false;
        btnSave.setText(R.string.button_save);
        clearTextFields();
    }

    private void clearTextFields() {
        txtMoney.setText("");
    }

}
