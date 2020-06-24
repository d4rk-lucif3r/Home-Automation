package com.lucifer.h_a_t_3.Bluetooth_Service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.lucifer.h_a_t_3.DatabaseHelper.DBHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Bt_connection extends Service  {
    String cn ="Connected";
    private BluetoothAdapter mBluetoothAdapter;
    public static final String B_DEVICE = "MY DEVICE";
    public static final String B_UUID = "00001101-0000-1000-8000-00805F9B34FB";
// 00000000-0000-1000-8000-00805f9b34fb
    public DBHelper mydb2;
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    private ConnectBtThread mConnectThread;
    private static ConnectedBtThread mConnectedThread;
    private String TAG = Bt_connection.class.getSimpleName();
    private static Handler mHandler = null;
    public static int mState = STATE_NONE;
    public static String deviceName;
    String dname;
    String address;
    BluetoothDevice device;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder(); //Declaring new Local Binder

    public  class LocalBinder extends Binder {
        public Bt_connection getServerInstance() {
            return Bt_connection.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mydb2 = new DBHelper(this);


        if(intent != null){
            Bundle bundle = intent.getExtras();
            address = bundle.getString("ADDRESS"); //Fetching address of bluetooth Device for New_Bt_Device and Paired_Device Fragment
            Log.e(TAG,"Address found :"+ address);
        }

        else{
            Log.e(TAG,"Intent address not found");
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        connectToDevice(address);


        dname =device.getName();
        boolean isInserted = mydb2.insertAddressData(address,dname); //Inserting Address into database for faster connection through MainActivity

        if (isInserted){

            Log.d(TAG,"Inserted data:"+ address +"And "+deviceName);}
        else{
            Log.d(TAG,"Insertion Error");
        }

        return START_STICKY;
    }

    private synchronized void connectToDevice(String macAddress){
         device = mBluetoothAdapter.getRemoteDevice(macAddress);
         Log.d(TAG,"connectToDevice : device created "+device);
        if (mState == STATE_CONNECTING){
            if (mConnectThread != null){
                mConnectThread.cancel();
                Log.d(TAG,"syncConnectToDevice: STATE CONNECTING");
                mConnectThread = null;
            }
        }
        if (mConnectedThread != null){
            mConnectedThread.cancel();
            Log.d(TAG,"ConnectToDevice: mConected not null");
            mConnectedThread = null;
        }
        Log.d(TAG,"ConnectToDevice: ConnectBtThread Called");
        mConnectThread = new ConnectBtThread(device);
        Log.d(TAG,"ConnectBtThread called");
        String dname = mBluetoothAdapter.getRemoteDevice(address).getName();
        Intent in = new Intent();
        in.putExtra("Connecting","Connecting" +" to " +"\t"+dname+"\t"+address); //Sending Data to Broadcast Receiver in
                                                                                             // DeviceList Activity and MainActivity
        in.setAction("Connecting");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);

        mConnectThread.start();
        setState(STATE_CONNECTING);
    }
    private void setState(int state){
        mState = state;
    }
    public synchronized void stop(){
        setState(STATE_NONE);
        if (mConnectThread != null){
            mConnectThread.cancel();
            Log.d(TAG,"syncStop: STATE CONNECTING");
            mConnectThread = null;
        }
        if (mConnectedThread != null){
            mConnectedThread.cancel();
            Log.d(TAG,"syncStop: mConnected not null");
            mConnectedThread = null;
        }
        if (mBluetoothAdapter != null){
            mBluetoothAdapter.cancelDiscovery();
        }

        stopSelf();
    }

    public void sendData(String message)
    {
        if (mConnectedThread!= null){
            mConnectedThread.write(message.getBytes());
            Log.d(TAG,"sent data :"+"\t"+ message);
        }else {
          Toast.makeText(Bt_connection.this,"Failed to send data",Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Fail to send data :"+"\t"+ message);
        }
    }
    public void connectLast(String add) throws IOException{
        String AddresLast = add;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        address = AddresLast;
        connectToDevice(address); //connecting to Last PAired Address. Called from Mainactivity
    }

    @Override
    public boolean stopService(Intent name) {
        setState(STATE_NONE);

        if (mConnectThread != null){
            mConnectThread.cancel();
            Log.d(TAG,"StopService: mConnect not null");
            mConnectThread = null;
        }

        if (mConnectedThread != null){
            mConnectedThread.cancel();
            Log.d(TAG,"stopService: mConected not null");
            mConnectedThread = null;
        }

        mBluetoothAdapter.cancelDiscovery();
        return super.stopService(name);
    }

    private class ConnectBtThread extends Thread{
        private final BluetoothSocket mSocket;
        private final BluetoothDevice mDevice;

        public ConnectBtThread(BluetoothDevice device){
            mDevice = device;
            deviceName = mDevice.getName();
            BluetoothSocket socket = null;
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(B_UUID)); //creating Insecure RfComm Connection
                Log.d(TAG,"ConnectBtThread: Socket Created on:"+B_UUID);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"ConnectBTThread :"+e.getMessage());
            }
            mSocket = socket;
        }

        @Override
        public void run() {
            mBluetoothAdapter.cancelDiscovery();

                try{
                    mSocket.connect();
                    mConnectedThread = new ConnectedBtThread(mSocket);
                    mConnectedThread.start();
                    Log.d(TAG,"ConnectBTThread run method: connecting socket");
                }catch (IOException e){
                    try{
                        Log.d(TAG,"ConnectBTThread run method: msocket  null");
                        mSocket.close();
                        Log.d(TAG,"ConnectBTThread run method: closing msocket ");
//                        toast("re connecting");
                        Log.d(TAG,"ConnectBTThread run method: re Connecting");

                    }catch (IOException e2){
                        Log.e(TAG,e2.getMessage());
                    }
                    Intent in = new Intent();
                    in.putExtra("Error_Connecting","Error Connecting" +" to " +"\t"+deviceName+"\t"+address);  //Sending Data to Broadcast Receiver in
                                                                                                                           // DeviceList Activity and MainActivity
                    in.setAction("Error_Connecting");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
                    Log.e(TAG,"ConnectBtThread run method:"+e.getMessage());

                }

        }

        public void cancel(){

            try {
                mSocket.close();
                Log.d(TAG,"connectThread cancel method: closing Socket");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG,"connectThread cancel method:"+e.getMessage());
            }
        }
    }

   private class ConnectedBtThread extends Thread{
        private final BluetoothSocket cSocket;
        private final InputStream inS;
        private final OutputStream outS;

        private byte[] buffer;

        public ConnectedBtThread(BluetoothSocket socket){

            cSocket = socket;
            Log.e(TAG,"Connected thread "+cSocket);
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                Log.d(TAG,"Input stream transferred to tmpin : ConnectedBtThread :"+tmpIn );
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"ConnectedBtthread :"+e.getMessage());
            }

            try {
                tmpOut = socket.getOutputStream();
                Log.d(TAG,"Output stream transferred to tmpout : ConnectedBtThread :"+ tmpOut);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"ConnectedBtThread  :"+e.getMessage());
            }

            inS = tmpIn;
            outS = tmpOut;
        }

        @Override
        public void run() {
            buffer = new byte[1024];
            Intent in = new Intent();
            in.putExtra("Connected", cn + " to " + "\t" + deviceName + "\t" + address);  //Sending Data to Broadcast Receiver in
                                                                                                     // DeviceList Activity and MainActivity
            in.setAction("Connected");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
            Log.d(TAG, "connectedBTThread run method");
            Log.d(TAG, "Connected to :" + address);
            device = mBluetoothAdapter.getRemoteDevice(address);
        }


        public void write(byte[] buff){ //Sending Data Over Bluetooth
            try {
                outS.write(buff);
                Log.d(TAG,"Write : ConnectedBtThread"+buff);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"ConnectedBtthread write method : "+e.getMessage());
            }
        }

        private void cancel(){
            try {
                cSocket.close();
                Log.d("service","connected thread cancel method");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"ConnectedBtThread cancel:"+e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        this.stop();
        super.onDestroy();
    }}


