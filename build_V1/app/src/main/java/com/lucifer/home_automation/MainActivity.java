package com.lucifer.home_automation;


import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import java.io.IOException;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private Button Connect;
    private Button Disconnect;
    private Button Device_1;
    private Button Device_2;
    private Button Device_3;
    private Button Device_4;
    private Button Default;
    String address = "00:18:E4:36:59:38"; /*address for Bluetooth*/
    BluetoothAdapter Bluetooth_adapter;
    BluetoothSocket Bt_Socket;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Invocation();
        listeners();


    }

    public void Invocation() {
        Connect = findViewById(R.id.connect);
        Disconnect = findViewById(R.id.disconnect);
        Device_1 = findViewById(R.id.Dev1);
        Device_2 = findViewById(R.id.Dev2);
        Device_3 = findViewById(R.id.Dev3);
        Device_4 = findViewById(R.id.Dev4);

        Default = findViewById(R.id.off);
        Bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        if (Bluetooth_adapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        } else if (!Bluetooth_adapter.isEnabled()) {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }
    }

    public void listeners() {
        Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Connect_bt();
                    Connect.setBackgroundResource(R.drawable.green);
                    Connect.setText(R.string.Connected);
                    Disconnect.setBackgroundResource(R.drawable.default_button);
                    Disconnect.setText(R.string.Disconnect);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Disconnect_bt();
                    Disconnect.setBackgroundResource(R.drawable.red);
                    Disconnect.setText(R.string.Disconnected);
                    Connect.setBackgroundResource(R.drawable.default_button);
                    Connect.setText(R.string.Connect_Again);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Device_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device_input(1);
                Device_1.setBackgroundResource(R.drawable.green);
                Device_1.setText(R.string.Online);


            }
        });
        Device_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Device_input(5);
                Device_1.setBackgroundResource(R.drawable.red);
                Device_1.setText(R.string.Device_1_offline);
                return true;
            }
        });
        Device_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device_input(2);

                Device_2.setBackgroundResource(R.drawable.green);
                Device_2.setText(R.string.Online);
            }
        });
        Device_2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Device_input(6);
                Device_2.setBackgroundResource(R.drawable.red);
                Device_2.setText(R.string.Device_2_offline);
                return true;
            }
        });
        Device_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device_input(3);
                Device_3.setBackgroundResource(R.drawable.green);
                Device_3.setText(R.string.Online);
            }
        });
        Device_3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Device_input(7);
                Device_3.setBackgroundResource(R.drawable.red);
                Device_3.setText(R.string.Device_3_offline);
                return true;
            }
        });
        Device_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device_input(4);
                Device_4.setBackgroundResource(R.drawable.green);
                Device_4.setText(R.string.Online);


            }
        });
        Device_4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Device_input(8);
                Device_4.setBackgroundResource(R.drawable.red);
                Device_4.setText(R.string.Device_4_offline);
                return true;
            }
        });
        Default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device_input(0);
                Device_1.setBackgroundResource(R.drawable.red);
                Device_1.setText(R.string.Device_1_offline);
                Device_2.setBackgroundResource(R.drawable.red);
                Device_2.setText(R.string.Device_2_offline);
                Device_3.setBackgroundResource(R.drawable.red);
                Device_3.setText(R.string.Device_3_offline);
                Device_4.setBackgroundResource(R.drawable.red);
                Device_4.setText(R.string.Device_4_offline);
            }
        });
    }

    public void Connect_bt() throws IOException {
        Bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice disp = Bluetooth_adapter.getRemoteDevice(address);
        Bt_Socket = disp.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        Bt_Socket.connect();//start connection
        Toast.makeText(this, "Bluetooth Connected", Toast.LENGTH_SHORT).show();
    }

    public void Device_input(int s) {
        switch (s) {
            case 0:
                if (Bt_Socket != null) {
                    try {
                        String str = "0"; // All Relays off
                        Bt_Socket.getOutputStream().write(str.getBytes());


                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case 1:
                if (Bt_Socket != null) {
                    try {
                        String str1 = "1"; // Device 1 Switches On
                        Bt_Socket.getOutputStream().write(str1.getBytes());

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 2:
                if (Bt_Socket != null) {
                    try {
                        String str2 = "2";  // Device 2 Switches On
                        Bt_Socket.getOutputStream().write(str2.getBytes());

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 3:
                if (Bt_Socket != null) {
                    try {
                        String str3 = "3"; // Device 3 Switches On
                        Bt_Socket.getOutputStream().write(str3.getBytes());

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 4:
                if (Bt_Socket != null) {
                    try {
                        String str4 = "4"; // Device 4 Switches On
                        Bt_Socket.getOutputStream().write(str4.getBytes());


                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 5:
                if (Bt_Socket != null) {
                    try {
                        String str5 = "a"; // Device 1 Switches Off
                        Bt_Socket.getOutputStream().write(str5.getBytes());


                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 6:
                if (Bt_Socket != null) {
                    try {
                        String str6 = "b"; // Device 2 Switches Off
                        Bt_Socket.getOutputStream().write(str6.getBytes());


                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 7:
                if (Bt_Socket != null) {
                    try {
                        String str7 = "c"; // Device 3 Switches Off
                        Bt_Socket.getOutputStream().write(str7.getBytes());


                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 8:
                if (Bt_Socket != null) {
                    try {
                        String str8 = "d"; // Device 4 Switches Off
                        Bt_Socket.getOutputStream().write(str8.getBytes());


                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }

    }
    public void Disconnect_bt() throws IOException {
        Bt_Socket.close();
        Toast.makeText(this, "Bluetooth Disconnected", Toast.LENGTH_SHORT).show();
    }




}
