package com.lucifer.h_a_t_3.Fragments.UI_Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lucifer.h_a_t_3.Activities.MainActivity;
import com.lucifer.h_a_t_3.Bluetooth_Service.Bt_connection;
import com.lucifer.h_a_t_3_github.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.BIND_AUTO_CREATE;


public class DeviceControlFragment extends Fragment {
    Button dev1, dev2, dev3, dev4, close;
    boolean mBounded;
    private static DeviceControlFragment instance = null;
    private ScheduledExecutorService scheduleTaskExecutor;
    private ScheduledExecutorService scheduleTaskExecutor2;
    private String TAG = DeviceControlFragment.class.getSimpleName();
    TextView textCounter;

    Timer timer;
    MyTimerTask myTimerTask;
    public Bt_connection mServer;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        schedule();

        Intent mIntent = new Intent(getContext(), Bt_connection.class);
        getActivity().bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        instance = this;


    }

    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mServer = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            Bt_connection.LocalBinder mLocalBinder = (Bt_connection.LocalBinder) service;
            mServer = mLocalBinder.getServerInstance();
            Log.d(TAG, "DEvice Control Frag Binded with service");
        }
    };

    public static DeviceControlFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_control, container, false);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        invocation();
        listeners();
    }

    public void invocation() {
        dev1 = getView().findViewById(R.id.Dev_1);
        dev2 = getView().findViewById(R.id.Dev_2);
        dev4 = getView().findViewById(R.id.Dev_3);
        dev3 = getView().findViewById(R.id.Dev_4);
        close = getView().findViewById(R.id.close_device_control);
        textCounter=getView().findViewById(R.id.textcount);

    }

    public void listeners() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment = new Room_show_fragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
                ft.replace(R.id.frag2, fragment);
                ft.commit();
            }
        });


        dev1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServer.sendData("1");

                Log.d(TAG, "dev 1Switching On........");
            }
        });


        dev1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mServer.sendData("a");
                Log.d(TAG, " dev 1 Switching Off........");
                return true;
            }
        });
        dev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServer.sendData("2");
                Log.d(TAG, " dev 2 Switching On........");

            }
        });
        dev2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mServer.sendData("b");
                Log.d(TAG, " dev 2 Switching Off........");
                return true;
            }
        });
        dev3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServer.sendData("3");
                Log.d(TAG, " dev 3 Switching On........");

            }
        });
        dev3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mServer.sendData("c");
                Log.d(TAG, " dev 3 Switching Off........");
                return true;
            }
        });
        dev4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServer.sendData("4");
                Log.d(TAG, " dev 4 Switching On........");

            }
        });
        dev4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mServer.sendData("d");
                Log.d(TAG, " dev 4 Switching Off........");
                return true;
            }
        });

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Device Control Destroyed");
        if (mBounded) {
            getContext().unbindService(mConnection);
            Log.d(TAG, "Service Unbinded from Device Control");
            mBounded = false;
        }
        super.onDestroy();
    }

    public void schedule() {
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        //Schedule a task to run every 5 seconds (or however long you want)
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Do stuff here!

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Toast.makeText(getContext() ,"Its been 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, 0, 5, TimeUnit.SECONDS); // or .MINUTES, .HOURS etc.


    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
            final String strDate = simpleDateFormat.format(calendar.getTime());

            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    textCounter.setText(strDate);
                }});
        }
    }

}