package com.example.a2zserviceprovider.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2zserviceprovider.R;
import com.example.a2zserviceprovider.SettingFragment;
import com.google.android.material.navigation.NavigationView;

public class AuthenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);
        Log.d("Creation", "Authentication Activity Created");
        Intent i = getIntent();
        //getting intent from main activity or any activity where sign in or sign up button is clicked
        Toolbar toolbar = findViewById(R.id.toolbar_authen);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_authen);
        navigationView = findViewById(R.id.nav_view_authen);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_services).setVisible(false);
        menu.findItem(R.id.nav_logout).setVisible(false);

        //removing username and useremail since it is unsigned
        view = navigationView.getHeaderView(0);
        TextView textView1 = view.findViewById(R.id.username);
        TextView textView2 = view.findViewById(R.id.useremail);
        textView1.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //for checking whether user has clicked sign in or sign up activity accordingly he will be directed to that fragment
        String intent = i.getStringExtra("activity");
        //printing intent message on logcat screen
        Log.d("message", intent);

        if (savedInstanceState == null) {
            if (intent.equals("signIn")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignInFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signIn);
            } else if (intent.equals("signUp")) {
                Log.d("creation", "signUn fragment created");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignUpFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signUp);
            }
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                finish();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_authen);
        drawer.closeDrawer(GravityCompat.START);
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

    public void OpenSigninPage(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignInFragment()).commit();
    }

}
