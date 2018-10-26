package com.example.vilash.moneymanagement;

import android.view.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangeYear extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_year);

        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText txtChangeYear = (EditText) findViewById(R.id.txtChangeYear);
        Button btnChangeYear = (Button) findViewById(R.id.btnChangeYear);


        btnChangeYear.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalVariable.currentYear = "Year" + txtChangeYear.getText().toString();
                GlobalVariable.currentYearInt = Integer.parseInt(txtChangeYear.getText().toString());

                Toast.makeText(ChangeYear.this, "Year has been changed to: " + txtChangeYear.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onRestart() {
        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");
        super.onRestart();
    }
}
