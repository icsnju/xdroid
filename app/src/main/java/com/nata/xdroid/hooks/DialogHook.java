package com.nata.xdroid.hooks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.nata.xdroid.utils.ViewUtil;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

import static com.nata.xdroid.utils.XPreferencesUtils.inMonitorMode;
import static com.nata.xdroid.utils.ViewUtil.getAllChildViews;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/12/5.
 */

public class DialogHook implements Hook {
    private Context context;

    public DialogHook(Context context) {
        this.context = context;
    }

    @Override
    public void hook(ClassLoader loader) {
        findAndHookMethod("android.app.Dialog", loader, "dismiss", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (inMonitorMode()) {
                    Dialog dialog = (Dialog) param.thisObject;
                    Activity rootActivity = dialog.getOwnerActivity();
                    if (rootActivity == null) return;
                    List<View> views = getAllChildViews(dialog.getWindow().getDecorView(), EditText.class);
                    ViewUtil.persistUserData(context, views);
                }
            }
        });

        findAndHookMethod("android.app.Dialog", loader, "show", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Dialog dialog = (Dialog) param.thisObject;
                Activity rootActivity = dialog.getOwnerActivity();
                if (rootActivity == null) return;
                List<View> views = getAllChildViews(dialog.getWindow().getDecorView(), EditText.class);
                ViewUtil.fillUserData(context, views);
            }
        });
    }
}
