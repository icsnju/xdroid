package com.nata.xdroid.utils;

import com.nata.xdroid.BuildConfig;

import java.util.HashSet;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by Calvin on 2016/11/24.
 */


public class XPreferencesUtils {

    private static XSharedPreferences instance = null;

    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences(BuildConfig.APPLICATION_ID, "pref_mine");
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        return instance;
    }

    public static boolean inMonitorMode() {
        return  !inTestMode();
    }

    public static boolean inTestMode() {
        return getInstance().getBoolean("test_mode", false);
    }

    public static boolean isNetWorkConnected() {
        return getInstance().getBoolean("network", false);
    }

    public static String getTestPackage() {
        return getInstance().getString("package", "com.tencent.mm");
    }

    public static int getCovActivityCount() {
        return getInstance().getStringSet("cov_acts",new HashSet<String>()).size();
    }

    public static boolean isInjection() {
        return getInstance().getBoolean("injection", true);
    }

    public static boolean isFakeGps() {
        return getInstance().getBoolean("gps", false);
    }

    public static boolean isXmonkey() {
        return getInstance().getBoolean("xmonkey", true);
    }



    public static boolean isAutoInput() {
        return getInstance().getBoolean("input", true);
    }

}



