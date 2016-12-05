package com.nata.xdroid.hooks;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.View;
import android.widget.EditText;

import com.nata.xdroid.StartTestReceiver;
import com.nata.xdroid.TestRunner;
import com.nata.xdroid.UserDataReceiver;
import com.nata.xdroid.db.beans.UserData;
import com.nata.xdroid.db.daos.UserDataDao;
import com.nata.xdroid.monkey.Monkey;
import com.nata.xdroid.utils.ViewUtil;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

import static com.nata.xdroid.utils.PreferencesUtils.inMonitorMode;
import static com.nata.xdroid.utils.PreferencesUtils.inTestMode;
import static com.nata.xdroid.utils.ViewUtil.getAllChildViews;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * Created by Calvin on 2016/11/21.
 */

public class ActivityHook {
    private Context context;
    private TestRunner testRunner = null;

    public ActivityHook(TestRunner runner, Context context) {
        this.context = context;
        this.testRunner = runner;
    }

    public void hook(ClassLoader loader) {
        findAndHookMethod("android.app.Activity", loader, "onPause", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (inMonitorMode()) {
                    Activity rootActivity = (Activity) param.thisObject;
                    List<View> views = getAllChildViews(rootActivity.getWindow().getDecorView(), EditText.class);
                    ViewUtil.persistUserData(context, views, rootActivity.getLocalClassName());
                }
                testRunner.setActive(false);
            }
        });

        findAndHookMethod("android.app.Activity", loader, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (inMonitorMode()) {
                    final Activity rootActivity = (Activity) param.thisObject;
                    List<View> views = getAllChildViews(rootActivity.getWindow().getDecorView(), EditText.class);
                    ViewUtil.fillUserData(context, views, rootActivity.getLocalClassName());
                }

                testRunner.setActive(true);
            }
        });
    }

}


// 填写数据

