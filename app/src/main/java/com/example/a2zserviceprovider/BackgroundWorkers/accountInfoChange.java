package com.example.a2zserviceprovider.BackgroundWorkers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.a2zserviceprovider.MainActivity;
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

public class accountInfoChange extends AsyncTask<Void, Void, String> {

    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    Context context;
    String changeKey, changeValue, email;
    public accountInfoChange(Context ctx, String s, String value, String e){
        changeKey = s;
        context = ctx;
        changeValue = value;
        email = e;
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
    protected String doInBackground(Void... voids) {
        String result = "";
        Log.d("test4","In accountInfoChange::doInBackground() "+ changeKey + " " +changeValue);
        String LOGIN_URL = "https://a2zserviceproviders.000webhostapp.com/db_connectivity/updateUserInfo.php";
        try {
            URL url = new URL(LOGIN_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            Log.d("test1","Output stream open");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String post_data = null;

            if(changeKey.equals("userName")){
                post_data=URLEncoder.encode("new_name", "UTF-8")+"="+URLEncoder.encode(changeValue, "UTF-8")+"&"
                        +URLEncoder.encode("uemail", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8");
            }

            else if(changeKey.equals("userPass")){
                post_data=URLEncoder.encode("new_pass", "UTF-8")+"="+URLEncoder.encode(changeValue, "UTF-8")+"&"
                        +URLEncoder.encode("uemail", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8");
            }
            Log.d("test5", post_data);
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            Log.d("test1","Data Sent, Waiting for input");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String line="";
            while((line=bufferedReader.readLine())!=null)
            {
                result += line;
                Log.d("test1",line);
            }
            //here result contain success or failure.
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
    protected void onPostExecute(String aVoid) {
        hideDialog();
        if(!aVoid.equals("success")) {
            alertDialog.setMessage("Error");
            alertDialog.show();
        }
        else {
            Log.d("test5", "successfully updated " + aVoid);

            if (changeKey.equals("userName")) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("Login Data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.putString("username", changeValue);
                editor.apply();
                Log.d("test5", "updated username = " + sharedPreferences.getString("username", ""));
                alertDialog.setMessage("success");
                alertDialog.show();
            }
            else{
                alertDialog.setMessage("Success");
                alertDialog.show();
            }
        }
    }
    public boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}