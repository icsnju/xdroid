package com.nata.xdroid.hooks;

import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.nata.xdroid.notices.CommonNotice;
import com.nata.xdroid.notices.Notifier;
import com.nata.xdroid.receivers.CrashReportReceiver;
import com.nata.xdroid.utils.FormatUtil;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/25.
 */

public class CrashHook implements Hook {
    private Context context;

    public CrashHook(Context context) {
        this.context = context;
    }


    @Override
    public void hook(final ClassLoader loader) {
        // Handle Crash
        findAndHookMethod("com.android.server.am.ActivityManagerService", loader, "handleApplicationCrash", IBinder.class, ApplicationErrorReport.CrashInfo.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                ApplicationErrorReport.CrashInfo info = (ApplicationErrorReport.CrashInfo) param.args[1];
                Notifier.notice(context, CommonNotice.CRASH + info.throwClassName + "->" + info.exceptionClassName + " -> " + info.exceptionMessage + " -> " + info.stackTrace);
            }
        });

    }
}
