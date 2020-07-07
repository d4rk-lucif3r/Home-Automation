package com.lucifer.h_a_t_4.Fragments.UI_Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lucifer.h_a_t_4.Services.Bt_connection;
import com.lucifer.h_a_t_4.R;


import java.util.Calendar;

import static android.content.Context.BIND_AUTO_CREATE;


public class DeviceControlFragment extends Fragment {
    Switch dev1, dev2, dev3, dev4;
    Button close;
    boolean mBounded;
    private static DeviceControlFragment instance = null;


    private String TAG = DeviceControlFragment.class.getSimpleName();

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String dev1_prefs = "Dev1" ;
    public static final String dev2_prefs = "Dev2";
    public static final String dev3_prefs = "Dev3";
    public static final String dev4_prefs = "Dev4";
    public static final String Send_On_prefs_1 = "Send_on_1";
    public static final String Send_Off_prefs_1 = "Send_off_1";
    public static final String Send_On_prefs_2 = "Send_on_2";
    public static final String Send_Off_prefs_2 = "Send_off_2";
    public static final String Send_On_prefs_3 = "Send_on_3";
    public static final String Send_Off_prefs_3 = "Send_off_3";
    public static final String Send_On_prefs_4 = "Send_on_4";
    public static final String Send_Off_prefs_4 = "Send_off_4";

    boolean alert1,alert2,alert3,alert4 ;
    SharedPreferences sharedpreferences;
    String CallData;


    PendingIntent myPendingIntent1,myPendingIntent2,myPendingIntent3,myPendingIntent4;

    PendingIntent myPendingIntent_off1,myPendingIntent_off2,myPendingIntent_off3,myPendingIntent_off4;

    AlarmManager alarmManager;
    AlarmManager alarmManager_off;
    BroadcastReceiver myBroadcastReceiver_on;
    BroadcastReceiver myBroadcastReceiver_off;

    Calendar firingCal;
    Calendar firingCal_off;
    boolean alarm_on = false;
    boolean alarm_off =false;


    int mHour , mMinute;


    public Bt_connection mServer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Device Control created");
        Intent mIntent = new Intent(getContext(), Bt_connection.class);


        Log.d(TAG,"data recieved for onclick listner"+CallData);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        alert1 = sharedpreferences.getBoolean("Done1",false);
        alert2 =sharedpreferences.getBoolean("Done2",false);
        alert3 = sharedpreferences.getBoolean("Done3",false);
        alert4 = sharedpreferences.getBoolean("Done4",false);

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
        Log.d(TAG,"invocation called");
        dev1 = getView().findViewById(R.id.Dev_1);
        dev2 = getView().findViewById(R.id.Dev_2);
        dev4 = getView().findViewById(R.id.Dev_3);
        dev3 = getView().findViewById(R.id.Dev_4);
        close = getView().findViewById(R.id.close_device_control);

        String value1= sharedpreferences.getString(dev1_prefs, "Device 1");
        dev1.setText(value1);

        String value2= sharedpreferences.getString(dev2_prefs, "Device 2");

        dev2.setText(value2);

        String value3= sharedpreferences.getString(dev3_prefs, "Device 3");
        dev3.setText(value3);

        String value4= sharedpreferences.getString(dev4_prefs, "Device 4");
        dev4.setText(value4);


    }

    public void listeners() {
     CallData =getArguments().getString("Call");
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




        dev1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                    alert.setTitle("Button 1 Extra Settings ");
                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    final TextView Button_Name = new TextView(getContext());
                    layout.addView(Button_Name);
                    String value1= sharedpreferences.getString(dev1_prefs,"Device 1");

                        Button_Name.setText(String.format("Name: %s", value1));
                    final TextView Send_on = new TextView(getContext());
                    layout.addView(Send_on);
                    String sendon= sharedpreferences.getString(Send_On_prefs_1, "Null");

                        Send_on.setText(String.format("Switch on Data: %s", sendon));
                    final TextView Send_off = new TextView(getContext());
                    layout.addView(Send_off);
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_1, "Null");

                        Send_off.setText(String.format("Switch off Data: %s", sendoff));
                    alert.setView(layout);
                    alert.setPositiveButton("Edit Button Configurations", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                            alert.setTitle("Button 1 Configuration ");
                            alert.setMessage("If You Don't What These Are Please Don't Play With These!!. Else Your Device Wouldn't Work");

                            LinearLayout layout = new LinearLayout(getContext());
                            layout.setOrientation(LinearLayout.VERTICAL);
                            final EditText Button_Name = new EditText(getContext());
                            layout.addView(Button_Name);
                            String value1= sharedpreferences.getString(dev1_prefs,"Enter Button name");
                            if(!alert1)
                                Button_Name.setHint(value1);
                            else if(alert1)
                                Button_Name.setText(value1);
                            final EditText Send_on = new EditText(getContext());
                            layout.addView(Send_on);
                            String sendon= sharedpreferences.getString(Send_On_prefs_1, "Data for switching Appliance on");
                            if(!alert1)
                                Send_on.setHint(sendon);
                            else if( alert1)
                                Send_on.setText(sendon);
                            final EditText Send_off = new EditText(getContext());
                            layout.addView(Send_off);
                            String sendoff= sharedpreferences.getString(Send_Off_prefs_1, "Data for switching appliance off");
                            if(!alert1)
                                Send_off.setHint(sendoff);
                            else if(alert1)
                                Send_off.setText(sendoff);
                            alert.setView(layout);
                            alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(dev1_prefs,Button_Name.getText().toString());
                                    editor.apply();
                                    String value= sharedpreferences.getString(dev1_prefs, "Device 1");
                                    dev1.setText(value);


                                    editor.putString(Send_On_prefs_1,Send_on.getText().toString());
                                    editor.apply();

                                    editor.putString(Send_Off_prefs_1,Send_off.getText().toString());
                                    editor.apply();
                                    editor.putBoolean("Done1",true);
                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });alert.show();

                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.setNeutralButton("Schedule", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                            builder.setTitle("Device Scheduling");
                            builder.setMessage("Would you like to schedule device to turn on or off");
                            builder.setPositiveButton("Schedule On",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.d(TAG,"positive clicked");
                                            final Calendar c = Calendar.getInstance();
                                            mHour = c.get(Calendar.HOUR_OF_DAY);
                                            mMinute = c.get(Calendar.MINUTE);
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.AlertDialogCustom,
                                                    new TimePickerDialog.OnTimeSetListener() {
                                                        @Override
                                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                                              int minute) {
                                                            Log.d(TAG,"time :"+hourOfDay+" : "+minute);
                                                            Integer AM_PM ;
                                                            if(hourOfDay < 12) {
                                                                AM_PM = Calendar.AM;
                                                                Log.d(TAG,"setting am");
                                                            } else {
                                                                AM_PM = Calendar.PM;
                                                                Log.d(TAG,"setting pm");
                                                            }

                                                            schedule_on(hourOfDay,minute,1,AM_PM);
                                                        }
                                                    }, mHour, mMinute, false);
                                            timePickerDialog.show();
                                        }
                                    });
                            builder.setNegativeButton("Schedule off", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG,"negative clicked");
                                    final Calendar c = Calendar.getInstance();
                                    mHour = c.get(Calendar.HOUR_OF_DAY);
                                    mMinute = c.get(Calendar.MINUTE);
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.AlertDialogCustom,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                                      int minute) {
                                                    Integer AM_PM ;
                                                    if(hourOfDay < 12) {
                                                        AM_PM = Calendar.AM;
                                                    } else {
                                                        AM_PM = Calendar.PM;
                                                    }
                                                    Log.d(TAG,"time :"+hourOfDay+" : "+minute +":"+AM_PM);

                                                    schedule_off(hourOfDay,minute,1,AM_PM);
                                                }
                                            }, mHour, mMinute, false);
                                    timePickerDialog.show();
                                }
                            });
                            builder.show();
                        }
                    });
                    alert.show();
                    return true;
            }
        });

        dev2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                alert.setTitle("Button 2 Extra Settings ");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView Button_Name = new TextView(getContext());
                layout.addView(Button_Name);
                String value1= sharedpreferences.getString(dev2_prefs,"Device 2");

                    Button_Name.setText(String.format("Name: %s", value1));

                final TextView Send_on = new TextView(getContext());
                layout.addView(Send_on);
                String sendon= sharedpreferences.getString(Send_On_prefs_2, "Null");

                 Send_on.setText(String.format("Switch on Data : %s", sendon));

                final TextView Send_off = new TextView(getContext());
                layout.addView(Send_off);
                String sendoff= sharedpreferences.getString(Send_Off_prefs_2, "Null");


                    Send_off.setText(String.format("Switch off Data : %s", sendoff));


                alert.setView(layout);

                alert.setPositiveButton("Edit Button Configurations", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                        alert.setTitle("Button 2 Configuration ");
                        alert.setMessage("If You Don't What These Are Please Don't Play With These!!. Else Your Device Wouldn't Work");

                        LinearLayout layout = new LinearLayout(getContext());
                        layout.setOrientation(LinearLayout.VERTICAL);

                        final EditText Button_Name = new EditText(getContext());
                        layout.addView(Button_Name);
                        String value1= sharedpreferences.getString(dev2_prefs,"Enter Button name");
                        if(!alert2)
                            Button_Name.setHint(value1);
                        else if(alert2)
                            Button_Name.setText(value1);
                        final EditText Send_on = new EditText(getContext());
                        layout.addView(Send_on);
                        String sendon= sharedpreferences.getString(Send_On_prefs_2, "Data for switching Appliance on");
                        if(!alert2)
                            Send_on.setHint(sendon);
                        else if(alert2)
                            Send_on.setText(sendon);

                        final EditText Send_off = new EditText(getContext());
                        layout.addView(Send_off);
                        String sendoff= sharedpreferences.getString(Send_Off_prefs_2, "Data for switching appliance off");

                        if(!alert2)
                            Send_off.setHint(sendoff);
                        else if(alert2)
                            Send_off.setText(sendoff);


                        alert.setView(layout);

                        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(dev2_prefs,Button_Name.getText().toString());
                                editor.apply();
                                String value2= sharedpreferences.getString(dev2_prefs, "Device 1");
                                dev2.setText(value2);

                                editor.putString(Send_On_prefs_2,Send_on.getText().toString());
                                editor.apply();

                                editor.putString(Send_Off_prefs_2,Send_off.getText().toString());
                                editor.apply();
                                editor.putBoolean("Done2",true);

                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });alert.show();

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.setNeutralButton("Schedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                        builder.setTitle("Device Scheduling");
                        builder.setMessage("Would you like to schedule device to turn on or off");
                        builder.setPositiveButton("Schedule On",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG,"positive clicked");
                                        final Calendar c = Calendar.getInstance();
                                        mHour = c.get(Calendar.HOUR_OF_DAY);
                                        mMinute = c.get(Calendar.MINUTE);
                                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.AlertDialogCustom,
                                                new TimePickerDialog.OnTimeSetListener() {

                                                    @Override
                                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                                          int minute) {
                                                        Log.d(TAG,"time :"+hourOfDay+" : "+minute );
                                                        Integer AM_PM ;
                                                        if(hourOfDay < 12) {
                                                            AM_PM = Calendar.AM;
                                                        } else {
                                                            AM_PM = Calendar.PM;
                                                        }
                                                        schedule_on(hourOfDay,minute,2,AM_PM);
                                                    }
                                                }, mHour, mMinute, false);
                                        timePickerDialog.show();




                                    }
                                });
                        builder.setNegativeButton("Schedule off", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"negative clicked");
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.AlertDialogCustom,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {
                                                Integer AM_PM ;
                                                if(hourOfDay < 12) {
                                                    AM_PM = Calendar.AM;
                                                } else {
                                                    AM_PM = Calendar.PM;
                                                }
                                                Log.d(TAG,"time :"+hourOfDay+" : "+minute +":"+AM_PM);

                                                schedule_off(hourOfDay,minute,2,AM_PM);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                            }
                        });

                        builder.show();
                    }


                });

                alert.show();
                return true;
            }
        });

        dev3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                alert.setTitle("Button 3 Extra Settings ");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView Button_Name = new TextView(getContext());
                layout.addView(Button_Name);
                String value1= sharedpreferences.getString(dev3_prefs,"Device 3");

                    Button_Name.setText(String.format("Name: %s", value1));
                final TextView Send_on = new TextView(getContext());
                layout.addView(Send_on);
                String sendon= sharedpreferences.getString(Send_On_prefs_3, "Null");

                    Send_on.setText(String.format("Switch on Data : %s", sendon));

                final TextView Send_off = new TextView(getContext());
                layout.addView(Send_off);
                String sendoff= sharedpreferences.getString(Send_Off_prefs_3, "Null");

                    Send_off.setText(String.format("Switch off Data : %s", sendoff));


                alert.setView(layout);

                alert.setPositiveButton("Edit Button Configurations", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                        alert.setTitle("Button 3 Configuration ");
                        alert.setMessage("If You Don't What These Are Please Don't Play With These!!. Else Your Device Wouldn't Work");

                        LinearLayout layout = new LinearLayout(getContext());
                        layout.setOrientation(LinearLayout.VERTICAL);

                        final EditText Button_Name = new EditText(getContext());
                        layout.addView(Button_Name);
                        String value1= sharedpreferences.getString(dev3_prefs,"Enter Button name");
                        if(!alert3)
                            Button_Name.setHint(value1);
                        else if(alert3)
                            Button_Name.setText(value1);
                        final EditText Send_on = new EditText(getContext());
                        layout.addView(Send_on);
                        String sendon= sharedpreferences.getString(Send_On_prefs_3, "Data for switching Appliance on");
                        if(!alert3)
                            Send_on.setHint(sendon);
                        else if(alert3)
                            Send_on.setText(sendon);

                        final EditText Send_off = new EditText(getContext());
                        layout.addView(Send_off);
                        String sendoff= sharedpreferences.getString(Send_Off_prefs_3, "Data for switching appliance off");

                        if(!alert3)
                            Send_off.setHint(sendoff);
                        else if(alert3)
                            Send_off.setText(sendoff);


                        alert.setView(layout);

                        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(dev3_prefs,Button_Name.getText().toString());
                                editor.apply();
                                String value3= sharedpreferences.getString(dev3_prefs, "Device 1");

                                dev3.setText(value3);


                                editor.putString(Send_On_prefs_3,Send_on.getText().toString());
                                editor.apply();

                                editor.putString(Send_Off_prefs_3,Send_off.getText().toString());
                                editor.apply();
                                editor.putBoolean("Done3",true);

                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });alert.show();

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.setNeutralButton("Schedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                        builder.setTitle("Device Scheduling");

                        builder.setMessage("Would you like to schedule device to turn on or off");
                        builder.setPositiveButton("Schedule On",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG,"positive clicked");
                                        final Calendar c = Calendar.getInstance();
                                        mHour = c.get(Calendar.HOUR_OF_DAY);
                                        mMinute = c.get(Calendar.MINUTE);
                                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.AlertDialogCustom,
                                                new TimePickerDialog.OnTimeSetListener() {

                                                    @Override
                                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                                          int minute) {
                                                        Integer AM_PM ;
                                                        if(hourOfDay < 12) {
                                                            AM_PM = Calendar.AM;
                                                        } else {
                                                            AM_PM = Calendar.PM;
                                                        }
                                                        Log.d(TAG,"time :"+hourOfDay+" : "+minute +":"+AM_PM);

                                                        schedule_on(hourOfDay,minute,3,AM_PM);
                                                    }
                                                }, mHour, mMinute, false);
                                        timePickerDialog.show();




                                    }
                                });
                        builder.setNegativeButton("Schedule off", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"negative clicked");
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.AlertDialogCustom,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {
                                                Integer AM_PM ;
                                                if(hourOfDay < 12) {
                                                    AM_PM = Calendar.AM;
                                                } else {
                                                    AM_PM = Calendar.PM;
                                                }
                                                Log.d(TAG,"time :"+hourOfDay+" : "+minute +":"+AM_PM);

                                                schedule_off(hourOfDay,minute,3,AM_PM);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                            }
                        });

                        builder.show();
                    }


                });

                alert.show();
                return true;
            }
        });

        dev4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                alert.setTitle("Button 4 Extra Settings ");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView Button_Name = new TextView(getContext());
                layout.addView(Button_Name);
                String value1= sharedpreferences.getString(dev4_prefs,"Device 4");

                    Button_Name.setText(String.format("Name: %s", value1));
                final TextView Send_on = new TextView(getContext());
                layout.addView(Send_on);
                String sendon= sharedpreferences.getString(Send_On_prefs_4, "Null");

                    Send_on.setText(String.format("Switch on Data : %s", sendon));

                final TextView Send_off = new TextView(getContext());
                layout.addView(Send_off);
                String sendoff= sharedpreferences.getString(Send_Off_prefs_4, "Null");


                    Send_off.setText(String.format("Switch off Data : %s", sendoff));


                alert.setView(layout);

                alert.setPositiveButton("Edit Button Configurations", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                        alert.setTitle("Button 4 Configuration ");
                        alert.setMessage("If You Don't What These Are Please Don't Play With These!!. Else Your Device Wouldn't Work");
                        LinearLayout layout = new LinearLayout(getContext());
                        layout.setOrientation(LinearLayout.VERTICAL);

                        final EditText Button_Name = new EditText(getContext());
                        layout.addView(Button_Name);
                        String value1= sharedpreferences.getString(dev4_prefs,"Enter Button name");
                        if(!alert4)
                            Button_Name.setHint(value1);
                        else if(alert4)
                            Button_Name.setText(value1);
                        final EditText Send_on = new EditText(getContext());
                        layout.addView(Send_on);
                        String sendon= sharedpreferences.getString(Send_On_prefs_4, "Data for switching Appliance on");
                        if(!alert4)
                            Send_on.setHint(sendon);
                        else if(alert4)
                            Send_on.setText(sendon);

                        final EditText Send_off = new EditText(getContext());
                        layout.addView(Send_off);
                        String sendoff= sharedpreferences.getString(Send_Off_prefs_4, "Data for switching appliance off");

                        if(!alert4)
                            Send_off.setHint(sendoff);
                        else if(alert4)
                            Send_off.setText(sendoff);


                        alert.setView(layout);

                        alert.setPositiveButton("Edit Button ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(dev4_prefs,Button_Name.getText().toString());
                                editor.apply();
                                String value4= sharedpreferences.getString(dev4_prefs, "Device 4");
                                dev4.setText(value4);


                                editor.putString(Send_On_prefs_4,Send_on.getText().toString());
                                editor.apply();

                                editor.putString(Send_Off_prefs_4,Send_off.getText().toString());
                                editor.apply();
                                editor.putBoolean("Done4",true);

                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });alert.show();

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.setNeutralButton("Schedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);
                        builder.setTitle("Device Scheduling");
                        builder.setMessage("Would you like to schedule device to turn on or off");
                        builder.setPositiveButton("Schedule On",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG,"positive clicked");
                                        final Calendar c = Calendar.getInstance();
                                        mHour = c.get(Calendar.HOUR_OF_DAY);
                                        mMinute = c.get(Calendar.MINUTE);
                                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.AlertDialogCustom,
                                                new TimePickerDialog.OnTimeSetListener() {

                                                    @Override
                                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                                          int minute) {
                                                        Integer AM_PM ;
                                                        if(hourOfDay < 12) {
                                                            AM_PM = Calendar.AM;
                                                        } else {
                                                            AM_PM = Calendar.PM;
                                                        }
                                                        Log.d(TAG,"time :"+hourOfDay+" : "+minute +":"+AM_PM);

                                                        schedule_on(hourOfDay,minute,4,AM_PM);
                                                    }
                                                }, mHour, mMinute, false);
                                        timePickerDialog.show();




                                    }
                                });
                        builder.setNegativeButton("Schedule off", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"negative clicked");
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.AlertDialogCustom,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {
                                                Integer AM_PM ;
                                                if(hourOfDay < 12) {
                                                    AM_PM = Calendar.AM;
                                                } else {
                                                    AM_PM = Calendar.PM;
                                                }
                                                Log.d(TAG,"time :"+hourOfDay+" : "+minute +":"+AM_PM);


                                                schedule_off(hourOfDay,minute,4,AM_PM);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                            }
                        });

                        builder.show();
                    }


                });

                alert.show();
                return true;
            }
        });




        dev1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    String sendon= sharedpreferences.getString(Send_On_prefs_1, "");
                    mServer.sendData(sendon);
                  Log.d(TAG,"On"+sendon);
                }
                else
                {
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_1, "");
                    mServer.sendData(sendoff);
                    Log.d(TAG,"Off"+sendoff);
                }
            }
        });
        dev2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    String sendon= sharedpreferences.getString(Send_On_prefs_2, "");
                    mServer.sendData(sendon);
                    Log.d(TAG,"On"+sendon);
                }
                else
                {
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_2, "");
                    mServer.sendData(sendoff);
                    Log.d(TAG,"Off"+sendoff);
                }
            }
        });
        dev3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    String sendon= sharedpreferences.getString(Send_On_prefs_3, "");
                    mServer.sendData(sendon);
                    Log.d(TAG,"On"+sendon);
                }
                else
                {
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_3, "");
                    mServer.sendData(sendoff);
                    Log.d(TAG,"Off"+sendoff);
                }
            }
        });
        dev4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    String sendon= sharedpreferences.getString(Send_On_prefs_4, "");
                    mServer.sendData(sendon);
                    Log.d(TAG,"On"+sendon);
                }
                else
                {
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_4, "");
                    mServer.sendData(sendoff);
                    Log.d(TAG,"Off"+sendoff);
                }
            }
        });
    }



    private void registerMyAlarmBroadcast(Integer Device_ID)
    {
        Log.i(TAG, "Going to register Intent.RegisterAlramBroadcast");
         final Integer D_id = Device_ID;
        alarm_on = true;
        Log.d(TAG,"devic id:"+D_id);
        myBroadcastReceiver_on = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if(D_id == 1){
                    String sendon= sharedpreferences.getString(Send_On_prefs_1, "");
                    mServer.sendData(sendon);
                    dev1.setChecked(true);
                    alarmManager.cancel(myPendingIntent1);
                    Log.d(TAG,"alarm data send to switch on to :"+ D_id);}
                if(D_id == 2){
                    String sendon= sharedpreferences.getString(Send_On_prefs_2, "");
                    mServer.sendData(sendon);
                    dev2.setChecked(true);
                    alarmManager.cancel(myPendingIntent2);
                    Log.d(TAG,"alarm data send to switch on to :"+ D_id);}
                if(D_id == 3){
                    String sendon= sharedpreferences.getString(Send_On_prefs_3, "");
                    mServer.sendData(sendon);
                    alarmManager.cancel(myPendingIntent3);
                    dev3.setChecked(true);
                    Log.d(TAG,"alarm data send to switch on to :"+ D_id);}
                if(D_id == 4){
                    String sendon= sharedpreferences.getString(Send_On_prefs_4, "");
                    mServer.sendData(sendon);
                    dev4.setChecked(true);
                    alarmManager.cancel(myPendingIntent4);
                    Log.d(TAG,"alarm data send to switch on to :"+ D_id);}


            }
        };



    }
    public  void schedule_on(Integer mHour , Integer mMinute ,Integer device_ID, Integer Am_Pm)
    {
        String am_pm;
        if(Am_Pm == Calendar.AM)
            am_pm = "AM" ;
        else
            am_pm = "PM";


        firingCal= Calendar.getInstance();

        Integer hour = 0;
        Log.d(TAG,"Schedule task called");
         if(mHour>12){
         hour = mHour -12;
             firingCal.set(Calendar.HOUR_OF_DAY, hour);}
        else{
            hour = mHour;
            firingCal.set(Calendar.HOUR , hour);
         }



        if(firingCal.get(Calendar.AM_PM) == Calendar.PM) {
            if (Am_Pm == Calendar.AM)
                firingCal.add(Calendar.DATE, 1);
        }





        firingCal.set(Calendar.MINUTE, mMinute);
        firingCal.set(Calendar.AM_PM,Am_Pm);
        Log.d(TAG,"Alarm setted on for "+ mHour +" : "+ mMinute +"");
        firingCal.set(Calendar.SECOND, 0);



        long intendedTime = firingCal.getTimeInMillis();
        Log.d(TAG,"Schedule on Intended TIme :"+ intendedTime + ":  "+ firingCal.getTime()+" 1: "+firingCal.get(Calendar.AM_PM)+" 2: "+firingCal.get(Calendar.HOUR_OF_DAY)+" 3: "+firingCal.get(Calendar.MINUTE));

        registerMyAlarmBroadcast(device_ID);

        alarmManager = (AlarmManager)(getActivity().getSystemService( Context.ALARM_SERVICE ));


        if(device_ID ==1){
        myPendingIntent1 = PendingIntent.getBroadcast( getActivity(), 0, new Intent("on1"),0 );
            getActivity().registerReceiver(myBroadcastReceiver_on, new IntentFilter("on1") );
        Log.d(TAG,"ON"+ device_ID);
            Toast.makeText(getContext(),"Going to turn on Device "+ device_ID +"  at "+ hour+":"+  mMinute +":"+ am_pm ,Toast.LENGTH_LONG).show();
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent1);}
        if(device_ID ==2){
            myPendingIntent2 = PendingIntent.getBroadcast( getActivity(), 0, new Intent("on2"),0 );
            getActivity().registerReceiver(myBroadcastReceiver_on, new IntentFilter("on2") );
            Toast.makeText(getContext(),"Going to turn on Device "+ device_ID +"  at "+ hour+":"+  mMinute +":"+ am_pm,Toast.LENGTH_LONG).show();
            Log.d(TAG,"ON"+ device_ID);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent2);}
        if(device_ID ==3){
            myPendingIntent3 = PendingIntent.getBroadcast( getActivity(), 0, new Intent("on3"),0 );
            Toast.makeText(getContext(),"Going to turn on Device "+ device_ID +"  at "+ hour+":"+  mMinute +":"+am_pm,Toast.LENGTH_LONG).show();
            getActivity().registerReceiver(myBroadcastReceiver_on, new IntentFilter("on3") );
            Log.d(TAG,"ON"+ device_ID);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent3);}
        if(device_ID ==4){
            myPendingIntent4 = PendingIntent.getBroadcast( getActivity(), 0, new Intent("on4"),0 );
            Toast.makeText(getContext(),"Going to turn on Device "+ device_ID +"  at "+ hour+":"+  mMinute +":"+ am_pm , Toast.LENGTH_LONG).show();
            getActivity().registerReceiver(myBroadcastReceiver_on, new IntentFilter("on4") ); Log.d(TAG,"ON"+ device_ID);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent4);}





    }

    private void registerMyAlarmBroadcast_off(Integer Device_ID)
    {
        Log.i(TAG, "Going to register Intent.RegisterAlramBroadcast offf");
        final Integer D_id= Device_ID;
        myBroadcastReceiver_off = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                alarm_off = true;
                Log.d(TAG,"BroadcastReceiver::off ::OnReceive()");

                if(D_id == 1){
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_1, "");
                    mServer.sendData(sendoff);
                    dev1.setChecked(false);
                    alarmManager_off.cancel(myPendingIntent_off1);

                Log.d(TAG,"alarm data send to switch off to :"+ D_id);}
                if(D_id == 2){
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_2, "");
                    mServer.sendData(sendoff);
                    alarmManager_off.cancel(myPendingIntent_off2);
                    dev2.setChecked(false);
                    Log.d(TAG,"alarm data send to switch off to :"+ D_id);}
                if(D_id == 3){
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_3, "");
                    mServer.sendData(sendoff);
                    dev3.setChecked(false);
                    alarmManager_off.cancel(myPendingIntent_off3);
                    Log.d(TAG,"alarm data send to switch off to :"+ D_id);}
                if(D_id == 4){
                    String sendoff= sharedpreferences.getString(Send_Off_prefs_4, "");
                    mServer.sendData(sendoff);
                    dev3.setChecked(false);
                    alarmManager_off.cancel(myPendingIntent_off4);
                    Log.d(TAG,"alarm data send to switch off to :"+ D_id);}



            }
        };



    }
    public  void schedule_off(Integer mHour , Integer mMinute ,Integer device_ID,Integer Am_Pm)
    {


        String am_pm;
        if(Am_Pm == Calendar.AM)
            am_pm = "AM" ;
        else
            am_pm = "PM";


        firingCal_off= Calendar.getInstance();
        int hour ;
        if(mHour>12){
            hour = mHour -12;
            firingCal_off.set(Calendar.HOUR_OF_DAY, hour);}
        else{
            hour = mHour;
            firingCal_off.set(Calendar.HOUR , hour);
        }



        if(firingCal_off.get(Calendar.AM_PM) == Calendar.PM) {
            if (Am_Pm == Calendar.AM)
                firingCal_off.add(Calendar.DATE, 1);

        }






        Log.d(TAG,"Schedule task off called");

        firingCal_off.set(Calendar.MINUTE, mMinute); // alarm minute
        firingCal_off.set(Calendar.AM_PM,Am_Pm);


        Log.d(TAG,"Alarm (off ) setted for "+hour+" : "+ mMinute +"");
        firingCal_off.set(Calendar.SECOND, 00); // and alarm second

        long intendedTime = firingCal_off.getTimeInMillis();
        Log.d(TAG,"Intended Time Schedule off :"+ intendedTime + ":  "+ firingCal_off.getTime()+" 1: "+firingCal_off.get(Calendar.AM_PM)+" 2: " +
                ""+firingCal_off.get(Calendar.HOUR_OF_DAY)+" 3: "+firingCal_off.get(Calendar.MINUTE));

        registerMyAlarmBroadcast_off(device_ID);
        alarmManager_off = (AlarmManager)(getActivity().getSystemService( Context.ALARM_SERVICE ));

        if(device_ID ==1){
            myPendingIntent_off1 = PendingIntent.getBroadcast( getActivity(), 0, new Intent("off1"),PendingIntent.FLAG_UPDATE_CURRENT );
            getActivity().registerReceiver(myBroadcastReceiver_off, new IntentFilter("off1") );
            alarmManager_off.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent_off1);
            Toast.makeText(getContext(),"Going to turn off Device "+ device_ID +"  at "+ hour+":"+  mMinute +":"+ am_pm  ,Toast.LENGTH_LONG).show();
            Log.d(TAG,"Off"+ device_ID);

        }
        if(device_ID ==2){
            myPendingIntent_off2 = PendingIntent.getBroadcast( getActivity(), 0, new Intent("off2"),PendingIntent.FLAG_UPDATE_CURRENT );
            getActivity().registerReceiver(myBroadcastReceiver_off, new IntentFilter("off2") );
            alarmManager_off.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent_off2);
            Toast.makeText(getContext(),"Going to turn off Device "+ device_ID +"  at "+ hour+":"+  mMinute +":"+ am_pm  ,Toast.LENGTH_LONG).show();
            Log.d(TAG,"Off"+ device_ID);
        }
        if(device_ID ==3){
            myPendingIntent_off3 = PendingIntent.getBroadcast( getActivity(), 0, new Intent("off3"),PendingIntent.FLAG_UPDATE_CURRENT);
            getActivity().registerReceiver(myBroadcastReceiver_off, new IntentFilter("off3") );
            alarmManager_off.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent_off3);
            Toast.makeText(getContext(),"Going to turn off Device "+ device_ID +"  at "+ hour+":"+  mMinute +":"+am_pm  ,Toast.LENGTH_LONG).show();
            Log.d(TAG,"Off"+ device_ID);
        }
        if(device_ID ==4){
            myPendingIntent_off4 = PendingIntent.getBroadcast( getActivity(), 0, new Intent("off4"),PendingIntent.FLAG_UPDATE_CURRENT );
            getActivity().registerReceiver(myBroadcastReceiver_off, new IntentFilter("off4") );
            alarmManager_off.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,intendedTime,myPendingIntent_off4);
            Toast.makeText(getContext(),"Going to turn off Device "+ device_ID +"  at "+ hour+":"+  mMinute +":"+ am_pm  ,Toast.LENGTH_LONG).show();
            Log.d(TAG,"Off"+ device_ID);
        }



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

    @Override
    public void onDetach() {
        Log.i(TAG,"Detached");
        super.onDetach();
    }
}
