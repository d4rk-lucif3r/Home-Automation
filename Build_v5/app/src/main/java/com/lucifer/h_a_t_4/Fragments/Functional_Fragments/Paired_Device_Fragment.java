package com.lucifer.h_a_t_4.Fragments.Functional_Fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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


import com.lucifer.h_a_t_4.Services.Bt_connection;
import com.lucifer.h_a_t_4.Fragments.BlankFragment;
import com.lucifer.h_a_t_4.R;


import java.util.Set;

public class Paired_Device_Fragment extends Fragment  {
    Button close_list , show;
    ListView devicelist;
    Button test;
    String address;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothAdapter mBluetoothAdapter;
    ArrayList list = new ArrayList();

    public ArrayList<String> mBTDevices = new ArrayList<String>();
    private String TAG = New_Bt_Device_Fragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBTDevices = new ArrayList<String>();

        return inflater.inflate(R.layout.fragment_paired_device_, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        devicelist =getView().findViewById(R.id.paired_device);
        close_list = getView().findViewById(R.id.close_list);
        show= getView().findViewById(R.id.show_device);
        myBluetooth = BluetoothAdapter.getDefaultAdapter(); //Declaring Bluetooth Adapter




        if(myBluetooth == null)
        {

            Toast.makeText(getContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();


            getActivity().finish();
        }
        else if(!myBluetooth.isEnabled())
        {

            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
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



            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 pairedDevicesList();


            }
        });
    }



    public void pairedDevicesList( ) //Displaying bonded device in a list
    {
        pairedDevices = myBluetooth.getBondedDevices(); //Getting Bonded Devices



        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address


            }
        }
        else
        {
            Toast.makeText(getContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }


    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) { //Declaring On item click listener which sends address of the device
            //clicked

            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17); // Get the device MAC address, the last 17 chars in the View

            Intent intent = new Intent(getActivity(), Bt_connection.class);
            Bundle b = new Bundle();
            b.putString("ADDRESS", address);
            intent.putExtras(b);
            getActivity().startService(intent);


        }
    };





}