package com.nata.xdroid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nata.xdroid.utils.NetWorkUtils;

/**
 * Created by Calvin on 2016/12/10.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            boolean isNetWork = NetWorkUtils.isNetworkConnected(context);
            context.getSharedPreferences("pref_mine", Context.MODE_WORLD_READABLE).edit().putBoolean("network", isNetWork).apply();
        }
    }
}
