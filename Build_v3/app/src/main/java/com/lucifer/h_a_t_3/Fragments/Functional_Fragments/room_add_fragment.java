package com.lucifer.h_a_t_3.Fragments.Functional_Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lucifer.h_a_t_3.DatabaseHelper.DBHelper;
import com.lucifer.h_a_t_3.Fragments.BlankFragment;
import com.lucifer.h_a_t_3_github.R;


public class room_add_fragment extends Fragment { //Fragment used to add new Room Name in Database

public DBHelper mydb;
Button add, close;

EditText add_edit;
    private String TAG = room_add_fragment.class.getSimpleName();





    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_add, container, false);
        add=view.findViewById(R.id.add);
        mydb = new DBHelper(getContext());
        close =view.findViewById(R.id.close);
        add_edit = view.findViewById(R.id.edit_add);
        add.setEnabled(false);

        add_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0) {
                    add.setEnabled(false);
                    Toast.makeText(getActivity(), "RoomName Cannot Be Blanked",
                            Toast.LENGTH_SHORT).show();
                } else {
                    add.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean isInserted = mydb.insertRoomData(add_edit.getText().toString());

                if (isInserted) {

                    Log.d(TAG, "Inserted data:" + add_edit.getText().toString());
                    Toast.makeText(getContext(), "Room Name Inserted", Toast.LENGTH_SHORT).show();
                }

                else{
                    Log.d(TAG,"Insertion Error");
                    Toast.makeText(getContext(), "Insertion Error. Try Again!", Toast.LENGTH_SHORT).show();
                }

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
        return view;
    }
}