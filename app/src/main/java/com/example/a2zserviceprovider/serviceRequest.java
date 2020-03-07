package com.example.a2zserviceprovider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

public class serviceRequest extends AsyncTask<String, Void, String> {

    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    Context context;

    serviceRequest(Context ctx){
        context = ctx;
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
    protected String doInBackground(String... args) {
        String result="";
        String technicianName = args[1];
        String technicianEmail = args[0];
        String serviceType = args[2];
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login Data", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        String useremail = sharedPreferences.getString("UserEmail","");
        sharedPreferences = context.getSharedPreferences("services Data",Context.MODE_PRIVATE);
        String serviceLocation = sharedPreferences.getString("serviceLocation","");
        Log.d("test2","Data to be passed : "+technicianEmail+" "+technicianName+" "+useremail+" "+username+" "+serviceType+" "+serviceLocation);

        String LOGIN_URL = "https://a2zserviceproviders.000webhostapp.com/db_connectivity/serviceRequest.php";
        try {
            URL url = new URL(LOGIN_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String post_data = URLEncoder.encode("userEmail", "UTF-8")+"="+URLEncoder.encode(useremail, "UTF-8")+"&"
                    +URLEncoder.encode("userName", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&"
                    +URLEncoder.encode("technicianName", "UTF-8")+"="+URLEncoder.encode(technicianName, "UTF-8")+"&"
                    +URLEncoder.encode("technicianEmail", "UTF-8")+"="+URLEncoder.encode(technicianEmail, "UTF-8")+"&"
                    +URLEncoder.encode("serviceType", "UTF-8")+"="+URLEncoder.encode(serviceType, "UTF-8")+"&"
                    +URLEncoder.encode("serviceLocation", "UTF-8")+"="+URLEncoder.encode(serviceLocation, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String line="";
            while((line=bufferedReader.readLine())!=null)
            {
                result += line;
            }
            //here result contain the username that is returned from dB.
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            //return result;
            Log.d("test2","result  = "+result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        hideDialog();
        Log.d("test1","serviceRequest::onPostExecute() "+aVoid);
        if(aVoid.equals("inserted")) {
            Fragment f = new successfulRequestFragment();
            Activity activity = (Activity) context;
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, f).commit();
            //getSupportFragmentManager operates on fragmentActivity object
            //transacting from one fragment to another fragment is done like this
        }
        else {
            alertDialog.setMessage("Something went wrong! Please Try Again");
            alertDialog.show();
        }
    }
}
