package com.nata.xdroid;

import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.WindowManager;

import com.nata.xdroid.monkey.Monkey;

import static com.nata.xdroid.utils.XPreferencesUtils.inTestMode;
import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Calvin on 2016/12/5.
 */

public class TestRunner  extends Thread{
    private Context context;
    private Monkey monkey;
    private boolean active = false;
    private String packageName;

    public TestRunner(Context context) {
        this.context = context;
        this.packageName = context.getPackageName();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Instrumentation instrumentation = new Instrumentation();
        PackageManager pm = context.getPackageManager();
        this.monkey = new Monkey(display, this.packageName, instrumentation, pm);

    }

    public void run() {
        while(true) {
            if(inTestMode() && active) {
                String event = monkey.nextRandomEvent();
                System.out.println(this.packageName + "-> " + event);
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    //    private String getForegroundApp() {
//        long ts = System.currentTimeMillis();
//        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,ts-2000, ts);
//        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
//            return null;
//        }
//        UsageStats recentStats = null;
//        for (UsageStats usageStats : queryUsageStats) {
//            if(recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()){
//                recentStats = usageStats;
//            }
//        }
//        return recentStats.getPackageName;
//    }
}

