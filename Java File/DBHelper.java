package com.example.vilash.moneymanagement;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_TRANSACTION_TABLE =
            "CREATE TABLE IF NOT EXISTS " + GlobalVariable.currentYear + " (" +
                    DatabaseContext.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContext.FeedEntry.TRANSACTION_COLUMN_TYPECODE + " INTEGER," +
                    DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECMONTH + " INTEGER," +
                    DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECDATE + " TEXT," +
                    DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECSUBJECT + " INTEGER," +
                    DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECMONEY + " INTEGER," +
                    DatabaseContext.FeedEntry.TRANSACTION_COLUMN_COMMENTS + " TEXT)";

    private static final String SQL_CREATE_CATEGORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DatabaseContext.FeedEntry.CATEGORY_TABLE_NAME + " (" +
                    DatabaseContext.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContext.FeedEntry.CATEGORY_COLUMN_CATEGORYTYPE + " TEXT)";

    //private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseContext.FeedEntry.TRANSACTION_TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "MoneyManagement.db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TRANSACTION_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    /* Insert New Transaction data on Database*/
    public void insertTransaction(int typeCode, int recMonth, String recDate, int recSubject, int recMoney, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_TYPECODE, typeCode);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECMONTH, recMonth);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECDATE, recDate);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECSUBJECT, recSubject);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECMONEY, recMoney);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_COMMENTS, comments);

        db.insert(GlobalVariable.currentYear, null, contentValues);
    }

    public void updateTransaction(int typeCode, int recMonth, String recDate, int recSubject, int recMoney, String comments) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_TYPECODE, typeCode);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECMONTH, recMonth);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECDATE, recDate);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECSUBJECT, recSubject);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_RECMONEY, recMoney);
        contentValues.put(DatabaseContext.FeedEntry.TRANSACTION_COLUMN_COMMENTS, comments);

        db.update(GlobalVariable.currentYear, contentValues, "_id="+ GlobalVariable.selectedID, null);

    }
    /*public void insertCategories(String myCat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContext.FeedEntry.CATEGORY_COLUMN_CATEGORYTYPE, myCat);

        db.insert(DatabaseContext.FeedEntry.CATEGORY_TABLE_NAME, null, contentValues);
    }*/

    public Cursor runReadOnlyQuery(String mySQL){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor RS =  db.rawQuery(mySQL, null );
        return RS;
    }

    public void deleteRow() {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(GlobalVariable.currentYear, "_id=" + GlobalVariable.selectedID, null);
    }
}