package com.nata.xdroid.Hooks;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.EditText;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.robv.android.xposed.XC_MethodHook;
import static com.nata.xdroid.Utils.PreferencesUtils.inMonitorMode;
import static com.nata.xdroid.Utils.PreferencesUtils.inTestMode;
import static com.nata.xdroid.Utils.ViewUtil.getAllChildViews;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * Created by Calvin on 2016/11/21.
 */

public class EditTextHook {
    Map<Integer, String> dataMap = new HashMap<>();

    public void hook(ClassLoader loader) {
        findAndHookMethod("android.app.Activity", loader, "onPause", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                if (inMonitorMode()) {
                    Activity rootActivity = (Activity) param.thisObject;
                    List<View> views = getAllChildViews(rootActivity.getWindow().getDecorView(), EditText.class);
                    for (int i = 0; i < views.size(); i++) {
                        EditText et = (EditText) views.get(i);
                        String text = et.getText().toString();
                        int id = et.getId();
                        if(!text.trim().equals("")) dataMap.put(id, text);
                        log("onPause: " + id + " " + text);
                    }
                }
            }
        });

        findAndHookMethod("android.app.Activity", loader, "onStart", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (inTestMode()) {
                    Activity rootActivity = (Activity) param.thisObject;
                    List<View> views = getAllChildViews(rootActivity.getWindow().getDecorView(), EditText.class);
                    for (int i = 0; i < views.size(); i++) {
                        EditText et = (EditText) views.get(i);
                        int id = et.getId();
                        String text = dataMap.get(id);
                        et.setText(text);
                        log("onStart put text: " + id + " " + text);
                    }
                }
            }
        });

        findAndHookMethod("android.app.Dialog", loader, "dismiss", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (!inMonitorMode()) {
                    Dialog dialog = (Dialog) param.thisObject;
                    List<View> views = getAllChildViews(dialog.getWindow().getDecorView(), EditText.class);
                    for (int i = 0; i < views.size(); i++) {
                        EditText et = (EditText) views.get(i);
                        String text = et.getText().toString();
                        int id = et.getId();
                        if(!text.trim().equals("")) dataMap.put(id, text);
                        log("dismiss: " + id + " " + text);
                    }
                }
            }
        });
    }
}
