package com.nata.xdroid.notifier;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by Calvin on 2016/12/9.
 */

public class ToastNotifier {
    private static final String X_DROID = "xdroid:";
    public static void makeToast(Context context, String toast){
        Toast.makeText(context,X_DROID + context.getPackageName() + " => " + toast,Toast.LENGTH_LONG).show();
    }
}
