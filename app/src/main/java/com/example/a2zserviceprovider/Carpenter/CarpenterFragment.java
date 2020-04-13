package com.example.a2zserviceprovider.Carpenter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

import com.example.a2zserviceprovider.BackgroundWorkers.locationFind;
import com.example.a2zserviceprovider.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarpenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarpenterFragment extends Fragment {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    ProgressDialog progressDialog;
    private TextView textLatLong;
    Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_carpenter, container, false);
        ctx = getActivity();
        final EditText problem_detail = root.findViewById(R.id.problemText);

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("Login Data", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");

        TextView warning_message = root.findViewById(R.id.submit_warning);
        Button submit_button = root.findViewById(R.id.buttonGetCurrentLocation);
        if(!username.equals("")){
            warning_message.setVisibility(View.INVISIBLE);
        }
        else {
            if (username.equals("")) {
                warning_message.setText("Please Sign In first");
            } else {
                warning_message.setText("No Internet");
            }
            submit_button.setBackgroundColor(getResources().getColor(R.color.colorDark));
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
                    editor.apply();
                    Log.d("test1", "Assigning a background worker ");
                    locationFind obj = new locationFind(getActivity(), "Carpenter");
                    obj.locationGet("Carpenter");
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