package com.lucifer.h_a_t_4.Fragments.Functional_Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lucifer.h_a_t_4.R;


public class AppConfigureFragmnet extends Fragment {
        Button confbutton;
        EditText confEditText;

    private String TAG = AppConfigureFragmnet.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_app_configure, container, false);
        confbutton = view.findViewById(R.id.confbutton);
        confEditText = view.findViewById(R.id.confEditText);




        return view;
    }
}