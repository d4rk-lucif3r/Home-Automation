package com.lucifer.h_a_t_3.Fragments.Functional_Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lucifer.h_a_t_3.Interface.FragmentToActivity;
import com.lucifer.h_a_t_3.Fragments.BlankFragment;
import com.lucifer.h_a_t_3_github.R;


public class Schedule_Fragment extends Fragment { //Fragment used for Schedule tasking **Disabled In this Build**
    Button close,add;
    TextView tvw;
    private String TAG = Schedule_Fragment.class.getSimpleName();
    private FragmentToActivity mCallback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }

    }

    private void sendData(Integer Hour, Integer Minute,String am_pm)
    {
        mCallback.communicate(Hour,Minute,am_pm);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_schedule_, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final TimePicker picker=(TimePicker)getView().findViewById(R.id.datePicker1);
        picker.setIs24HourView(true);

        close = getView().findViewById(R.id.Close_schedule);
        add = getView().findViewById(R.id.done);
        tvw = getView().findViewById(R.id.Schedule_tvw);
        super.onViewCreated(view, savedInstanceState);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = picker.getHour();
                    minute = picker.getMinute();
                }
                else{
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                tvw.setText("Selected Date: "+ hour +":"+ minute+" "+am_pm);
                sendData(hour,minute,am_pm);
                Log.d(TAG,"Time data Sent :"+hour+":" +minute+"" +am_pm);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}