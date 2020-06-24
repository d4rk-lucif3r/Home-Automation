package com.lucifer.h_a_t_3.Fragments.Functional_Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.lucifer.h_a_t_3_github.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerDialog extends DialogFragment //Timer AlertDialog **Disabled in this Build
{

    private static final String TIME = "sec";

    private TextView mTime;
    private TimerTask mTimerTask;
    private long mSec;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Timer");
        alert.setPositiveButton("OK", null);

        View view = View.inflate(getActivity(), R.layout.timer_dialog, null);
        mTime = (TextView) view.findViewById(R.id.time);

        alert.setView(view);
        return alert.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mSec = savedInstanceState.getLong(TIME);
            mTime.setText(String.valueOf(mSec));
        }
        startTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TIME, mSec);
    }

    private void startTimer() {
        Timer t = new Timer();

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mSec++;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTime.setText(String.valueOf(mSec));
                    }
                });
            }
        };

        t.scheduleAtFixedRate(mTimerTask, 1000, 1000);
    }

    private void stopTimer() {
        mTimerTask.cancel();
    }
}