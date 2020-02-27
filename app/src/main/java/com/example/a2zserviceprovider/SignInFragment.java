package com.example.a2zserviceprovider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SignInFragment extends Fragment {

    Context ctx;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
         */
        View root = inflater.inflate(R.layout.fragment_signin, container, false);
        ctx = getActivity();
        final EditText ETemail = root.findViewById(R.id.login_input_email);
        final EditText ETpassword = root.findViewById(R.id.login_input_password);
        FloatingActionButton btn_login = root.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ETemail.getText().toString();
                String password = ETpassword.getText().toString();
                backgroundWorker bW = new backgroundWorker(getActivity());
                bW.execute(email,password);
                Log.d("message","On sign In fragment");
            }
        });

        return root;
    }

//this class is for doing background processing like showing dialog box and connecting to dB etc.
//don't write any access specifier for this class not even private
    class backgroundWorker extends AsyncTask<String,Void,String> {

        AlertDialog alertDialog;
        Context context;
        backgroundWorker(Context ctx)
        {
            context=ctx;
        }
        @Override
        protected String doInBackground(String... voids) {
            //link to PHP file for passing and fetching result of login
            String LOGIN_URL = "https://a2zserviceproviders.000webhostapp.com/db_connectivity/login.php";
                try {
                    String user_email = voids[0];
                    String user_password = voids[1];
                    URL url = new URL(LOGIN_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                    String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(user_email, "UTF-8")+"&"
                            +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(user_password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line=bufferedReader.readLine())!=null)
                    {
                        result += line;
                    }
                    //here result contain the username that is returned from dB.
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login Status");
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging you in...");
            showDialog();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            hideDialog();
            //if there is not successful login then, aVoid="invalid credentials" that would be fetched
            if(aVoid.equals("invalid credentials")) {
                alertDialog.setMessage(aVoid);
                alertDialog.show();
            }
            //else aVoid = username
            else
            {
                /*
                Log.d("UserName",aVoid);
                Intent i = new Intent(ctx,MainActivity.class);
                i.putExtra("SignedIn",aVoid);
                startActivity(i);
                */
                 //write here the code to be executed after successful sign In
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        private void showDialog() {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }
        private void hideDialog() {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

}
