package com.nata.xdroid.Hooks;

import android.app.ApplicationErrorReport;
import android.os.IBinder;
import de.robv.android.xposed.XC_MethodHook;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/25.
 */

public class CrashHook implements Hook{
    @Override
    public void hook(ClassLoader loader) {
        findAndHookMethod("com.android.server.am.ActivityManagerService", loader, "handleApplicationCrash", IBinder.class, ApplicationErrorReport.CrashInfo.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                ApplicationErrorReport.CrashInfo info = (ApplicationErrorReport.CrashInfo)param.args[1];
                log("Crash: " + info.exceptionClassName + " -> " + info.exceptionMessage);
            }
        });
    }
}
