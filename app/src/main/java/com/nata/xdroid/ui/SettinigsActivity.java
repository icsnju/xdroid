package com.nata.xdroid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.nata.xdroid.R;
import com.nata.xdroid.db.beans.UserData;
import com.nata.xdroid.db.daos.UserDataDao;
import com.nata.xdroid.utils.FormatUtil;

import java.util.Arrays;
import java.util.List;

public class SettinigsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
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


//            getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.pref_setting);

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

            Preference userdata = findPreference("userdata");
            userdata.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference pref) {
                    Context context = getActivity().getApplicationContext();
                    List<UserData> userDatas = new UserDataDao(context).getAll();
                    String userData = FormatUtil.getUserDataInfo(userDatas);

                    UserDataDialog.show(context, userData);
                    return true;
                }
            });
        }
    }
}
