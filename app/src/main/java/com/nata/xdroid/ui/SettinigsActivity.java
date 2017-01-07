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

//            Preference userdata = findPreference("userdata");
//            userdata.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference pref) {
//
//                    List<UserData> userDatas = new UserDataDao(context).getAll();
//                    String userData = FormatUtil.getUserDataInfo(userDatas);
//
//                    UserDataDialog.show(context, userData);
//                    return true;
//                }
//            });

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

//            Preference coverage = findPreference("coverage");
//            coverage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference pref) {
//                    SharedPreferences sp = context.getSharedPreferences("monkey_coverage",MODE_WORLD_READABLE);
//                    Map<String,Set<String>> map = (Map<String,Set<String>>)sp.getAll();
//                    String message = "";
//                    for(Map.Entry<String,Set<String>> entry: map.entrySet()) {
//                        String packageName = entry.getKey();
//                        Set<String> activities = entry.getValue();
//                        List<String> actLists = ActivityUtil.getActivities(context, packageName);
//                        float coverage = (float)activities.size() /actLists.size();
//                        message += packageName + " : " + "\n" ;
//                        message += coverage + " => " + activities.size() + "\\" + actLists.size() + "\n";
//                    }
//                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(getActivity());
//                    alertDialogBuilder.setMessage(message);
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();//将dialog显示出来
//                    return true;
//                }
//            });

//            Preference clearCoverage = findPreference("clear_coverage");
//            clearCoverage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference pref) {
//                    SharedPreferences sp = context.getSharedPreferences("monkey_coverage",MODE_WORLD_READABLE);
//                    sp.edit().clear().apply();
//                    return true;
//                }
//            });

//            final SwitchPreference open = (SwitchPreference)findPreference("open");
//            open.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newVal) {
//                    final boolean value = (Boolean) newVal;
////                    open.setChecked(value);
////                    if(value) getActivity().bindService();
//                    return true;
//                }
//            });

//            Preference broadcast = findPreference("broadcast");
//            broadcast.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference pref) {
//                    Intent intent = new Intent("com.fsck.k9.service.CoreReceiver.wakeLockRelease");
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                    return true;
//                }
//            });


        }
    }
}
