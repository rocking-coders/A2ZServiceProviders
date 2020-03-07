package com.example.a2zserviceprovider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AcRepairTechniciansFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root  = inflater.inflate(R.layout.fragment_ac_repair_technicians, container, false);
        Bundle b= this.getArguments();
        ArrayList<String> technicians =b.getStringArrayList("Technicians");
        //Log.d("test1", String.valueOf(technicians));
        return root;
    }
}
