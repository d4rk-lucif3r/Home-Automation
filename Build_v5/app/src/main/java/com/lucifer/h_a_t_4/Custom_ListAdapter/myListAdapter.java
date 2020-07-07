package com.lucifer.h_a_t_4.Custom_ListAdapter;

import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;



import com.lucifer.h_a_t_4.DatabaseHelper.DBHelper;


import com.lucifer.h_a_t_4.R;


import java.util.ArrayList;

public class myListAdapter extends ArrayAdapter<String> { //Custom ListAdapter
    private final Context context;
    private final ArrayList<String> web;
    DBHelper mydb;
    TextView txtTitle;

    public myListAdapter(Context context, int tvResourceId, ArrayList<String> web) {
        super(context, tvResourceId, web);
        mydb = new DBHelper(getContext());
        this.context = context;
        this.web = web;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mylistadapter, parent, false);

        txtTitle = rowView.findViewById(R.id.textView2);


        txtTitle.setText(web.get(position));


        return rowView;


    }
}

