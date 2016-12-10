package com.nata.xdroid.receivers;

/**
 * Created by Calvin on 2016/11/26.
 */

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.WindowManager;
import com.nata.xdroid.monkey.Monkey;

public class StartTestReceiver extends BroadcastReceiver {
    public static final String EXTRA_NAME_PACKAGE_NAME = "pkg_name";
    public static final String ACTION_START_TEST = "com.nata.xdroid.action.START_TEST";

    public static Intent getTestBroadCastIntent(String pkgName) {
        Intent intent = new Intent(ACTION_START_TEST);
        intent.putExtra(EXTRA_NAME_PACKAGE_NAME, pkgName);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        System.out.println("onReceive start test intent");
        if (ACTION_START_TEST.equals(intent.getAction())) {
            final String packageName = (String) intent.getSerializableExtra(EXTRA_NAME_PACKAGE_NAME);
            final PackageManager pm = context.getPackageManager();
            final WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            final Display display = wm.getDefaultDisplay();
            new Thread() {
                public void run() {
                    Instrumentation instrumentation = new Instrumentation();
                    Monkey monkey = new Monkey(display, packageName, instrumentation, pm);
                    for (int i = 0; i < 100; i++) {
                        String event = monkey.nextRandomEvent();
                        System.out.println(event);
                    }
                }
            }.start();
        }
    }
}
