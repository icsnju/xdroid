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

    /**
     * 是否处于监控模式
     * @return
     */
    public static boolean inMonitorMode() {
        return  !inTestMode();
    }

    /**
     * 是否处于测试模式
     * @return
     */
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


//
//    public static boolean notWhisper() {
//        return getInstance().getBoolean("not_whisper", false);
//    }
//
//    public static boolean notMute() {
//        return getInstance().getBoolean("not_mute", false);
//    }
//
//    public static boolean delay() {
//        return getInstance().getBoolean("delay", false);
//    }
//
//    public static int delayTime() {
//        return getInstance().getInt("delay_time", 0);
//    }
//
//    public static boolean quickOpen() {
//        return getInstance().getBoolean("quick_open", true);
//    }

}



