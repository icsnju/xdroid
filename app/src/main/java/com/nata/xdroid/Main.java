package com.nata.xdroid;

import android.app.Application;

import com.nata.xdroid.hooks.ActivityHook;
import com.nata.xdroid.hooks.CrashHook;

import junit.framework.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class Main implements IXposedHookLoadPackage {

    public static final String ZHNT_PACKAGE_NAME = "com.cvicse.zhnt";
    public static final String LOGCAT_PACKAGE_NAME = "org.jtb.alogcat";
    public static final String MM_PACKAGE_NAME = "com.tencent.mm";
    public static final String MULTISMS_PACKAGE_NAME = "com.hectorone.multismssender";
    public static final String CRASH_PACKAGE_NAME = "com.nata.crashapplication";

    public static final String[] sut =
            {
                    "android",
                    "com.fsck.k9",
                    "com.eleybourn.bookcatalogue",
                    "org.totschnig.myexpenses",
                    "com.nloko.android.syncmypix",
                    "org.wordpress.android",
                    "aarddict.android",
                    "org.liberty.android.fantastischmemo",
                    "com.evancharlton.mileage",
                    "com.hectorone.multismssender",
                    "com.kvance.Nectroid",
                    "com.fsck.k9",
                    "com.android.keepass"
            };

    List<String> list = Arrays.asList(sut);
    Map<String, TestRunner> runners = new HashMap<>();

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final ClassLoader loader = loadPackageParam.classLoader;
        final String packageName = loadPackageParam.packageName;

        if (list.contains(packageName)) {
            findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Application context = (Application) param.thisObject;

                    if (packageName.equals("android")) {
//                        int uid = context.getApplicationInfo().uid;
//                        new BroadcastHook(uid).hook(loader);
                    } else {
                        if( runners.get(packageName) == null) {
                            TestRunner testRunner = new TestRunner(context);
                            runners.put(packageName, testRunner);
                        }
                        TestRunner runner = runners.get(packageName);

                        new CrashHook(context).hook(loader);
                        new ActivityHook(runner, context).hook(loader);


//                    new AutoTestHook().hook(loader);
//                    new ActivityCoverageHook(packageName).hook(loader);
//                    new MotionEventHook().hook(loader);
//                    new GPSLocationHook().hook(loader);
//                    new ActionHook().hook(loader);
//                    new ContentHook().hook(loader);
//                    new ExceptionHook().hook(loader);
                    }
                }
            });
        }

    }
}
