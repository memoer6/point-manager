package com.memoer6.pointreader3.view;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.memoer6.pointreader3.R;

public class SettingsActivity extends AppCompatActivity {


    //tag to get the key for order setting preference
    protected static final String KEY_PREF_ORDER = "pref_inverted";

    //tag to get the key for transaction number setting preference
    protected static final String KEY_PREF_NUMBER = "pref_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        //ensures that your application is properly initialized with default settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


    }


    //Our SettingsActivity hosts the SettingsFragment that displays our preference settings
    public static class SettingsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {

        Preference mNumberPref;

        //There are several reasons you might want to be notified as soon as the user changes one
        // of the preferences. In order to receive a callback when a change happens to any one of
        // the preferences, implement the SharedPreference.OnSharedPreferenceChangeListener interface
        // and register the listener for the SharedPreferences object by calling
        // registerOnSharedPreferenceChangeListener().The interface has only one callback method,
        // onSharedPreferenceChanged(), and you might find it easiest to implement the interface
        // as a part of your activity

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);


            //Load summary with saved number of transactions
            mNumberPref = findPreference(KEY_PREF_NUMBER);
            SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
            mNumberPref.setSummary(getResources().getString(R.string.pref_number_summary) +
                    String.valueOf(sharedPref.getInt(KEY_PREF_NUMBER,10)));

        }

        @Override
        public void onSharedPreferenceChanged(  SharedPreferences sharedPreferences, String key) {

            if (key.equals(KEY_PREF_NUMBER)) {

                //Update the summary with the new value of number of transactions
                mNumberPref.setSummary(getResources().getString(R.string.pref_number_summary) +
                        String.valueOf(sharedPreferences.getInt(key,10)));
            }

        }

        //For proper lifecycle management in the activity, we recommend that you register and
        // unregister your SharedPreferences.OnSharedPreferenceChangeListener during the onResume()
        // and onPause() callbacks, respectively:
        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }


    }


}
