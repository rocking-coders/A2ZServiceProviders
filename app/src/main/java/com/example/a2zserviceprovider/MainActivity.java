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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2zserviceprovider.Authentication.AuthenActivity;
import com.example.a2zserviceprovider.BackgroundWorkers.fetchTotalServicesBW;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View view;
    private SharedPreferences sharedPreferences;
    String username, useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        Log.d("test6", "MainAcitivity::onCreate()");

        drawer = findViewById(R.id.drawer_layout1);
        navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationView.setCheckedItem(R.id.nav_home);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Log.d("status", "Not logged in");

        //changing Navigation buttons and updating name of user
        sharedPreferences = getSharedPreferences("Login Data", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        useremail = sharedPreferences.getString("UserEmail", "");
        Log.d("Username", username);

        //upating UI
        Menu menu = navigationView.getMenu();
        view = navigationView.getHeaderView(0);
        TextView textView1 = view.findViewById(R.id.username);
        TextView textView2 = view.findViewById(R.id.useremail);

        //if user is signed in
        if (!username.equals("")) {
            Log.d("status", "Logged In");
            //making sign in and sign up invisible
            menu.findItem(R.id.nav_signIn).setVisible(false);
            menu.findItem(R.id.nav_signUp).setVisible(false);

            //updating user image and email
            textView1.setText(username);
            textView2.setText(useremail);
        } else {
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_services).setVisible(false);
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        }

        if (savedInstanceState == null) {
            //first fragment to be opened - homeFragment along with highlighted
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new HomeFragment()).commit();
            //navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    protected void onResume() {
        Log.d("test6", "MainActivity::onResume()");

        //updating user name if changed in settings
        TextView textView1 = view.findViewById(R.id.username);
        String curr_userName = sharedPreferences.getString("username", "");
        if(!textView1.toString().equals(curr_userName)) {
            textView1.setText(curr_userName);
            username = curr_userName;
        }
        navigationView.setCheckedItem(R.id.nav_home);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("test6", "MainAcitivity::onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("test6", "MainAcitivity::onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d("test6", "MainAcitivity::onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d("test6", "MainAcitivity::onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            }
            case R.id.nav_services: {
                fetchTotalServicesBW bw = new fetchTotalServicesBW(this, "MainActivity");
                bw.execute();
                navigationView.setCheckedItem(R.id.nav_services);
                break;
            }
            case R.id.nav_setting: {
                Intent settings = new Intent(this, SettingsPrefActivity.class);
                startActivity(settings);
                Log.d("test6", "MainActivity::onNavigationListener");
                break;
            }
            case R.id.nav_signIn: {
                Intent intent_signin = new Intent(MainActivity.this, AuthenActivity.class);
                intent_signin.putExtra("activity", "signIn");
                startActivity(intent_signin);
                break;
            }
            case R.id.nav_logout: {
                SharedPreferences sharedPreferences = getSharedPreferences("Login Data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent_new_session = new Intent(this, MainActivity.class);
                intent_new_session.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_new_session);
                break;
            }
            case R.id.nav_signUp: {
                Intent intent_signup = new Intent(MainActivity.this, AuthenActivity.class);
                intent_signup.putExtra("activity", "signUp");
                startActivity(intent_signup);
                break;
            }
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        Log.d("test6", "MainAcitivity::onBackPressed");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
