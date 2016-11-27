package com.nata.xdroid.hooks;

import android.app.Application;
import android.app.ApplicationErrorReport;
import android.content.Intent;
import android.os.IBinder;

import com.nata.xdroid.CrashReportReceiver;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/25.
 */

public class CrashHook implements Hook {
    @Override
    public void hook(final ClassLoader loader) {

        findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                final Application context = (Application) param.thisObject;
                findAndHookMethod("com.android.server.am.ActivityManagerService", loader, "handleApplicationCrash", IBinder.class, ApplicationErrorReport.CrashInfo.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        ApplicationErrorReport.CrashInfo info = (ApplicationErrorReport.CrashInfo) param.args[1];
                        log("Crash: " + info.exceptionClassName + " -> " + info.exceptionMessage);
                        Intent intent = CrashReportReceiver.getCrashBroadCastIntent(info, context.getPackageName());
                        context.sendBroadcast(intent);
                    }
                });
            }
        });
    }
}
