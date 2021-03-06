package com.example.a2zserviceprovider;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.example.a2zserviceprovider.BackgroundWorkers.accountInfoChange;

import java.util.Map;

@SuppressWarnings("ALL")
public class SettingsPrefActivity extends AppCompatPreferenceActivity {
    private static final String TAG = SettingsPrefActivity.class.getSimpleName();
    private static String username, useremail;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("Login Data", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        useremail = sharedPreferences.getString("UserEmail", "");

        // load settings fragment
         getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            context = getActivity();

            //user Account settings (name and email)
            PreferenceCategory general = (PreferenceCategory) findPreference(getString(R.string.key_category_gen));
            EditTextPreference userNamePreference = (EditTextPreference) findPreference(getString(R.string.key_username));
            Preference userEmailPreference = (Preference) findPreference(getString(R.string.key_useremail));
            EditTextPreference userPassPreference = (EditTextPreference) findPreference(getString(R.string.key_userpass));


            if(!username.equals("")) {
                //set userName
                userNamePreference.setSummary(username);
                userEmailPreference.setSummary(useremail);

                //clearing all Default sharedPreferences as password as contained in it and others too.
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().clear().apply();

                // userName EditText change listener
                bindPreferenceSummaryToValue(findPreference(getString(R.string.key_username)));

                // userpass preference change listener
                bindPreferenceSummaryToValue(findPreference(getString(R.string.key_userpass)));
            }
            //hiding userName preference if not signed in
            else{
                //removing all non-related preferances
                general.removePreference(userNamePreference);
                general.removePreference(userEmailPreference);
                general.removePreference(userPassPreference);
            }

            // notification preference change listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));

            // feedback preference click listener
            Preference myPref = findPreference(getString(R.string.key_send_feedback));
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return true;
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(R.string.summary_choose_ringtone);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            }
            else if (preference instanceof EditTextPreference) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("No Internet Connection");
                if (preference.getKey().equals("key_user_name")) {
                    if(!stringValue.equals("") && !stringValue.equals(username)) {
                        accountInfoChange obj = new accountInfoChange(context, "userName", stringValue, useremail);
                        if (obj.internet_connection()) {
                            obj.execute();
                            preference.setSummary(stringValue);
                        }
                        else{
                            alertDialog.show();
                        }
                    }
                }
                else if (preference.getKey().equals("key_user_pass")){
                    Log.d("test7", "password field Pressed"+stringValue+"=Password");
                    if(!stringValue.equals("")){
                        accountInfoChange obj = new accountInfoChange(context, "userPass", stringValue, useremail);
                        if (obj.internet_connection()) {
                            obj.execute();
                            preference.setSummary(stringValue);
                        }
                        else{
                            alertDialog.show();
                        }
                    }

                }
            }
            else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rocking.coders1@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }
}