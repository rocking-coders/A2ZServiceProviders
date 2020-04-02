package com.example.a2zserviceprovider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class successfulRequestFragment extends Fragment {

    Context context;

    successfulRequestFragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_successful_request, container, false);
        TextView textView = root.findViewById(R.id.TVsuccessfulRequest);
        textView.setText("Your Request has been accepted. Technician will contact You.");

        //removing service location from services Data
        SharedPreferences sharedPreferences = context.getSharedPreferences("services Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("serviceLocation");
        editor.apply();

        return root;
    }
}
