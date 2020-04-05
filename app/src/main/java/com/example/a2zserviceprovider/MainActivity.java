package com.example.a2zserviceprovider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.a2zserviceprovider.Authentication.AuthenActivity;
import com.example.a2zserviceprovider.BackgroundWorkers.fetchTotalServicesBW;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View view;
    private SharedPreferences sharedPreferences;
    String username, useremail;
    String url;

    //variables for loading image
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    ImageView imgProfile, imgPlus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = null;

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
        url = sharedPreferences.getString("profiledp_path", "");
        Log.d("Username", username);

        //upating UI
        Menu menu = navigationView.getMenu();
        view = navigationView.getHeaderView(0);

        //setting dp
        imgProfile = view.findViewById(R.id.img_profile);
        imgPlus = view.findViewById(R.id.img_plus);

        TextView textView1 = view.findViewById(R.id.username);
        TextView textView2 = view.findViewById(R.id.useremail);

        //if user is signed in
        if (!username.equals("")) {
            Log.d("status", "Logged In");
            //making sign in and sign up invisible
            menu.findItem(R.id.nav_signIn).setVisible(false);
            menu.findItem(R.id.nav_signUp).setVisible(false);

            //loading profile dp
            if (!url.equals("")) {
                loadProfile(url);
                Log.d("test7", "url = " + url + "url");
            } else {
                loadProfileDefault(R.drawable.baseline_account_circle_black_48);
            }

            //updating user image and email
            textView1.setText(username);
            textView2.setText(useremail);
        } else {
            //disabling all elements that are not needed.
            //imgProfile.setVisibility(View.INVISIBLE);
            imgPlus.setVisibility(View.INVISIBLE);
            loadProfileDefault(R.drawable.ic_android_a2z_512x512);

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
    protected void onResume() {
        Log.d("test6", "MainActivity::onResume()");
        if (url != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("Login Data", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profiledp_path", url);
            editor.apply();
            Log.d("test6", "url stored" + sharedPreferences.getString("profiledp_path", ""));
        }
        //updating user name if changed in settings
        TextView textView1 = view.findViewById(R.id.username);
        String curr_userName = sharedPreferences.getString("username", "");
        if (!textView1.toString().equals(curr_userName)) {
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
    public void onBackPressed() {
        Log.d("test6", "MainAcitivity::onBackPressed");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        GlideApp.with(this).load(url)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void loadProfileDefault(int drawable) {
        GlideApp.with(this).load(drawable)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }


    public void onProfileImageClick(View view) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading profile image from local cache
                    url = uri.toString();
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

}
