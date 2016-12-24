package com.nata.xdroid.hooks;

import android.content.Context;
import android.content.Intent;

import com.nata.xdroid.notices.CommonNotice;
import com.nata.xdroid.notices.Notifier;
import com.nata.xdroid.receivers.CrashReportReceiver;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/12/24.
 */

public class UncaughtExceptionHook implements Hook{

    private Context context;

    public UncaughtExceptionHook(Context context) {
        this.context = context;
    }

    @Override
    public void hook(ClassLoader loader) {
        // Handle uncaughtException
        Class<?> classHandler = Thread.getDefaultUncaughtExceptionHandler().getClass();
        findAndHookMethod(classHandler, "uncaughtException", Thread.class, Throwable.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Notifier.notice(context, CommonNotice.CRASH);
                        Intent intent = CrashReportReceiver.getCrashBroadCastIntent((Throwable) param.args[1], context.getPackageName());
                        context.sendBroadcast(intent);
                    }
                });
    }
}
