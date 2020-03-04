package com.example.a2zserviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);
        Log.d("Creation","Authentication Activity Created");
        Intent i=getIntent();
        //getting intent from main activity or any activity where sign in or sign up button is clicked
        Toolbar toolbar = findViewById(R.id.toolbar_authen);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_authen);
        navigationView = findViewById(R.id.nav_view_authen);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //for checking whether user has clicked sign in or sign up activity accordingly he will be directed to that fragment
        String intent = i.getStringExtra("activity");
        //printing intent message on logcat screen
        Log.d("message",intent);

        if (savedInstanceState == null) {
            if(intent.equals("signIn")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignInFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signIn);
            }
            else if(intent.equals("signUp"))
            {
                Log.d("creation","signUn fragment created");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignUpFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signUp);
            }
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new ServicesFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_services);
                break;
            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SettingFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_setting);
                break;
            case R.id.nav_signIn:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignInFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signIn);
                break;
            case R.id.nav_signUp:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignUpFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signUp);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void OpenSignupPage(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignUpFragment()).commit();
    }
    public void OpenSigninPage(View view)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen,new SignInFragment()).commit();
    }

}
