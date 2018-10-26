package com.example.vilash.moneymanagement;

import android.provider.BaseColumns;

public final class DatabaseContext {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContext() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {

        //Transaction Table columns
        public static final String TRANSACTION_TABLE_NAME = "Year2017";
        public static final String TRANSACTION_COLUMN_TYPECODE = "TypeCode";
        public static final String TRANSACTION_COLUMN_RECMONTH = "RecMonth";
        public static final String TRANSACTION_COLUMN_RECDATE = "RecDate";
        public static final String TRANSACTION_COLUMN_RECSUBJECT = "RecSubject";
        public static final String TRANSACTION_COLUMN_RECMONEY = "RecMoney";
        public static final String TRANSACTION_COLUMN_COMMENTS = "Comments";


        //Category type columns
        public static final String CATEGORY_TABLE_NAME = "MyCategories";
        public static final String CATEGORY_COLUMN_CATEGORYTYPE = "CategoryType";
    }

}