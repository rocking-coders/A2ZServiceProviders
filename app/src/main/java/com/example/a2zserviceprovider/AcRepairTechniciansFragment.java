package com.example.a2zserviceprovider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AcRepairTechniciansFragment extends Fragment implements View.OnClickListener{
    RadioGroup radioGroup;
    RadioButton t1, t2, t3, t4;
    Button Bsubmit;
    String technicianName,technicianEmail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ac_repair_technicians, container, false);
        Bundle b = this.getArguments();
        final ArrayList<String> technicians = b.getStringArrayList("Technicians");
        //removing arguments in bundle
        this.getArguments().remove("Technicians");

        radioGroup = root.findViewById(R.id.technicianGroup);
        t1 = root.findViewById(R.id.technician1);
        t2 = root.findViewById(R.id.technician2);
        t3 = root.findViewById(R.id.technician3);
        TextView textView = root.findViewById(R.id.tnoTechnician);
        Bsubmit = root.findViewById(R.id.buttonRequest);
        Bsubmit.setOnClickListener(this);
        if (technicians.size() == 1) {
            if (technicians.get(0) == "null") {
                Bsubmit.setVisibility(View.INVISIBLE);
                t1.setVisibility(View.INVISIBLE);
                t2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
                textView.setText("Sorry! No technician found nearby");
            } else {
                textView.setVisibility(View.INVISIBLE);
                t1.setText(technicians.get(0));
                t2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
            }
        } else {
            textView.setVisibility(View.INVISIBLE);
            t1.setText(technicians.get(0));
            t2.setText(technicians.get(2));
            if (technicians.size() == 2) {
                t3.setVisibility(View.INVISIBLE);
            } else {
                t3.setText(technicians.get(4));
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.technician1:
                        Log.d("test1","Selected t1");
                        technicianName = technicians.get(0);
                        technicianEmail = technicians.get(1);
                        break;
                    case R.id.technician2:
                        Log.d("test1","Selected t2");
                        technicianName = technicians.get(2);
                        technicianEmail = technicians.get(3);
                        break;
                    case R.id.technician3:
                        Log.d("test1","Selected t3");
                        technicianName = technicians.get(4);
                        technicianEmail = technicians.get(5);
                        break;
                }
            }
        });


        return root;
    }

    @Override
    public void onClick(View v) {
        serviceRequest makeRequest = new serviceRequest(getActivity());
        makeRequest.execute(technicianEmail,technicianName,"AC Repair");
    }
}
