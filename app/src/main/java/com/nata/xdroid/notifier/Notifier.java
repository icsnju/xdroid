package com.nata.xdroid.notifier;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.nata.xdroid.receivers.ToastReceiver;

import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Calvin on 2016/12/22.
 */

public class Notifier {
    public static void toast(Context context, String content) {
        ToastNotifier.makeToast(context,content);
    }

    public static void beem(Context context) {
        BeepNotifier.makeBeem(context);
    }

    public static void notice(final Context context, final String content) {
        log("Notice => " + content);
        Intent toastIntent = ToastReceiver.getToastIntent(content);
        context.sendBroadcast(toastIntent);


//        final Thread toastThread= new Thread(new Runnable() {
//
//            @Override
//            public void run()
//            {
//                Looper.prepare();
//                BeepNotifier.makeBeem(context);
//                ToastNotifier.makeToast(context.getApplicationContext(),content);
//                Looper.loop();
//            }
//        });
//        toastThread.start();
    }
}
