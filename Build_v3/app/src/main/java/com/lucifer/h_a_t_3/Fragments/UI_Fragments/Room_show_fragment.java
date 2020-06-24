package com.lucifer.h_a_t_3.Fragments.UI_Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lucifer.h_a_t_3.Custom_ListAdapter.myListAdapter;
import com.lucifer.h_a_t_3.DatabaseHelper.DBHelper;
import com.lucifer.h_a_t_3_github.R;


import java.util.ArrayList;


public class Room_show_fragment extends Fragment {
    ListView RoomList;
    Cursor data;
    public myListAdapter listAdapter2;
    DBHelper mydb;


    ArrayList<String> theList =new ArrayList<>();
    ArrayList<String> theList_2 =new ArrayList<>();
    private String TAG = Room_show_fragment.class.getSimpleName();
    private static Room_show_fragment instance = null;
    Cursor cursor1;
    TextView text;
    String abc;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("Connected");
            text.setText(type);

        }};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }
    public static Room_show_fragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_room_show, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mydb = new DBHelper(getContext());
        if(mydb.check_db2()){
            data = mydb.getallRoomdata();}

        RoomList=getView().findViewById(R.id.Room_list);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("GETADDRESS"));

        if(mydb.check_db2()) {
            data = mydb.getallRoomdata();
            while (data.moveToNext()) {
                theList.add(data.getString(0));
            }

            listAdapter2 = new myListAdapter(getContext(), R.layout.mylistadapter, theList);
            // listAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, theList);
            RoomList.setAdapter(listAdapter2);
            RoomList.setOnItemClickListener(myListClickListener);
            RoomList.setOnItemLongClickListener(myLongClickListener);

        }
        super.onResume();
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            Fragment fragment ;
            fragment = new DeviceControlFragment();

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.pop_enter,R.anim.pop_exit);
            ft.replace(R.id.frag2, fragment);
            ft.commit();





        }
    };
    private AdapterView.OnItemLongClickListener myLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            String getSelectedItem = theList.get(position);

            mydb.deletedata(getSelectedItem);
         listAdapter2.remove(getSelectedItem);
            return true;
        }
    };

    @Override
    public void onPause() {
      theList.clear();
        Log.d(TAG,"List Cleared");
        super.onPause();
    }
}