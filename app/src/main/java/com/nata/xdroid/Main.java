package com.nata.xdroid;

import com.nata.xdroid.hooks.CrashHook;
import com.nata.xdroid.hooks.EditTextHook;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Calvin on 2016/11/21.
 */

public class Main implements IXposedHookLoadPackage {

    public static final String ZHNT_PACKAGE_NAME = "com.cvicse.zhnt";
    public static final String LOGCAT_PACKAGE_NAME = "org.jtb.alogcat";
    public static final String MM_PACKAGE_NAME = "com.tencent.mm";
    public static final String MULTISMS_PACKAGE_NAME = "com.hectorone.multismssender";
    public static final String CRASH_PACKAGE_NAME="com.nata.crashapplication";

    public static final String []sut =
            {       "com.fsck.k9",
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
                    "com.android.keepass"
            };

    List<String> list = Arrays.asList(sut);
    public static final String packageName = CRASH_PACKAGE_NAME;


    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        ClassLoader loader = loadPackageParam.classLoader;

        if(loadPackageParam.packageName.equals("android")) {
            new CrashHook().hook(loader);
//            new BroadcastHook().hook(loader);
        }


        /**
         * App specific hook
         */
        if (list.contains(loadPackageParam.packageName)) {
//            new ActivityCoverageHook(packageName).hook(loader);
            new EditTextHook().hook(loader);
//            new MotionEventHook().hook(loader);
//            new GPSLocationHook().hook(loader);
//            new ActionHook().hook(loader);
//            new ContentHook().hook(loader);
//            new ExceptionHook().hook(loader);
        }


    }
}
