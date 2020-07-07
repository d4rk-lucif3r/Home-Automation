package com.lucifer.h_a_t_4.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.lucifer.h_a_t_4.Services.Bt_connection;
import com.lucifer.h_a_t_4.DatabaseHelper.DBHelper;
import com.lucifer.h_a_t_4.R;


import java.io.IOException;

public class MainActivity extends AppCompatActivity  {
    Button  LastCon;
    TextView textxt;
    BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private String TAG = MainActivity.class.getSimpleName();
    boolean mBounded;
    boolean alarm = false;
    public Bt_connection mServer;
    DBHelper mydb;
    String LastAddress, device;




    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) { //Broadcast Receiver For Connected to Bluetooth Device Status
            String type = intent.getStringExtra("Connected");
            textxt.setText(type);
            LastCon.setVisibility(View.INVISIBLE);
            textxt.setTextColor(ContextCompat.getColor(context, R.color.Green));
        }};
    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) { //Broadcast Receiver For Connecting to Bluetooth Device Status
            String type = intent.getStringExtra("Connecting");
            textxt.setText(type);
            textxt.setTextColor(ContextCompat.getColor(context, R.color.Red));
        }};
    private BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) { //Broadcast Receiver For Error Connecting to Bluetooth Device Status
            String type = intent.getStringExtra("Error_Connecting");
            textxt.setText(type);
            LastCon.setText("Retry Last Connection");
            LastCon.setVisibility(View.VISIBLE);
            textxt.setTextColor(ContextCompat.getColor(context, R.color.Red));
        }};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        Log.d(TAG,"Main activity Created");
        Intent mIntent = new Intent(this, Bt_connection.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE); //Binding to BT_Connection Service for Bluetooth Connection
        setContentView(R.layout.activity_main);

        textxt = findViewById(R.id.testxt);
        LastCon=findViewById(R.id.LastCon);
        LastCon.setVisibility(View.INVISIBLE);
        textxt.setText(null);



        if(myBluetooth == null) //Bluetooth Adapter testing
        {
            Toast.makeText(MainActivity.this, "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!myBluetooth.isEnabled()) //Bluetooth Adapter testing
        {

            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
            Log.d(TAG, "MainActivity: Starting Bluetooth ");

        }
        checkLast();


        LastCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mServer.connectLast(LastAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    } ServiceConnection mConnection = new ServiceConnection()  //Declaring new Service Connection
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

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("Connected")); //Registering Broadcast Receiver
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(broadcastReceiver2, new IntentFilter("Connecting")); //Registering Broadcast Receiver
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(broadcastReceiver3, new IntentFilter("Error_Connecting")); //Registering Broadcast Receiver

        super.onResume();
    }







    public void checkLast() //Function for calling Last device Paired so that returning Connection Could Be Made easily
    {
        if(LastConnect()){
            Log.d(TAG,"Lastconnect true");

            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom); //Declaring Alert Dialog
                builder.setTitle("Previous Connection Detected!");
                builder.setMessage("We have a detected a connection with a device earlier. Would you like to pair to "+"\t"+device+" ?");
                builder.setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"yes pressed ");
                                try {
                                    mServer.connectLast(LastAddress); //Calling ConnectLAst Method in BT_Connection Service
                                }catch (IOException e){
                                    Log.e(TAG,"LastCon error :"+ e.getMessage());
                                }
                            }
                        });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer isdelete = mydb.deleteAddressdata(LastAddress);
                        if(isdelete>0)
                            Log.d(TAG,"LastAddress Deleted Successfully");
                        else
                            Log.d(TAG,"LastAddress Delete Unsuccessful");
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }}
    public boolean LastConnect() //Function for fetching Last Address Stored in Database
    {
        if(mydb.check_db()) {
            DBHelper.DbResponse cursor1 = mydb.getLastAddressdata();
            LastAddress = cursor1.name;
            device = cursor1.device;
            Log.d(TAG, "Last Address Received :" + cursor1.name + "and " + cursor1.device);
        }
        Log.d(TAG,"LastAddress: "+LastAddress);
        if (LastAddress != null) {
            Log.d(TAG, "Bool true");
            return true;

        } else {
            Log.d(TAG, "Bool false");
            return false;
        }
    }

    @Override
    protected void onDestroy() { //Unbinding All Services and BroadcastReceiver Activity Bounded With
        if(mBounded) {
            unbindService(mConnection);
            Log.d(TAG,"Unbinding Service from MAin Activity");

        }
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (RuntimeException e){
            Log.e(TAG,"broadcastreciever"+e.getMessage());
        }
        try {
            unregisterReceiver(broadcastReceiver2);
        } catch (RuntimeException e){
            Log.e(TAG,"broadcastreciever2"+e.getMessage());
        }
        try {
            unregisterReceiver(broadcastReceiver3);
        } catch (RuntimeException e){
            Log.e(TAG,"broadcastreciever3"+e.getMessage());
        }
        mydb.close();
        super.onDestroy();
    }



}