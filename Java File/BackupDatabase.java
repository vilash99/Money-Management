package com.example.vilash.moneymanagement;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BackupDatabase extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_database);

        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnBackupDatabase = (Button) findViewById(R.id.btnBackupDatabase);

        btnBackupDatabase.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    String myPackageName = getApplicationContext().getPackageName();

                    if (sd.canWrite()) {
                        String currentDBPath = "//data//" + myPackageName + "//databases//" + DBHelper.DATABASE_NAME;
                        String backupDBPath = DBHelper.DATABASE_NAME;

                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        }
                    }
                    Toast.makeText(BackupDatabase.this, "Database is backed up in Internal Storage!", Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {
                    Toast.makeText(BackupDatabase.this, "Error: " +  e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void onRestart() {
        this.setTitle("Money Management (" + GlobalVariable.currentYearInt + "-" + ((GlobalVariable.currentYearInt+1)-2000) + ")");
        super.onRestart();
    }
}
