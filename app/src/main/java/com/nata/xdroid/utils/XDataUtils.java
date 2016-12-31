package com.nata.xdroid.utils;

import com.nata.xdroid.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by Calvin on 2016/11/24.
 */


public class XDataUtils {

    private static XSharedPreferences instance = null;

    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences(BuildConfig.APPLICATION_ID, "user_data");
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        return instance;
    }

    public static String query(String key) {
        return getInstance().getString(key, "");
    }

}



