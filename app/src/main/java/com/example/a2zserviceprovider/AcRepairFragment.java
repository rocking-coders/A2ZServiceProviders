package com.example.a2zserviceprovider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

        textLatLong = root.findViewById(R.id.textLatLong);

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
                    //getCurrentLocation();
                    backgroundWorker bw = new backgroundWorker(getActivity());
                    bw.execute();
                    Log.d("test1", "background work assigned");
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

    //background worker
    class backgroundWorker extends AsyncTask<Void, Void, Void> {
        AlertDialog alertDialog;
        Context context;
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public backgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            Log.d("test1", "onPreExecute");
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Error");
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Processing");
            showDialog();
        }

        private void showDialog() {
            Log.d("test1", "Progress bar is started");
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        private void hideDialog() {
            Log.d("test1", "progress bar is stopped");
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("test1", "doInBackground");
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.getFusedLocationProviderClient(getActivity())
                    .requestLocationUpdates(locationRequest, new LocationCallback() {

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(getActivity())
                                    .removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int latestLocationIndex = locationResult.getLocations().size() - 1;
                                double latitude =
                                        locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                double longitude =
                                        locationResult.getLocations().get(latestLocationIndex).getLongitude();
                                textLatLong.setText(
                                        String.format(
                                                "Latitude: %s\nLongitude: %s",
                                                latitude,
                                                longitude
                                        )
                                );

                                Location location = new Location("providerNA");
                                location.setLatitude(latitude);
                                location.setLongitude(longitude);
                                //till here we got latitude and longitude

                                //converting longitude and latitude into address
                                String errorMessage = "";
                                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                } catch (Exception exception) {
                                    errorMessage = exception.getMessage();
                                }
                                if (addresses == null || addresses.isEmpty()) {
                                    //could not able to fetch address from longitude and latitude
                                    //then we have to take address manually
                                    //Code it here
                                    Log.d("test1", errorMessage);
                                } else {
                                    //we got the address
                                    Address address = addresses.get(0);
                                    //combining all addresses into array list if multiple addresses are fetched from given longitude and latitude
                                    /*
                                    ArrayList<String> addressFragments = new ArrayList<>();
                                    for(int i = 0;i <= address.getMaxAddressLineIndex();i++){
                                        addressFragments.add(address.getAddressLine(i));
                                        Log.d("address",address.getAddressLine(i));
                                    }
                                    */
                                    //we are getting only one match to corresponding longitude and latitude thus there is no benefit of using array list
                                    Log.d("pin code", address.getAddressLine(0));
                                }
                                hideDialog();
                            } else {
                                hideDialog();
                            }
                        }
                    }, Looper.getMainLooper());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("test1","onPostExecute");
           //after complete execution of background task redirect the page to the fragment for showing the result
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.d("test1","onProgressUpdate");
            super.onProgressUpdate(values);
        }

    }

}
