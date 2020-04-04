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

import com.example.a2zserviceprovider.ACRepair.AcRepairActivity;
import com.example.a2zserviceprovider.Carpenter.CarpenterActivity;
import com.example.a2zserviceprovider.Painter.PainterActivity;
import com.example.a2zserviceprovider.Plumber.PlumberActivity;

public class HomeFragment extends Fragment implements View.OnClickListener{
    Button button_ac_repair,button_plumber,button_carpenter,button_painter,button_tutor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        button_ac_repair = root.findViewById(R.id.ac_repair);
        button_ac_repair.setOnClickListener(this);
        button_plumber = root.findViewById(R.id.plumber);
        button_plumber.setOnClickListener(this);
        button_painter = root.findViewById(R.id.painter);
        button_painter.setOnClickListener(this);
        button_carpenter = root.findViewById(R.id.carpenter);
        button_carpenter.setOnClickListener(this);

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
                Intent intent_painter = new Intent(getContext(), PainterActivity.class);
                startActivity(intent_painter);

            }
            case R.id.carpenter:
            {
                Intent intent_carpenter = new Intent(getContext(), CarpenterActivity.class);
                startActivity(intent_carpenter);

            }
        }
    }
}
