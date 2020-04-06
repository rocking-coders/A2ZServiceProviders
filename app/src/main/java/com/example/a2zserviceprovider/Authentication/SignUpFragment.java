package com.example.a2zserviceprovider.Authentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a2zserviceprovider.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class SignUpFragment extends Fragment {

    Context ctx;
    ProgressDialog progressDialog;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
         */
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        ctx = getActivity();
        final EditText ETname = root.findViewById(R.id.signup_input_name);
        final EditText ETemail = root.findViewById(R.id.signup_input_email);
        final EditText ETpassword = root.findViewById(R.id.signup_input_password);
        FloatingActionButton btn_signup = root.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ETname.getText().toString();
                String email = ETemail.getText().toString();
                String password = ETpassword.getText().toString();
                boolean connection = internet_connection();
                if(!name.equals("") && !email.equals("") && !password.equals("") && connection) {
                    SignUpFragment.backgroundWorker bW = new SignUpFragment.backgroundWorker(getActivity());
                    bW.execute(email, password, name);
                    Log.d("message", "On sign Up fragment");
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Error");
                    if(connection) {
                        alertDialog.setMessage("Fill all credentials");
                    }
                    else{
                        alertDialog.setMessage("No Internet");
                    }
                    alertDialog.show();
                }
            }
        });

        return root;
    }

    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    class backgroundWorker extends AsyncTask<String,Void,String> {

        AlertDialog alertDialog;
        Context context;
        backgroundWorker(Context ctx)
        {
            context=ctx;
        }
        @Override
        protected String doInBackground(String... voids) {

            String LOGIN_URL = "https://a2zserviceproviders.000webhostapp.com/db_connectivity/register.php";
                try {
                    String user_email = voids[0];
                    String user_password = voids[1];
                    String user_name = voids[2];
                    URL url = new URL(LOGIN_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                    String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(user_email, "UTF-8")+"&"
                            +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(user_password, "UTF-8") +"&"
                            +URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(user_name, "UTF-8");
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
            alertDialog.setTitle("Registration Status");
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Processing...");
            showDialog();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            hideDialog();
            if(aVoid.equals("error")) {
                alertDialog.setMessage("Failed! Try Again");
                alertDialog.show();
            }
            else
            {
                Log.d("message","Insertion successful");
                Fragment fragment = new SignInFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_authen, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
