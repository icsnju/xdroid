package com.nata.xdroid.hooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nata.xdroid.R;
import com.nata.xdroid.TestRunner;
import com.nata.xdroid.utils.ActivityUtil;
import com.nata.xdroid.utils.ToastUtil;
import com.nata.xdroid.utils.ViewUtil;

import java.util.HashSet;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static com.nata.xdroid.utils.ToastUtil.makeToast;
import static com.nata.xdroid.utils.XPreferencesUtils.inMonitorMode;
import static com.nata.xdroid.utils.ViewUtil.getAllChildViews;
import static com.nata.xdroid.utils.XPreferencesUtils.inTestMode;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * Created by Calvin on 2016/11/21.
 */

public class ActivityHook {
    private Context context;
    private TestRunner testRunner = null;
    List<String> actList = null;
    HashSet<String> activitySet;
    String packageName;

    public ActivityHook(TestRunner runner, Context context, String packageName) {
        this.context = context;
        this.testRunner = runner;
        this.packageName = packageName;
        activitySet = new HashSet<>();
    }

    public void hook(final ClassLoader loader) {

        findAndHookMethod("android.app.Activity", loader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (actList == null) {
                    actList = ActivityUtil.getActivities(context, packageName);
                    log("AllActivities size: " + actList.size());
                }
                Context activity = (Activity) param.thisObject;
                String activityName = activity.getClass().getName();
                activitySet.add(activityName);
                float coverage = (float) activitySet.size() / actList.size();

                log(packageName + "=> " + activityName + " => ActivityCoverage: " + coverage);

            }
        });


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

                if (!testRunner.isAlive()) {
                    testRunner.start();
                }

                testRunner.setActive(true);
            }
        });

        findAndHookMethod("android.app.Activity", loader, "startActivityForResult", Intent.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("beforeHookedMethod: " + "startActivityForResult1");
                Intent intent = (Intent) param.args[0];
                processIntent(intent);
            }
        });
    }

    private void processIntent(Intent intent) {
        if (intent == null) return;
        log(intent.toString());

        String action = intent.getAction();
        if (action != null) {
            if (action.equals(Intent.ACTION_OPEN_DOCUMENT))
                makeToast(context, "应用打开了文档管理器，请选择合适的文件" + intent.getType());
            if (action.equals(Intent.ACTION_CHOOSER))
                makeToast(context, "应用打开了应用选择器,请选择合适的程序");
        }
    }
}

