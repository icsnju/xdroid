package com.nata.xdroid.utils;

import com.nata.xdroid.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by Calvin on 2016/11/24.
 */


public class PreferencesUtils {

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

//    public static boolean notSelf() {
//        return getInstance().getBoolean("not_self", false);
//    }
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



