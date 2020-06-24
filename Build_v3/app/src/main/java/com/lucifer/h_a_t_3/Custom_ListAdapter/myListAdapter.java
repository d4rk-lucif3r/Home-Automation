package com.lucifer.h_a_t_3.Custom_ListAdapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;


import com.lucifer.h_a_t_3_github.R;

import java.util.ArrayList;

public class myListAdapter extends ArrayAdapter<String> { //ListAdapter for Room Show Fragment ListView
    private final Context context;
    private final ArrayList<String> web;

    public  myListAdapter(Context context, int tvResourceId,ArrayList <String> web){
        super(context,tvResourceId,web);
        this.context = context;
        this.web = web;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View rowView= inflater.inflate(R.layout.mylistadapter, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        txtTitle.setText(web.get(position));

        return rowView;
    }

}

