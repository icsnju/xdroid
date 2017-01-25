package com.nata.xdroid.hooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.nata.xdroid.XMonkey;
import com.nata.xdroid.receivers.NewActivityReceiver;
import com.nata.xdroid.utils.ViewUtil;
import com.nata.xdroid.utils.XPreferencesUtils;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

import static com.nata.xdroid.utils.ViewUtil.getAllChildViews;
import static com.nata.xdroid.utils.XPreferencesUtils.inMonitorMode;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * Created by Calvin on 2016/11/21.
 */

public class XMonkeyHook {
    private XMonkey xMonkey;

    public XMonkeyHook(XMonkey xMonkey) {
        this.xMonkey = xMonkey;
    }

    public void hook(final ClassLoader loader) {
        findAndHookMethod("android.app.Activity", loader, "onPause", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                xMonkey.setActive(false);
            }
        });

        findAndHookMethod("android.app.Activity", loader, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                xMonkey.setActive(true);
            }
        });
    }
}

