package com.example.a2zserviceprovider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {

    LinearLayout linearLayout;
    Context context;
    ArrayList<String> servicesOffered;
    TextView details;
    public ServicesFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_services,container,false);
        Log.d("test3","On services fragment");

        Bundle b = this.getArguments();
        servicesOffered = b.getStringArrayList("Services Offered");
        //removing arguments in bundle
        this.getArguments().remove("Services Offered");

        linearLayout = root.findViewById(R.id.fragment_services);
        //int size = servicesOffered.size()/4;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        Log.d("test3","Size = "+servicesOffered.size());
        String serviceDetails="";
        for(int i=0;i<servicesOffered.size();i+=4)
        {
            serviceDetails+=servicesOffered.get(i)+"\n"+servicesOffered.get(i+1)+"\n"+servicesOffered.get(i+2)+"\n"+servicesOffered.get(i+3)+"\n\n\n";
        }
        Log.d("test3",serviceDetails);
        details = new TextView(context);
        details.setText(serviceDetails);
        details.setTextSize(getResources().getDimension(R.dimen.servicesOffered_TextSize));
        details.setLayoutParams(params);
        linearLayout.addView(details);

        return root;
    }
}
