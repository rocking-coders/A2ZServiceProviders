package com.example.a2zserviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class AuthenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);

        Intent i=getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar_authen);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_authen);
        NavigationView navigationView = findViewById(R.id.nav_view_authen);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            if(i.getStringExtra("activity")=="signIn") {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignInFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signIn);
            }
            else
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignUpFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signIn);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new HomeFragment()).commit();
                break;
            case R.id.nav_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new ServicesFragment()).commit();
                break;
            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SettingFragment()).commit();
                break;
            case R.id.nav_signIn:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignInFragment()).commit();
                break;
            case R.id.nav_signUp:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_authen, new SignUpFragment()).commit();
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
