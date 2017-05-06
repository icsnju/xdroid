package com.nata.xdroid.receivers;

/**
 * Created by Calvin on 2016/11/26.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nata.xdroid.notifier.BeepNotifier;
import com.nata.xdroid.notifier.ToastNotifier;

public class ToastReceiver extends BroadcastReceiver {
    public static final String EXTRA_NAME_CONTENT = "content";
    public static final String ACTION_TOAST= "com.nata.xdroid.action.TOAST";

    public static Intent getToastIntent(String content) {
        Intent intent = new Intent(ACTION_TOAST);
        intent.putExtra(EXTRA_NAME_CONTENT, content);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_TOAST.equals(intent.getAction())) {
            String content = intent.getExtras().getString(EXTRA_NAME_CONTENT);
            ToastNotifier.makeToast(context,content);
            BeepNotifier.makeBeem(context);
        }
    }
}
