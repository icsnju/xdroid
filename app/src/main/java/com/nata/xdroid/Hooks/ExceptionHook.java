package com.nata.xdroid.hooks;

import java.io.IOException;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Calvin on 2016/11/21.
 */

public class ExceptionHook implements Hook {

    @Override
    public void hook(ClassLoader loader) {
        hookAllConstructors(IOException.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("Throwable " + (Throwable) param.thisObject);
            }
        });
    }
}
