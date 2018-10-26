package com.example.vilash.moneymanagement;


import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_DATE;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_MONEY;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_SUBJECT;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_TYPE;
import static com.example.vilash.moneymanagement.StatisticalColumns.COLUMN_COMMENTS;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SummaryViewAdapters extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    TextView txtDate;
    TextView txtMoney;
    TextView txtSubject;
    TextView txtType;
    TextView txtComments;

    public SummaryViewAdapters(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.summary_view_columns, null);

            txtDate=(TextView) convertView.findViewById(R.id.clmDate);
            txtMoney=(TextView) convertView.findViewById(R.id.clmSummeryMoney);
            txtSubject=(TextView) convertView.findViewById(R.id.clmSubject);
            txtType=(TextView) convertView.findViewById(R.id.clmTransType);
            txtComments=(TextView) convertView.findViewById(R.id.clmComments);
        }

        HashMap<String, String> map=list.get(position);
        txtDate.setText(map.get(COLUMN_DATE));
        txtMoney.setText(map.get(COLUMN_MONEY));
        txtSubject.setText(map.get(COLUMN_SUBJECT));
        txtType.setText(map.get(COLUMN_TYPE));
        txtComments.setText(map.get(COLUMN_COMMENTS));

        return convertView;
    }

}
