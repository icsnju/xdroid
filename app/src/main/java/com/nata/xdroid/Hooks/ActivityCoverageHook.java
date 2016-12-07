package com.nata.xdroid.hooks;

import android.content.Context;
import android.os.Bundle;
import com.nata.xdroid.utils.ActivityUtil;
import java.util.HashSet;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import static com.nata.xdroid.utils.XPreferencesUtils.inTestMode;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class ActivityCoverageHook implements Hook{
    HashSet<String> activitySet;
    List<String> actList = null;
    String packageName ="";


    public ActivityCoverageHook(String packageName) {
        this.packageName = packageName;
        activitySet = new HashSet<>();
    }

    public void hook(final ClassLoader loader) {
        findAndHookMethod("android.app.Activity",loader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if(inTestMode()) {
                    Context context = (Context) param.thisObject;
                    if (actList == null ){
                        actList = ActivityUtil.getActivities(context, packageName);
                        log("AllActivities size: " +actList.size());
                    }

                    String activityName = context.getClass().getName();

                    activitySet.add(activityName);
                    log("Activity: " + activityName);
                }
            }
        });
    }

}
