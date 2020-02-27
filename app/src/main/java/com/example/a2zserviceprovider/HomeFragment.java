package com.example.a2zserviceprovider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment implements View.OnClickListener{
    Button button_ac_repair,button_plumber,button_carpenter,button_painter,button_tutor;
    private FirebaseDatabase database;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Retrieving instance of project connected firebase database
        //database=FirebaseDatabase.getInstance();
        button_ac_repair = root.findViewById(R.id.ac_repair);
        button_ac_repair.setOnClickListener(this);
        button_plumber = root.findViewById(R.id.plumber);
        button_plumber.setOnClickListener(this);
        button_painter = root.findViewById(R.id.painter);
        button_painter.setOnClickListener(this);
        return root;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ac_repair: {
                Intent intent_ac_repair = new Intent(getContext(), AcRepairActivity.class);
                startActivity(intent_ac_repair);
                break;
            }
            case R.id.plumber: {
                Intent intent_plumber = new Intent(getContext(), PlumberActivity.class);
                startActivity(intent_plumber);
                break;
            }
            case R.id.painter:
            {
                //DatabaseReference mRef = database.getReference("Name");
                //mRef.setValue("Deepak");

            }
        }
    }
}
