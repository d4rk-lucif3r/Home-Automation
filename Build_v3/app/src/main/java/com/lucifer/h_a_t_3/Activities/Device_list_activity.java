package com.lucifer.h_a_t_3.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lucifer.h_a_t_3.Bluetooth_Service.Bt_connection;
import com.lucifer.h_a_t_3.DatabaseHelper.DBHelper;
import com.lucifer.h_a_t_3.Interface.FragmentToActivity;
import com.lucifer.h_a_t_3.Fragments.Functional_Fragments.New_Bt_Device_Fragment;
import com.lucifer.h_a_t_3.Fragments.Functional_Fragments.Paired_Device_Fragment;
import com.lucifer.h_a_t_3.Fragments.Functional_Fragments.Schedule_Fragment;
import com.lucifer.h_a_t_3.Fragments.Functional_Fragments.room_add_fragment;
import com.lucifer.h_a_t_3_github.R;


import java.util.Calendar;


public class Device_list_activity extends AppCompatActivity implements FragmentToActivity //Implementing FragmentToActivity Interface for getting time Data
{

    Button new_device , back , add_room , paired , schedule;
    TextView text;
    public DBHelper mydb;
    Integer hour;
    Integer minute;
    String Am_pm;


    PendingIntent myPendingIntent;
    AlarmManager alarmManager;
    BroadcastReceiver myBroadcastReceiver;
    Calendar firingCal;
    boolean alarm = false;

    boolean mb1 =false;
    boolean mb2 =false;
    boolean mb3 =false;

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

        }};
    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onReceive(Context context, Intent intent) {  //Broadcast Receiver For Connecting to Bluetooth Device Status
            mb2 = true;
            String type = intent.getStringExtra("Connecting");
            text.setText(type);
            text.setTextColor(ContextCompat.getColor(context, R.color.Red));

        }};
    private BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) { //Broadcast Receiver For Error Connecting to Bluetooth Device Status
            mb3 = true;
            String type = intent.getStringExtra("Error_Connecting");
            text.setText(type);
            text.setTextColor(ContextCompat.getColor(context, R.color.Red));
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mydb = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list_activity);
        invocations();
        listeners();
        Log.d(TAG,"Activity Created");
        text.setText(null);
        Intent mIntent = new Intent(this, Bt_connection.class);  //Binding to BT_Connection Service for Bluetooth Connection
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);


    }

    ServiceConnection mConnection = new ServiceConnection()  //Declaring new Service Connection
    {

        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mServer = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            Bt_connection.LocalBinder mLocalBinder = (Bt_connection.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();
            Log.d(TAG,"Main Activity Binded with service");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Device List resumed");

        LocalBroadcastManager.getInstance(Device_list_activity.this).registerReceiver(broadcastReceiver, new IntentFilter("Connected"));  //Registering Broadcast Receiver
        LocalBroadcastManager.getInstance(Device_list_activity.this).registerReceiver(broadcastReceiver2, new IntentFilter("Connecting"));  //Registering Broadcast Receiver
        LocalBroadcastManager.getInstance(Device_list_activity.this).registerReceiver(broadcastReceiver3, new IntentFilter("Error_Connecting"));  //Registering Broadcast Receiver

    }


    public void invocations() {
        text = findViewById(R.id.textView);
        new_device = findViewById(R.id.new_device);
        back = findViewById(R.id.back);
        add_room= findViewById(R.id.Add_room);
        paired= findViewById(R.id.pairedDevices);
        schedule =findViewById(R.id.Schedule);
        schedule.setVisibility(View.INVISIBLE);
    }

    public void listeners() {



      /*  schedule.setOnClickListener(new View.OnClickListener() { /ScheduleTasking hidden needed some fixes
            @Override
            public void onClick(View v) {
                Fragment fragment1;
                fragment1 = new Schedule_Fragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
                ft.replace(R.id.frag_1, fragment1);
                ft.commit();

            }
        });*/


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
                i.putExtra("Hour",hour);
                i.putExtra("Minute", minute);
                i.putExtra("AM/PM", Am_pm);


                finish();
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                if(alarm!=false) {
                    alarmManager.cancel(myPendingIntent);
                    try {
                        unregisterReceiver(myBroadcastReceiver);   //Unregistering Broadcast Receiver
                    } catch (RuntimeException e){
                        Log.e(TAG,"mybroadcastReciever"+e.getMessage());
                    }

                }

            }
        });
            add_room.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment2;
                    fragment2 = new room_add_fragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
                    ft.replace(R.id.frag_1, fragment2);
                    ft.commit();
                }
            });
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
    public void communicate(Integer Hour, Integer Minute, String am_pm) //Interface method for getting Time Data (Not useful as Scheduled Tasks Hidden )
    {
        hour =Hour;
        minute = Minute;
        Am_pm =am_pm;
     // schedule2();


        Log.d(TAG,"Time Data Recieved :"+hour+":" +minute+":" +Am_pm );
    }
    private void registerMyAlarmBroadcast()
    {
        Log.i(TAG, "Going to register Intent.RegisterAlramBroadcast");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        myBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                alarm = true;
                Log.d(TAG,"BroadcastReceiver::OnReceive()");
                mServer.sendData("3");

                Log.d(TAG,"alarm data send");


            }
        };
        myPendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.alarm.example"),0 );


    }
    public  void schedule2()
    {
        Log.d(TAG,"Schedule task called");
        firingCal= Calendar.getInstance();
        firingCal.set(Calendar.HOUR, hour); // At the hour you want to fire the alarm
        firingCal.set(Calendar.MINUTE, minute); // alarm minute
        Log.d(TAG,"Alarm setted for "+hour+":"+minute+""+Am_pm);
        firingCal.set(Calendar.SECOND, 0); // and alarm second
        long intendedTime = firingCal.getTimeInMillis();

        registerMyAlarmBroadcast();
        alarmManager = (AlarmManager)(getSystemService( Context.ALARM_SERVICE ));
         registerReceiver(myBroadcastReceiver, new IntentFilter("com.alarm.example") );

        //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime , AlarmManager.INTERVAL_DAY , myPendingIntent );
        alarmManager.set(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent);

    }



    @Override
    protected void onDestroy() { //Unbinding All Services and BroadcastReceiver Activity Bounded With
        if(mBounded) {
            unbindService(mConnection);
            Log.d(TAG,"Unbinding Service from Device List Activity");
        }
        if(alarm) {

            alarmManager.cancel(myPendingIntent);
            try {
                unregisterReceiver(myBroadcastReceiver);
            } catch (RuntimeException e){
                Log.e(TAG,"mybroadcastReciever"+e.getMessage());
            }

        }
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