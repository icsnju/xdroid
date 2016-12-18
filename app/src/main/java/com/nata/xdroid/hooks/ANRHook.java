package com.nata.xdroid.hooks;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/25.
 */

public class ANRHook implements Hook {
    @Override
    public void hook(final ClassLoader loader) {

        // Handle ANR
        findAndHookMethod("com.android.server.am.ActivityManagerService", loader, "appNotResponding", "com.android.server.am.ProcessRecord", "com.android.server.am.ActivityRecord", "com.android.server.am.ActivityRecord",boolean.class,String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String annotation = (String) param.args[4];
                log("Hook ANR: " + annotation);
            }
        });

//        findAndHookConstructor("com.android.server.am.AppNotRespondingDialog", loader,
//                "com.android.server.am.ActivityManagerService",
//                Context.class,
//                "com.android.server.am.ProcessRecord",
//                "com.android.server.am.ActivityRecord",
//                boolean.class,
//                new XC_MethodHook() {
//
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
//                        Context context = (Context) methodHookParam.args[1];
//                         log("Hook ANR" + ": " + context.getPackageName());
//                    }
//                });

    }
}
