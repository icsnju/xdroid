package com.nata.xdroid;

import android.Manifest;
import android.app.Application;

import com.nata.xdroid.hooks.ANRHook;
import com.nata.xdroid.hooks.ActivityHook;
import com.nata.xdroid.hooks.BluetoothHook;
import com.nata.xdroid.hooks.ContactHook;
import com.nata.xdroid.hooks.CalendarHook;
import com.nata.xdroid.hooks.CrashHook;
import com.nata.xdroid.hooks.GPSLocationHook;
import com.nata.xdroid.hooks.NetworkHook;
import com.nata.xdroid.utils.PermissionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
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
                    "com.android.keepass",
                    "com.tencent.mobileqq",
                    "com.borneq.heregpslocation",
                    "com.nata.crashapplication",
                    "tw.qtlin.mac.airunlocker"
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
                        new ANRHook(context).hook(loader);
                    } else {
                        // 启动TestRunner
                        TestRunner runner = runners.get(packageName);
                        if (runner == null) {
                            runner = new TestRunner(context);
                            runners.put(packageName, runner);
                        }

                        // 获取被赋予权限的Permission
                        List<String> permissions = PermissionUtil.getGrantedPermissions(context, packageName);

                        // 联系人相关Hook
                        if(permissions.contains(Manifest.permission.READ_CONTACTS)){
                            new ContactHook(context).hook(loader);
                            XposedBridge.log("检测到读取联系人的权限, hook联系人");
                        }

                        // 蓝牙相关Hook
                        if(permissions.contains(Manifest.permission.BLUETOOTH)){
                            new BluetoothHook(context).hook(loader);
                            XposedBridge.log("检测到蓝牙权限, hook蓝牙");
                        }

                        // 日历相关Hook
                        if(permissions.contains(Manifest.permission.READ_CALENDAR)){
                            new CalendarHook(context).hook(loader);
                            XposedBridge.log("检测到日历权限, hook蓝牙");
                        }

                        // 位置相关Hook
                        if(permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) ||
                                permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            new GPSLocationHook(context).hook(loader);
                            XposedBridge.log("检测到位置权限, hook GPS");
                        }

                        // 网络相关权限
                        if(permissions.contains(Manifest.permission.INTERNET)) {
                            new NetworkHook(context).hook(loader);
                            XposedBridge.log("检测到位置权限, hook 网络相关权限");
                        }

                        new CrashHook(context).hook(loader);
                        new ActivityHook(runner, context, packageName).hook(loader);

                    }
                }
            });
        }

    }
}
