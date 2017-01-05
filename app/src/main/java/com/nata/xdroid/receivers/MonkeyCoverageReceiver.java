package com.nata.xdroid.receivers;

/**
 * Created by Calvin on 2016/11/26.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_WORLD_READABLE;

public class MonkeyCoverageReceiver extends BroadcastReceiver {
    public static final String EXTRA_NAME_PACKAGE_NAME = "package_name";
    public static final String EXTRA_NAME_ACTIVITY_NAME = "activity_name";
    public static final String ACTION_MONKEY_COVERAGE= "com.nata.xdroid.action.MONKEY_COVERAGE";

    public static Intent getNewActivityIntent(String packageName, String activityName) {
        Intent intent = new Intent(ACTION_MONKEY_COVERAGE);
        intent.putExtra(EXTRA_NAME_PACKAGE_NAME, packageName);
        intent.putExtra(EXTRA_NAME_ACTIVITY_NAME, activityName);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_MONKEY_COVERAGE.equals(intent.getAction())) {
            String packageName = intent.getExtras().getString(EXTRA_NAME_PACKAGE_NAME);
            String activityName = intent.getExtras().getString(EXTRA_NAME_ACTIVITY_NAME);
            SharedPreferences sp = context.getSharedPreferences("monkey_coverage",MODE_WORLD_READABLE);
            Set<String> set = sp.getStringSet(packageName,new HashSet<String>());
            set.add(activityName);
            sp.edit().putStringSet(packageName, set).apply();
        }
    }
}
