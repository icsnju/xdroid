package com.nata.xdroid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nata.xdroid.injector.InjectorManager;

/**
 * Created by Calvin on 2016/12/10.
 */

public class AudiosMockReceiver extends BroadcastReceiver {
    public static final String ACTION_MOCK_AUDIOS = "com.nata.xdroid.action.MOCK_AUDIOS";

    public static Intent getMockAudiosIntent() {
        Intent intent = new Intent(ACTION_MOCK_AUDIOS);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_MOCK_AUDIOS.equals(intent.getAction())) {
            InjectorManager.injectAudios(context);
        }
    }
}