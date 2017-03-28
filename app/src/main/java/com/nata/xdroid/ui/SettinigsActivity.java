package com.nata.xdroid.ui;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;

import com.nata.xdroid.R;
import com.nata.xdroid.services.TestService;
import com.nata.xdroid.utils.ActivityUtil;
import com.nata.xdroid.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettinigsActivity extends AppCompatActivity {
    private ServiceConnection serviceConnection;
    private Intent serviceIntent;
    private TestService testService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        bindService();
        startService(serviceIntent);
    }

    private void bindService() {
        serviceIntent = new Intent(SettinigsActivity.this, TestService.class);
        if(serviceConnection == null) {
            serviceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    testService = ((TestService.TestBinder)service).getService();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void unbindService() {
        if(null != serviceConnection) {
            unbindService(serviceConnection);
            serviceConnection = null;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        unbindService();
        super.onPause();
    }

    @Override
    protected void onResume() {
        bindService();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        bindService();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        unbindService();
        super.onStop();
    }



    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            PreferenceManager prefMgr = getPreferenceManager();
            prefMgr.setSharedPreferencesName("pref_mine");
            prefMgr.setSharedPreferencesMode(MODE_WORLD_READABLE);

            SharedPreferences sp = prefMgr.getSharedPreferences();
            final Context context = getActivity().getApplicationContext();

            addPreferencesFromResource(R.xml.pref_setting);

            boolean isNetWork = NetWorkUtils.isNetworkConnected(context);
//            SharedPreferences sp = context.getSharedPreferences("pref_mine",MODE_WORLD_READABLE);
            sp.edit().putBoolean("network",isNetWork)
                     .putBoolean("test_mode", false)
                     .putStringSet("cov_acts",new HashSet<String>())
                     .apply();

            Preference crash = findPreference("crash");
            crash.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference pref) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), StatisticsActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            Preference test = findPreference("test");
            test.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference pref) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), XMonkeyActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
}
