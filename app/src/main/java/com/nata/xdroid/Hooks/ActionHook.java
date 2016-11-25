package com.nata.xdroid.Hooks;

import android.view.View;
import android.widget.Button;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */


public class ActionHook implements Hook {
    @Override
    public void hook(ClassLoader loader) {
        // performClick
        findAndHookMethod("android.view.View", loader, "performClick", new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                View node = (View) param.thisObject;
                String action = "click widget : " + node.getId();
                if (node instanceof Button) {
                    action += ((Button) node).getText();
                }
                XposedBridge.log(action);

            }
        });

        // performLongClick
        findAndHookMethod("android.view.View", loader, "performLongClick", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                View node = (View) param.thisObject;
                String action = "longclick widget : " + node.getId();
                if (node instanceof Button) {
                    action += ((Button) node).getText();
                }
                XposedBridge.log(action);
            }
        });
    }
}
