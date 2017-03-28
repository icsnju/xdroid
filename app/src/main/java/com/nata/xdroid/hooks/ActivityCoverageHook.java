package com.nata.xdroid.hooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nata.xdroid.receivers.MonkeyCoverageReceiver;
import com.nata.xdroid.receivers.NewActivityReceiver;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * Created by Calvin on 2016/11/21.
 */

public class ActivityCoverageHook {
    private Context context;
    public ActivityCoverageHook(Context context) {
        this.context = context;
    }

    public void hook(final ClassLoader loader) {

        findAndHookMethod("android.app.Activity", loader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Context activity = (Activity) param.thisObject;
                String activityName = activity.getClass().getName();
                String packageName = context.getPackageName();
                Intent intent = NewActivityReceiver.getNewActivityIntent(activityName);
                context.sendBroadcast(intent);
            }
        });
    }
}

