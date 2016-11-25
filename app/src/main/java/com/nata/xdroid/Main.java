package com.nata.xdroid;

import android.app.ApplicationErrorReport;
import android.os.IBinder;

import com.nata.xdroid.Hooks.ActionHook;
import com.nata.xdroid.Hooks.ActivityCoverageHook;
import com.nata.xdroid.Hooks.ContentHook;
import com.nata.xdroid.Hooks.CrashHook;
import com.nata.xdroid.Hooks.EditTextHook;
import com.nata.xdroid.Hooks.ExceptionHook;
import com.nata.xdroid.Hooks.GPSLocationHook;
import com.nata.xdroid.Hooks.MotionEventHook;
import com.nata.xdroid.Utils.PreferencesUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class Main implements IXposedHookLoadPackage {

    public static final String ZHNT_PACKAGE_NAME = "com.cvicse.zhnt";
    public static final String LOGCAT_PACKAGE_NAME = "org.jtb.alogcat";
    public static final String MM_PACKAGE_NAME = "com.tencent.mm";
    public static final String MULTISMS_PACKAGE_NAME = "com.hectorone.multismssender";
    public static final String CRASH_PACKAGE_NAME="com.nata.crashapplication";


    public static final String packageName = CRASH_PACKAGE_NAME;


    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        ClassLoader loader = loadPackageParam.classLoader;
        if (loadPackageParam.packageName.equals(packageName)) {
//            new ActivityCoverageHook(packageName).hook(loader);
//            new EditTextHook().hook(loader);
//            new MotionEventHook().hook(loader);
//            new GPSLocationHook().hook(loader);
//            new ActionHook().hook(loader);
//            new ContentHook().hook(loader);
//            new ExceptionHook().hook(loader);
        }

        if(loadPackageParam.packageName.equals("android")) {
            new CrashHook().hook(loader);
        }
    }
}
