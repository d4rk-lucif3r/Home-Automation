package com.lucifer.h_a_t_3.Fragments.Functional_Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import com.lucifer.h_a_t_3.Bluetooth_Service.Bt_connection;
import com.lucifer.h_a_t_3.Fragments.BlankFragment;
import com.lucifer.h_a_t_3_github.R;


public class New_Bt_Device_Fragment extends Fragment  {
    Button close_list , show;
    ListView devicelist;

    String address;


    private BluetoothAdapter mBluetoothAdapter = null;

    public Boolean mbounded = false;
    public ArrayList<String> mBTDevices = new ArrayList<String>();
    private String TAG = New_Bt_Device_Fragment.class.getSimpleName();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBTDevices = new ArrayList<String>();

        return inflater.inflate(R.layout.fragment_new_bt_device, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        devicelist =getView().findViewById(R.id.paired_device);
        close_list = getView().findViewById(R.id.close_list);
        show= getView().findViewById(R.id.show_device);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        if(mBluetoothAdapter == null) //Checking For Bluetooth Adapter
        {
            Toast.makeText(getContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        else if(!mBluetoothAdapter.isEnabled())
        {

            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);

        }
        if(!isLocationEnabled(getContext())) //Checking if Location Services Enabled on device or not
        {
            LocationEnable();
        }


        close_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1;
                fragment1 = new BlankFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
                ft.replace(R.id.frag_1, fragment1);
                ft.commit();
                mBluetoothAdapter.cancelDiscovery();



            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDiscover();
            }
        });
    }








    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
          Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);// Displaying Nearby Devices in ListView
                mBTDevices.add( device.getName() + "\n" + device.getAddress());
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                final ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, mBTDevices);
                devicelist.setAdapter(adapter);
                devicelist.setOnItemClickListener(myListClickListener);
                mbounded = true;
            }
        }
    };
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);

            Intent intent = new Intent(getActivity(), Bt_connection.class);
            Bundle b = new Bundle();
            b.putString("ADDRESS", address); //Sending Device Address to BT_connection Service so that connection Could be made with the device
            intent.putExtras(b);
            getActivity().startService(intent);


        }
    };


    public void btnDiscover() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();

        }
        if(!mBluetoothAdapter.isDiscovering()){
            Log.d(TAG,"enabling discovery");
            //check BT permissions in manifest
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();

        }
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(broadcastReceiver, discoverDevicesIntent);
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = getActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += getActivity().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
    private boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }

    public void LocationEnable(){ //Enabling Location Services for discovering Nearby Bluetooth Devices
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
        builder.setTitle("Location Access Needed!");
        builder.setMessage("To search for Nearby Bluetooth Android Needs Access Location Service. Would You Like To Turn Them On? ");
        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"yes pressed ");
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);

                    }
                });
        builder.setNegativeButton(R.string.no,null);

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onDestroy() { //Unregistering all the bounded Services and Broadcast Receivers
        mBluetoothAdapter.cancelDiscovery();
        if(mbounded)
            getActivity().unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
}