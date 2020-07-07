package com.lucifer.h_a_t_4.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lucifer.h_a_t_4.Services.Bt_connection;
import com.lucifer.h_a_t_4.DatabaseHelper.DBHelper;
import com.lucifer.h_a_t_4.Fragments.Functional_Fragments.New_Bt_Device_Fragment;
import com.lucifer.h_a_t_4.Fragments.Functional_Fragments.Paired_Device_Fragment;
import com.lucifer.h_a_t_4.R;


public class Device_list_activity extends AppCompatActivity {

    Button new_device, back, paired;
    TextView text;

    public DBHelper mydb;


    boolean mb1 = false;
    boolean mb2 = false;
    boolean mb3 = false;

    boolean mBounded;
    public Bt_connection mServer;

    private String TAG = Device_list_activity.class.getSimpleName();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onReceive(Context context, Intent intent) { //Broadcast Receiver For Connected to Bluetooth Device Status
            mb1 = true;
            String type = intent.getStringExtra("Connected");
            text.setText(type);
            text.setTextColor(ContextCompat.getColor(context, R.color.Green));

        }
    };
    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onReceive(Context context, Intent intent) {//Broadcast Receiver For Connecting to Bluetooth Device Status
            mb2 = true;
            String type = intent.getStringExtra("Connecting");
            text.setText(type);
            text.setTextColor(ContextCompat.getColor(context, R.color.Red));

        }
    };
    private BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {//Broadcast Receiver For Error Connecting to Bluetooth Device Status
            mb3 = true;
            String type = intent.getStringExtra("Error_Connecting");
            text.setText(type);
            text.setTextColor(ContextCompat.getColor(context, R.color.Red));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mydb = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list_activity);
        invocations();
        listeners();
        Log.d(TAG, "Activity Created");
        text.setText(null);


    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Device List resumed");

        LocalBroadcastManager.getInstance(Device_list_activity.this).registerReceiver(broadcastReceiver, new IntentFilter("Connected"));
        LocalBroadcastManager.getInstance(Device_list_activity.this).registerReceiver(broadcastReceiver2, new IntentFilter("Connecting"));
        LocalBroadcastManager.getInstance(Device_list_activity.this).registerReceiver(broadcastReceiver3, new IntentFilter("Error_Connecting"));

    }


    public void invocations() { // Invocating buttons and textView
        text = findViewById(R.id.textView);
        new_device = findViewById(R.id.new_device);
        back = findViewById(R.id.back);
        paired= findViewById(R.id.pairedDevices);
    }

    public void listeners() {// Onclick Listeners

        new_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1;
                fragment1 = new New_Bt_Device_Fragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
                ft.replace(R.id.frag_1, fragment1);
                ft.commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);

        }});

        paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1;
                fragment1 = new Paired_Device_Fragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
                ft.replace(R.id.frag_1, fragment1);
                ft.commit();



            }
        });





    }

    @Override
    protected void onDestroy() { //Unbinding All Services and BroadcastReceiver Activity Bounded With

        if(mb1)
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (RuntimeException e){
                Log.e(TAG,"broadcastreciever"+e.getMessage());
            }
        if(mb2)
            try {
                unregisterReceiver(broadcastReceiver2);
            } catch (RuntimeException e){
                Log.e(TAG,"broadcastreciever2"+e.getMessage());
            }
        if(mb3)
            try {
                unregisterReceiver(broadcastReceiver3);
            } catch (RuntimeException e){
                Log.e(TAG,"broadcastreciever3"+e.getMessage());
            }
        super.onDestroy();
    }
}