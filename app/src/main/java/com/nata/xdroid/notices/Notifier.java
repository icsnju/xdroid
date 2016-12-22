package com.nata.xdroid.notices;

import android.content.Context;

/**
 * Created by Calvin on 2016/12/22.
 */

public class Notifier {
    public static void toast(Context context, String content) {
        ToastNotifier.makeToast(context,content);
    }

    public static void beem(Context context) {
        BeemNotifier.makeBeem(context);
    }

    public static void notice(Context context, String content) {
        BeemNotifier.makeBeem(context);
        ToastNotifier.makeToast(context,content);
    }
}
