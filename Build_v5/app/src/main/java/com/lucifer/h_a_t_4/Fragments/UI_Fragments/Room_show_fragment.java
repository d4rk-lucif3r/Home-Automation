package com.lucifer.h_a_t_4.Fragments.UI_Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucifer.h_a_t_4.Activities.Device_list_activity;
import com.lucifer.h_a_t_4.Custom_ListAdapter.myListAdapter;
import com.lucifer.h_a_t_4.DatabaseHelper.DBHelper;
import com.lucifer.h_a_t_4.R;


import java.util.ArrayList;


public class Room_show_fragment extends Fragment {
    public ListView RoomList;
    Cursor data;
    public myListAdapter listAdapter2;
    DBHelper mydb;
    Button settings;
    String Room1,Room2,Room3,Room4,Room5;
    boolean done1,done2,done3,done4,done5;

    ArrayList<String> theList = new ArrayList<>();
    ArrayList<String> theList_2 = new ArrayList<>();
    private String TAG = Room_show_fragment.class.getSimpleName();
    private static Room_show_fragment instance = null;
    TextView text;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("Connected");
            text.setText(type);

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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


        RoomList = getView().findViewById(R.id.Room_list);
        settings = getView().findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(getContext(), Device_list_activity.class);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                                                }
                                            });

        Room1 = sharedpreferences.getString("Room1","Room 1");
        Room2 = sharedpreferences.getString("Room2","Room 2");
        Room3 = sharedpreferences.getString("Room3","Room 3");
        Room4 = sharedpreferences.getString("Room4","Room 4");
        Room5 = sharedpreferences.getString("Room5","Room 5");

        done1 = sharedpreferences.getBoolean("Done1",false);
        done2 = sharedpreferences.getBoolean("Done2",false);
        done3 = sharedpreferences.getBoolean("Done3",false);
        done4 = sharedpreferences.getBoolean("Done4",false);
        done5 = sharedpreferences.getBoolean("Done5",false);


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("GETADDRESS"));


        theList.add(Room1);
        theList.add(Room2);
        theList.add(Room3);
        theList.add(Room4);
        theList.add(Room5);

        //listAdapter2 = new myListAdapter(getContext(), android.R.layout.simple_list_item_1, theList);
        listAdapter2 = new myListAdapter(getContext(), R.layout.mylistadapter, theList);

        RoomList.setAdapter(listAdapter2);
        RoomList.setOnItemClickListener(myListClickListener);
        RoomList.setOnItemLongClickListener(myLongClickListener);


        super.onResume();
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            Fragment fragment;
            fragment = new DeviceControlFragment();

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
            ft.replace(R.id.frag2, fragment);
            ft.commit();
            switch (arg2) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putString("Call", "1");
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    Bundle args = new Bundle();
                    args.putString("Call", "2");
                    fragment.setArguments(args);
                    break;

            }

        }
    };
    private AdapterView.OnItemLongClickListener myLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom); //Declaring Alert Dialog
            builder.setTitle("Room Settings");
            builder.setPositiveButton("Delete Room",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom); //Declaring Alert Dialog
                            builder.setTitle("Room Deletion");
                            builder.setMessage("You're About to Delete a Room. Are you Sure?");
                            builder.setPositiveButton("Yes, Delete It!",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String getSelectedItem = theList.get(position);
                                            listAdapter2.remove(getSelectedItem);
                                        }
                                    });
                            builder.setNegativeButton("No, Leave It!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.setCancelable(false);
                            builder.show();
                        }
                    });
            builder.setNegativeButton("Update Room Name", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                    builder.setTitle("Update Room Name");
                    builder.setMessage("Please Enter New Room Name Below. Note: You Need to Restart Application for changes to take place ");
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final EditText Room_name = new EditText(getContext());
                    Room_name.setForegroundGravity(Gravity.CENTER);

                    Room_name.setHint("Enter Room Name Here");
                    switch (position) {
                        case 0:
                            if(done1)
                            Room_name.setText(Room1);
                            break;
                        case 1:
                            if(done2)
                            Room_name.setText(Room2);

                            break;
                        case 2:
                            if(done3)

                                Room_name.setText(Room3);

                            break;
                        case 3:
                            if(done4)

                                Room_name.setText(Room4);


                            break;
                        case 4:
                            if(done5)

                                Room_name.setText(Room5);
                            break;


                    }
                    linearLayout.addView(Room_name);
                    builder.setView(linearLayout);
                    builder.setPositiveButton("Update Name", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            switch (position) {
                                case 0:
                                    editor.putBoolean("Done1",true);
                                    editor.putString("Room1",Room_name.getText().toString());
                                    editor.apply();
                                    Toast.makeText(getContext(),"Room Name Updated",Toast.LENGTH_SHORT).show();

                                    break;
                                case 1:
                                    editor.putBoolean("Done2",true);
                                    editor.putString("Room2",Room_name.getText().toString());
                                    editor.apply();
                                    Toast.makeText(getContext(),"Room Name Updated",Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    editor.putBoolean("Done3",true);
                                    editor.putString("Room3",Room_name.getText().toString());
                                    editor.apply();
                                    Toast.makeText(getContext(),"Room Name Updated",Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    editor.putBoolean("Done4",true);
                                    editor.putString("Room4",Room_name.getText().toString());
                                    editor.apply();
                                    Toast.makeText(getContext(),"Room Name Updated",Toast.LENGTH_SHORT).show();

                                    break;
                                case 4:
                                    editor.putBoolean("Done5",true);
                                    editor.putString("Room5",Room_name.getText().toString());
                                    editor.apply();
                                    Toast.makeText(getContext(),"Room Name Updated",Toast.LENGTH_SHORT).show();

                                    break;


                            }
                        }
                    });
                    builder.setNegativeButton("Cancel",null);
                    builder.show();

                }
            });
            builder.setNeutralButton("Return",null);
            builder.setCancelable(true);
            builder.show();
            return true;
        }
    };

    @Override
    public void onPause() {
        theList.clear();
        Log.d(TAG,"Room show paused : List cleared");
        super.onPause();
    }
}