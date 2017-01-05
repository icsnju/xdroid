package com.nata.xdroid;

import android.Manifest;
import android.app.Application;

import com.nata.xdroid.hooks.ANRHook;
import com.nata.xdroid.hooks.ActivityCoverageHook;
import com.nata.xdroid.hooks.ActivityHook;
import com.nata.xdroid.hooks.BluetoothHook;
import com.nata.xdroid.hooks.ContactHook;
import com.nata.xdroid.hooks.CalendarHook;
import com.nata.xdroid.hooks.CrashHook;
import com.nata.xdroid.hooks.LocationHook;
import com.nata.xdroid.hooks.NetworkHook;
import com.nata.xdroid.hooks.UncaughtExceptionHook;
import com.nata.xdroid.notices.ToastNotifier;
import com.nata.xdroid.utils.ActivityUtil;
import com.nata.xdroid.utils.PermissionUtil;
import com.nata.xdroid.utils.XPreferencesUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        final boolean isOpen = XPreferencesUtils.isOpen();


        if (targetPackage.equals(packageName) || packageName.equals("android")) {
            findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Application context = (Application) param.thisObject;

                    if (packageName.equals("android")) {
//                        int uid = context.getApplicationInfo().uid;
//                        new BroadcastHook(uid).hook(loader);
                        new ANRHook(context).hook(loader);
                        new CrashHook(context).hook(loader);
                    } else {
                        new UncaughtExceptionHook(context).hook(loader);
                        new ActivityCoverageHook(context).hook(loader);

                        // 如果开启插入依赖的选项
                        if (isOpen) {
                            // 启动TestRunner
                            TestRunner runner = new TestRunner(context);
                            runner.start();

                            // Activity相关Hook
                            new ActivityHook(runner, context, packageName).hook(loader);

                            // 获取被赋予权限的Permission
                            List<String> permissions = PermissionUtil.getGrantedPermissions(context, packageName);

                            // 联系人相关Hook
                            if (permissions.contains(Manifest.permission.READ_CONTACTS)) {
                                new ContactHook(context).hook(loader);
                                XposedBridge.log("检测到读取联系人的权限, hook联系人");
                            }

                            // 蓝牙相关Hook
                            if (permissions.contains(Manifest.permission.BLUETOOTH)) {
                                new BluetoothHook(context).hook(loader);
                                XposedBridge.log("检测到蓝牙权限, hook蓝牙");
                            }

                            // 日历相关Hook
                            if (permissions.contains(Manifest.permission.READ_CALENDAR)) {
                                new CalendarHook(context).hook(loader);
                                XposedBridge.log("检测到日历权限, hook蓝牙");
                            }

                            // 位置相关Hook
                            if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) ||
                                    permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                new LocationHook(context).hook(loader);
                                XposedBridge.log("检测到位置权限, hook GPS");
                            }

                            // 网络相关权限
                            if (permissions.contains(Manifest.permission.INTERNET)) {
                                new NetworkHook(context).hook(loader);
                                XposedBridge.log("检测到位置权限, hook 网络相关权限");
                            }
                        }

                    }
                }
            });
        }

    }
}
