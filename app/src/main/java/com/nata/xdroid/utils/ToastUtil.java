package com.nata.xdroid.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Calvin on 2016/12/9.
 */

public class ToastUtil {
    private static final String X_DROID = "xdroid:";
    public static void makeToast(Context context, String toast){
        Toast.makeText(context,X_DROID + toast,Toast.LENGTH_LONG).show();
    }
}
