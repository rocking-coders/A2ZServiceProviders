package com.example.a2zserviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class AcRepairActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_repair);

        //app bar most high
        /*
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
         */
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout2);
        NavigationView navigationView = findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //changing Navigation buttons if logged in
        SharedPreferences sharedPreferences = getSharedPreferences("Login Data", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        Log.d("Username",username);
        if(!username.equals("")){
            Log.d("status","Logged In");
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_signIn).setVisible(false);
            menu.findItem(R.id.nav_signUp).setVisible(false);
            view = navigationView.getHeaderView(0);
            TextView textView = view.findViewById(R.id.username);
            textView.setText(username);
        }

        if (savedInstanceState == null) {
            //first fragment to be opened - homeFragment along with highlighted
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new AcRepairFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_services);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new HomeFragment()).commit();
                break;
            case R.id.nav_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new ServicesFragment()).commit();
                break;
            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new SettingFragment()).commit();
                break;
            case R.id.nav_signIn:
                startActivity(new Intent(AcRepairActivity.this, AuthenActivity.class));
                break;
            case R.id.nav_signUp:
                startActivity(new Intent(AcRepairActivity.this, AuthenActivity.class));
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
    /*
    public void OpenSignupPage(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new SignUpFragment()).commit();
    }
    public void OpenSigninPage(View view)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,new SignInFragment()).commit();
    }

     */
}
