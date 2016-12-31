package com.nata.xdroid.receivers;

/**
 * Created by Calvin on 2016/11/26.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nata.xdroid.db.beans.UserData;
import com.nata.xdroid.db.daos.UserDataDao;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_WORLD_READABLE;

public class NewActivityReceiver extends BroadcastReceiver {
    public static final String EXTRA_NAME_ACTIVITY_NAME = "activity_name";
    public static final String ACTION_COLLECT_ACTIVITY= "com.nata.xdroid.action.COLLECT_ACTIVITY";

    public static Intent getNewActivityIntent(String activityName) {
        Intent intent = new Intent(ACTION_COLLECT_ACTIVITY);
        intent.putExtra(EXTRA_NAME_ACTIVITY_NAME, activityName);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_COLLECT_ACTIVITY.equals(intent.getAction())) {
            String activityName = intent.getExtras().getString(EXTRA_NAME_ACTIVITY_NAME);
            SharedPreferences sp = context.getSharedPreferences("pref_mine",MODE_WORLD_READABLE);
            Set<String> set = sp.getStringSet("cov_acts",new HashSet<String>());
            set.add(activityName);
            sp.edit().putStringSet("cov_acts", set).apply();
        }
    }
}
