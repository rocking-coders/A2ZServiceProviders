package com.example.a2zserviceprovider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AcRepairFragment extends Fragment {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    ProgressDialog progressDialog;
    private TextView textLatLong;
    Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ac_repair, container, false);
        ctx = getActivity();
        final EditText problem_detail = root.findViewById(R.id.problemText);

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("Login Data", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");

        TextView warning_message = root.findViewById(R.id.submit_warning);
        Button submit_button = root.findViewById(R.id.buttonGetCurrentLocation);
        if(!username.equals("")){
            warning_message.setVisibility(View.INVISIBLE);
        }
        else{
            submit_button.setEnabled(false);
        }

        root.findViewById(R.id.buttonGetCurrentLocation).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    String problem = problem_detail.getText().toString();
                    Log.d("Problem_detail", problem);
                    SharedPreferences preferences = ctx.getSharedPreferences("services Data",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("problem_specification",problem);
                    editor.commit();
                    Log.d("test1", "Assigning a background worker ");
                    locationFind obj = new locationFind(getActivity(), "AcRepair");
                    obj.locationGet("AC Mechanic");
                }
            }
        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
