package com.nata.xdroid.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;

import java.util.ArrayList;

import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Calvin on 2016/11/24.
 */

public class ActivityUtil {
    public static ArrayList<String> getActivities(Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ActivityInfo[] list = info.activities;
        ArrayList<String>acts = new ArrayList<>();
        if(list!= null) {
            for (ActivityInfo act: list) {
                acts.add(act.name);
            }
        }

        return acts;
    }




}
