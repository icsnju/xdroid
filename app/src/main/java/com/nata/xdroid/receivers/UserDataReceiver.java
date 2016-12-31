package com.nata.xdroid.receivers;

/**
 * Created by Calvin on 2016/11/26.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_WORLD_READABLE;

public class UserDataReceiver extends BroadcastReceiver {
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_CONTENT = "content";
    public static final String ACTION_LOG_DATA = "com.nata.xdroid.action.LOG_DATA";

    public static Intent getUserDataIntent(String name, String content) {
        Intent intent = new Intent(ACTION_LOG_DATA);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_CONTENT, content);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_LOG_DATA.equals(intent.getAction())) {
            String name = intent.getExtras().getString(EXTRA_NAME);
            String content = intent.getExtras().getString(EXTRA_CONTENT);
            SharedPreferences sp = context.getSharedPreferences("user_data",MODE_WORLD_READABLE);
            sp.edit().putString(name,content).apply();
        }
    }
}
