package com.nata.xdroid;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.nata.xdroid.monkey.Monkey;
import com.nata.xdroid.monkey.MonkeyEvent;
import com.nata.xdroid.monkey.MonkeyKeyEvent;
import com.nata.xdroid.notifier.CommonNotice;
import com.nata.xdroid.notifier.Notifier;
import com.nata.xdroid.notifier.ToastNotifier;
import com.nata.xdroid.utils.ActivityUtil;
import com.nata.xdroid.utils.XPreferencesUtils;

import static com.nata.xdroid.utils.XPreferencesUtils.inTestMode;

/**
 * Created by Calvin on 2016/12/5.
 */

public class XMonkey extends Thread{
    private Monkey monkey;
    private boolean active = false;
    private String packageName;
    private int count = 0;
    private int activityCount = 0;
    private Context context;

    public XMonkey(Context context) {
        this.context = context;
        this.packageName = context.getPackageName();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Instrumentation instrumentation = new Instrumentation();
        PackageManager pm = context.getPackageManager();
        this.monkey = new Monkey(display, this.packageName, instrumentation, pm);

    }

    public void run() {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        MonkeyEvent event = null;

        while(true) {
            if(inTestMode() && active) {
                event = monkey.nextRandomEvent();
                count++;

                // notice users if no activities are found after a long time
                if(count %500 == 0) {
                    int curActivityCount = XPreferencesUtils.getCovActivityCount();
                    if( curActivityCount == activityCount) {
                        Notifier.notice(context, CommonNotice.NO_NEW_ACTIVITY);
                    }
                    activityCount = curActivityCount;
                }
            } else {
                // if event is back event and is out of application, then restart
                if(event instanceof MonkeyKeyEvent && ((MonkeyKeyEvent) event).getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if(!ActivityUtil.isRunningForeground(context,packageName)){
                        context.startActivity(launchIntent);
                    }
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

