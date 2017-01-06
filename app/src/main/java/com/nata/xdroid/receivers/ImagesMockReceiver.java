package com.nata.xdroid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nata.xdroid.injector.InjectorManager;

/**
 * Created by Calvin on 2016/12/10.
 */

public class ImagesMockReceiver extends BroadcastReceiver {
    public static final String ACTION_MOCK_IMAGES = "com.nata.xdroid.action.MOCK_IMAGES";

    public static Intent getMockImagesIntent() {
        Intent intent = new Intent(ACTION_MOCK_IMAGES);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_MOCK_IMAGES.equals(intent.getAction())) {
            InjectorManager.injectImages(context);
        }
    }
}