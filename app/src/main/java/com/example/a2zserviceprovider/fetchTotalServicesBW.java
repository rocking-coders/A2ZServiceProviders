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

public class fetchTotalServicesBW extends AsyncTask<Void, Void, ArrayList<String> > {

    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    Context context;
    SharedPreferences sharedPreferences;
    String userName,userEmail, serviceType;
    fetchTotalServicesBW(Context ctx, String sT){
        context = ctx;
        serviceType = sT;
        sharedPreferences = ctx.getSharedPreferences("Login Data",Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("username","");
        userEmail = sharedPreferences.getString("UserEmail","");
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
    protected ArrayList<String > doInBackground(Void... voids) {
        ArrayList<String> result = new ArrayList<String>();
        Log.d("test3","doInBackground");
        String LOGIN_URL = "https://a2zserviceproviders.000webhostapp.com/db_connectivity/fetchServicesDone.php";
        try {
            URL url = new URL(LOGIN_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String post_data = URLEncoder.encode("userEmail", "UTF-8")+"="+URLEncoder.encode(userEmail, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String line="";
            while((line=bufferedReader.readLine())!=null)
            {
                result.add(line);
                Log.d("test3",line);
            }
            //here result contain the username that is returned from dB.
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            //return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("test3", String.valueOf(result));
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> aVoid) {
        hideDialog();
        Log.d("test3","In postExecute");
        for(String s:aVoid){
            Log.d("test3",s);
        }
        Fragment f = new ServicesFragment(context);
        Bundle b = new Bundle();
        b.putStringArrayList("Services Offered",aVoid);
        f.setArguments(b);
        Activity activity = (Activity) context;
        FragmentActivity fragmentActivity = (FragmentActivity) activity;
        if(serviceType.equals("MainActivity")) {
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, f).commit();
        }
        else if(serviceType.equals("AcRepair")){
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, f).commit();
        }
        else if(serviceType.equals("Plumber")){
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_plumber, f).commit();
        }
    }

}
