package com.nata.xdroid.Hooks;

import android.view.MotionEvent;
import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Calvin on 2016/11/21.
 */

public class MotionEventHook implements Hook {
    @Override
    public void hook(ClassLoader loader) {
        findAndHookMethod("android.app.Activity", loader, "dispatchTouchEvent", MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                MotionEvent event = (MotionEvent) param.args[0];
                String codinates = "(" + event.getX() + "," + event.getY() + ")";
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP : log("Activity---onTouchEvent---UP" + codinates);break;
                    case MotionEvent.ACTION_DOWN : log("Activity---onTouchEvent---DOWN" + codinates);break;
                    case MotionEvent.ACTION_MOVE : log("Activity---onTouchEvent---MOVE" + codinates);break;
                    default:break;
                }
            }
        });
    }
}
