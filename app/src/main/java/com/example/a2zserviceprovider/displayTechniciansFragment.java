package com.example.a2zserviceprovider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a2zserviceprovider.BackgroundWorkers.serviceRequest;

import java.util.ArrayList;

public class displayTechniciansFragment extends Fragment implements View.OnClickListener {
    RadioGroup radioGroup;
    RadioButton t1, t2, t3;
    Button Bsubmit;
    String technicianName, technicianEmail;
    String serviceType;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_technicians, container, false);
        Bundle b = this.getArguments();
        final ArrayList<String> technicians = b.getStringArrayList("Technicians");
        serviceType = b.getString("technicianType");
        Log.d("test4", serviceType);
        //removing arguments in bundle
        this.getArguments().remove("Technicians");

        radioGroup = root.findViewById(R.id.technicianGroup);
        t1 = root.findViewById(R.id.technician1);
        t2 = root.findViewById(R.id.technician2);
        t3 = root.findViewById(R.id.technician3);
        Bsubmit = root.findViewById(R.id.buttonRequest);
        Bsubmit.setOnClickListener(this);
        Log.d("debug", String.valueOf(technicians.size()));
        if (technicians.size() == 1) {
            //clearing services data cache
            SharedPreferences preferences = getActivity().getSharedPreferences("services Data",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
                Bsubmit.setVisibility(View.INVISIBLE);
                t1.setClickable(false);
                t2.setClickable(false);
                t3.setClickable(false);
                t1.setVisibility(View.INVISIBLE);
                t2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
                TextView t= root.findViewById(R.id.textView2);
                t.setText(getResources().getString(R.string.noTechnician));
            }
            else {
                    if(technicians.size()==2)
                    {
                        t1.setText(technicians.get(0));
                        t2.setVisibility(View.INVISIBLE);
                        t3.setVisibility(View.INVISIBLE);
                    }
                    else if(technicians.size()==4)
                    {
                        t1.setText(technicians.get(0));
                        t2.setText(technicians.get(2));
                        t3.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        t1.setText(technicians.get(0));
                        t2.setText(technicians.get(2));
                        t3.setText(technicians.get(4));
                    }
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.technician1:
                    Log.d("test1", "Selected t1");
                    technicianName = technicians.get(0);
                    technicianEmail = technicians.get(1);
                    break;
                case R.id.technician2:
                    Log.d("test1", "Selected t2");
                    technicianName = technicians.get(2);
                    technicianEmail = technicians.get(3);
                    break;
                case R.id.technician3:
                    Log.d("test1", "Selected t3");
                    technicianName = technicians.get(4);
                    technicianEmail = technicians.get(5);
                    break;
            }
        });


        return root;
    }
    @Override
    public void onClick(View v) {
        serviceRequest makeRequest = new serviceRequest(getActivity(), serviceType);
        makeRequest.execute(technicianEmail, technicianName);
    }
}
