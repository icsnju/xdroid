package com.nata.xdroid.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Calvin on 2016/12/31.
 */

public class AppUtil {

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
