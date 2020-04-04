package com.example.a2zserviceprovider.BackgroundWorkers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.a2zserviceprovider.R;
import com.example.a2zserviceprovider.displayTechniciansFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class findTechnician extends AsyncTask<String, Void, ArrayList<String> > {

    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    Context context;
    String serviceType;

    findTechnician(Context ctx, String sT){
        context = ctx;
        serviceType = sT;
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Message");
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing ");
        showDialog();
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        ArrayList<String> result = new ArrayList<String>();
        String pincode = strings[0];
        String technicianType = strings[1];

        Log.d("test1","In findTechnician::doInBackground() pincode = "+pincode);
        String LOGIN_URL = "https://a2zserviceproviders.000webhostapp.com/db_connectivity/findTechnician.php";
        try {
            URL url = new URL(LOGIN_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            Log.d("test1","Output stream open");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String post_data = URLEncoder.encode("pincode", "UTF-8")+"="+URLEncoder.encode(pincode, "UTF-8")+"&"
                    +URLEncoder.encode("technicianType", "UTF-8")+"="+URLEncoder.encode(technicianType, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            Log.d("test1","Data Sent, Waiting for input");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String line="";
            //db is returning only three results like this
            //  Mahesh Kumar
            //  m@g.com
            //  Pawan Kumar
            //  p@g.com
            //  Harsh @g.com
            //  h@g.com
            while((line=bufferedReader.readLine())!=null)
            {
                result.add(line);
                Log.d("test1",line);
            }
            //here result contain the username that is returned from dB.
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("test1", String.valueOf(result));
            //return result to postExecute
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String > aVoid) {
        hideDialog();
        Log.d("test1","findTechnician::onPostExecute() "+aVoid);
        //sending the technician result as bundle arguments
        Fragment f= new displayTechniciansFragment();
        Bundle b = new Bundle();
        b.putStringArrayList("Technicians",aVoid);
        b.putString("technicianType", serviceType);
        f.setArguments(b);
        Activity activity = (Activity)context;
        FragmentActivity fragmentActivity = (FragmentActivity)activity;
        if(serviceType.equals("AcRepair")) {
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, f).commit();
            //getSupportFragmentManager operates on fragmentActivity object
            //transacting from one fragment to another fragment is done like this
        }
        else if(serviceType.equals("Plumber")){
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_plumber, f).commit();
        }
        else if(serviceType.equals("Painter")){
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_painter, f).commit();
        }
        else if(serviceType.equals("Carpenter")){
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_carpenter, f).commit();
        }
    }
}
