package com.nata.xdroid.hooks;

import android.app.Activity;
import android.content.Intent;

import com.nata.xdroid.utils.ViewUtil;

import de.robv.android.xposed.XC_MethodHook;

import static com.nata.xdroid.utils.PreferencesUtils.inTestMode;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/12/2.
 */

public class AutoTestHook implements Hook{
    @Override
    public void hook(ClassLoader loader) {

        findAndHookMethod("android.app.Activity", loader, "startActivty", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("start Activity");
                if (inTestMode()) {
                    Intent intent = (Intent) param.args[0];
                    log(intent.getAction());
                }
            }
        });


        findAndHookMethod("android.app.Activity", loader, "onResume", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (inTestMode()) {
                    Activity rootActivity = (Activity) param.thisObject;
                    ViewUtil.exploreTest(rootActivity.getWindow().getDecorView());
                }
            }
        });
    }
}
