package com.nata.xdroid.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;

import java.util.ArrayList;
import java.util.List;

import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Calvin on 2016/11/24.
 */

public class ActivityUtil {
    public static ArrayList<String> getActivities(Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo info;
        try {
            info = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
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

    public static boolean isRunningForeground(Context context, String packageName){
        String topActivityClassName= getTopActivityName(context);
        if (packageName!=null&&topActivityClassName!=null&&topActivityClassName.startsWith(packageName)) {
            System.out.println("---> isRunningForeGround");
            return true;
        } else {
            System.out.println("---> isRunningBackGround");
            return false;
        }
    }


    private static String getTopActivityName(Context context){
        String topActivityClassName=null;
        ActivityManager activityManager =
                (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
        if(runningTaskInfos != null){
            ComponentName f=runningTaskInfos.get(0).topActivity;
            topActivityClassName=f.getClassName();
        }
        return topActivityClassName;
    }




}
