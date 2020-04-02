package com.example.a2zserviceprovider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

public class locationFind {
    Context context;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    String serviceType;
    //FragmentManager fragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    locationFind(Context ctx, String sT) {
        context = ctx;
        serviceType = sT;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing");
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

    void locationGet(final String technicianType) {

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Message");

        Log.d("test1", "On locationFind::locationGet()");
        showDialog();

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(context)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            //till here we got latitude and longitude

                            //converting longitude and latitude into address
                            String errorMessage = "";
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
                                //Log.d("test1", address.getAddressLine(0));
                                //getting the pin code in string
                                String geoaddress = address.getAddressLine(0);
                                SharedPreferences preferences = context.getSharedPreferences("services Data",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("serviceLocation",geoaddress);
                                editor.commit();

                                int h = geoaddress.lastIndexOf(',');
                                String pincode = geoaddress.substring(h - 5, h);
                                Log.d("test1", "locationFind:: " + pincode);
                                findTechnician obj = new findTechnician(context, serviceType);
                                obj.execute(pincode,technicianType);

                            }
                            hideDialog();
                        } else {
                            alertDialog.setMessage("Something went Wrong");
                            hideDialog();
                            alertDialog.show();
                        }
                    }
                }, Looper.getMainLooper());
    }
}
