package com.example.vilash.moneymanagement;

import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_DATE;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_MONEY;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_SUBJECT;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_TYPE;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_COMMENTS;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_ID;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import android.app.AlertDialog;
import android.content.DialogInterface;


public class DetailView extends AppCompatActivity {
    private ListView listView;
    private Spinner txtCurrentMonth;
    private Spinner txtTransactionType;
    private CheckBox chkAllYear;
    private EditText txtSearchBox;
    private boolean firstMonthChange;
    private DBHelper myDB;
    private boolean searchText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        myDB = new DBHelper(this);

        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chkAllYear = (CheckBox)findViewById(R.id.chkAllYear);
        txtSearchBox = (EditText) findViewById(R.id.txtSearchText);

        //Initialize all controls
        txtCurrentMonth = (Spinner)findViewById(R.id.txtCurrentMonth);
        txtTransactionType = (Spinner)findViewById(R.id.txtTransType);

        txtCurrentMonth.setOnItemSelectedListener(new DetailView.CurrentMonthItemSelectedListener());
        txtTransactionType.setOnItemSelectedListener(new DetailView.TransactionTypeItemSelectedListener());

        //Load all Months in txtCurrentMonth
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtCurrentMonth.setAdapter(adapter);

        //Load all Transaction Type in txtTrasactionType
        ArrayAdapter<CharSequence> adapterTrans = ArrayAdapter.createFromResource(this,
                R.array.TransType, android.R.layout.simple_spinner_item);
        adapterTrans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtTransactionType.setAdapter(adapterTrans);


        listView=(ListView)findViewById(R.id.lstvwDetailView);

        firstMonthChange=true;
        //Set to current month
        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        txtCurrentMonth.setSelection(currentMonth);

        //Choose Credit from Transaction Type
        txtTransactionType.setSelection(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String mySelected = listView.getItemAtPosition(position).toString();
                mySelected = mySelected.substring(mySelected.indexOf("MyRecId=")+8, mySelected.indexOf(','));

                GlobalVariable.selectedID = Integer.parseInt(mySelected);
                GlobalVariable.editMode = true;

                //Ask for Update, Delete or Cancel
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailView.this);
                builder.setTitle("Money Management");
                builder.setMessage("What you want to do with this transaction?");

                builder.setPositiveButton("Update",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new  Intent(DetailView.this, AddTransaction.class));
                        }
                    });

                builder.setNeutralButton("Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            myDB.deleteRow();
                            addDataInListView();
                            Toast.makeText(DetailView.this, "Selected transaction is deleted!", Toast.LENGTH_SHORT).show();
                        }
                    });

                builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    });

                AlertDialog dialog =  builder.create();
                dialog.show();
            }
        });

        Button txtSearch = (Button)findViewById(R.id.btnSearch);
        txtSearch.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Search in all years
                searchText = true;
                addDataInListView();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
        GlobalVariable.editMode=false;
    }

    protected void onRestart() {
        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");
        super.onRestart();
    }

    ////------------Click on txtCurrentMonth spinner ---------------------//
    private class CurrentMonthItemSelectedListener implements AdapterView.OnItemSelectedListener {
        String firstItem = String.valueOf(txtCurrentMonth.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(txtCurrentMonth.getSelectedItem()))) {
                //Toast.makeText(StatisticalView.this, txtCurrentMonth.getSelectedItem() +" selected", Toast.LENGTH_SHORT).show();
                //addDataInListView();
            } else {
                if(firstMonthChange) {
                     addDataInListView();
                }
                else {
                    firstMonthChange = false;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {
        }
    }


    ////------------Click on txtCurrentMonth spinner ---------------------//
    private class TransactionTypeItemSelectedListener implements AdapterView.OnItemSelectedListener {
        String firstItem = String.valueOf(txtTransactionType.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(txtTransactionType.getSelectedItem()))) {
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


    public void addDataInListView() {

        ArrayList<HashMap<String, String>> myList = new ArrayList<>();

        //Show all Categories and money
        int selectedMonth = (int)txtCurrentMonth.getSelectedItemId();
        int selectedTransType = (int)txtTransactionType.getSelectedItemId();
        String myQuery = "";

        Cursor rs = null;

        if(searchText) {
            String searchText = txtSearchBox.getText().toString();

            if(chkAllYear.isChecked()) {
                //Search in All year

                //Get All Year Tables
                rs = myDB.runReadOnlyQuery("SELECT name FROM sqlite_master where type='table' AND name LIKE 'Year%'");

                int i = 0;
                while(rs.moveToNext()) {
                    if(i == 0) {
                        i = 1;
                        myQuery = "SELECT A._id, (CASE TypeCode WHEN 1 THEN 'Credit' WHEN 2 THEN 'Debit' WHEN 3 THEN 'Reminder' END) As TransType, " +
                                "A.RecDate, B.CategoryType, A.RecMoney, A.Comments FROM " + rs.getString(0) + " A LEFT JOIN MyCategories B ON " +
                                "A.RecSubject = B._id WHERE A.RecSubject LIKE '%" + searchText +"%' OR A.Comments Like '%" + searchText + "%'";
                    }
                    else {
                        myQuery = myQuery + " UNION ALL SELECT A._id, (CASE TypeCode WHEN 1 THEN 'Credit' WHEN 2 THEN 'Debit' WHEN 3 THEN 'Reminder' END) As TransType, " +
                                "A.RecDate, B.CategoryType, A.RecMoney, A.Comments FROM " + rs.getString(0) + " A LEFT JOIN MyCategories B ON " +
                                "A.RecSubject = B._id WHERE A.RecSubject LIKE '%" + searchText +"%' OR A.Comments Like '%" + searchText + "%'";
                    }
                }
                rs.close();
            }
            else {
                //Search in just this year
                myQuery = "SELECT A._id, (CASE TypeCode WHEN 1 THEN 'Credit' WHEN 2 THEN 'Debit' WHEN 3 THEN 'Reminder' END) As TransType, " +
                        "A.RecDate, B.CategoryType, A.RecMoney, A.Comments FROM " + GlobalVariable.currentYear + " A LEFT JOIN MyCategories B ON " +
                        "A.RecSubject = B._id WHERE A.RecSubject LIKE '%" + searchText +"%' OR A.Comments Like '%" + searchText + "%' Order By A.RecDate Desc";
            }
        }
        else {
            //All Selected
            if (selectedTransType == 3) {
                myQuery = "SELECT _id, (CASE TypeCode WHEN 1 THEN 'Credit' WHEN 2 THEN 'Debit' WHEN 3 THEN 'Reminder' END) As " +
                        "TransType, RecDate, (Select CategoryType From MyCategories Where _id = RecSubject) As RecSubjects, RecMoney, Comments FROM " +
                        GlobalVariable.currentYear + " Where RecMonth=" + (selectedMonth + 1) + " Order By RecDate Desc";
            } else {
                myQuery = "SELECT _id, (CASE TypeCode WHEN 1 THEN 'Credit' WHEN 2 THEN 'Debit' WHEN 3 THEN 'Reminder' END) As " +
                        "TransType, RecDate, (Select CategoryType From MyCategories Where _id = RecSubject) As RecSubjects, RecMoney, Comments FROM " +
                        GlobalVariable.currentYear + " Where RecMonth=" + (selectedMonth + 1) + " AND TypeCode=" + (selectedTransType + 1) + " Order By RecDate Desc";
            }
        }

        rs = myDB.runReadOnlyQuery(myQuery);

        while(rs.moveToNext()) {
            HashMap<String,String> temp = new HashMap<>();
            temp.put(COLUMN_DATE, rs.getString(2));
            temp.put(COLUMN_MONEY, rs.getString(4) + " Rs. ");
            temp.put(COLUMN_SUBJECT, rs.getString(3));
            temp.put(COLUMN_TYPE, rs.getString(1));
            temp.put(COLUMN_COMMENTS, rs.getString(5));
            temp.put(COLUMN_ID, rs.getString(0));
            myList.add(temp);
        }
        rs.close();

        searchText=false;
        DetailViewAdapters myLSTAdapter =  new DetailViewAdapters(this, myList);
        listView.setAdapter(myLSTAdapter);
    }

}
