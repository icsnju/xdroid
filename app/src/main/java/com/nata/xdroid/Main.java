package com.nata.xdroid;

import android.Manifest;
import android.app.Application;

import com.nata.xdroid.hooks.ANRHook;
import com.nata.xdroid.hooks.ActivityCoverageHook;
import com.nata.xdroid.hooks.ActivityHook;
import com.nata.xdroid.hooks.BluetoothHook;
import com.nata.xdroid.hooks.ContentsHook;
import com.nata.xdroid.hooks.CrashHook;
import com.nata.xdroid.hooks.LocationHook;
import com.nata.xdroid.hooks.NetworkHook;
import com.nata.xdroid.hooks.UncaughtExceptionHook;
import com.nata.xdroid.hooks.XMonkeyHook;
import com.nata.xdroid.utils.PermissionUtil;
import com.nata.xdroid.utils.XPreferencesUtils;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class Main implements IXposedHookLoadPackage {
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final ClassLoader loader = loadPackageParam.classLoader;
        final String packageName = loadPackageParam.packageName;

        String targetPackage = XPreferencesUtils.getTestPackage();
        final boolean isInjection = XPreferencesUtils.isInjection();
        final boolean isXMonkey = XPreferencesUtils.isXmonkey();


        if (targetPackage.equals(packageName) || packageName.equals("android")) {
            findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Application context = (Application) param.thisObject;

                    if (packageName.equals("android")) {
                        new ANRHook(context).hook(loader);
                        new CrashHook(context).hook(loader);
                    } else {
                        new UncaughtExceptionHook(context).hook(loader);
                        new ActivityCoverageHook(context).hook(loader);

                        if(isXMonkey) {
                            XMonkey runner = new XMonkey(context);
                            runner.start();
                            new XMonkeyHook(runner).hook(loader);
                        }

                        // if the dependency injection option is opened
                        if (isInjection) {
                            new ActivityHook(context, packageName).hook(loader);
                            List<String> permissions = PermissionUtil.getGrantedPermissions(context, packageName);

                            new ContentsHook(context).hook(loader);

                            // Bluetooth related Hook
                            if (permissions.contains(Manifest.permission.BLUETOOTH)) {
                                new BluetoothHook(context).hook(loader);
                            }

                            // GPS related Hook
                            if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) ||
                                    permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                new LocationHook(context).hook(loader);
                            }

                            // Network related Hook
                            if (permissions.contains(Manifest.permission.INTERNET)) {
                                new NetworkHook(context).hook(loader);
                            }
                        }
                    }
                }
            });
        }

    }
}
