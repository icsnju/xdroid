package com.nata.xdroid.hooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;

import com.nata.xdroid.XMonkey;
import com.nata.xdroid.notifier.CommonNotice;
import com.nata.xdroid.notifier.Notifier;
import com.nata.xdroid.receivers.NewActivityReceiver;
import com.nata.xdroid.utils.ViewUtil;
import com.nata.xdroid.utils.XPreferencesUtils;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

import static com.nata.xdroid.utils.XPreferencesUtils.inMonitorMode;
import static com.nata.xdroid.utils.ViewUtil.getAllChildViews;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * Created by Calvin on 2016/11/21.
 */

public class ActivityHook {
    private Context context;

    String packageName;

    public ActivityHook(Context context, String packageName) {
        this.context = context;
        this.packageName = packageName;
    }

    public void hook(final ClassLoader loader) {

        findAndHookMethod("android.app.Activity", loader, "onPause", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (inMonitorMode()) {
                    Activity rootActivity = (Activity) param.thisObject;
                    List<View> views = getAllChildViews(rootActivity.getWindow().getDecorView(), EditText.class);
                    ViewUtil.persistUserData(context, views);
                }
            }
        });

        findAndHookMethod("android.app.Activity", loader, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                final Activity rootActivity = (Activity) param.thisObject;
                List<View> views = getAllChildViews(rootActivity.getWindow().getDecorView(), EditText.class);
                if(XPreferencesUtils.isAutoInput()) {
                    ViewUtil.fillUserData(context, views);
                }
            }
        });

        findAndHookMethod("android.app.Activity", loader, "startActivityForResult", Intent.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("beforeHookedMethod: " + "startActivityForResult1");
                Intent intent = (Intent) param.args[0];
                processIntent(intent);
            }
        });
    }

    private void processIntent(Intent intent) {
        if (intent == null) return;
        log(intent.toString());

        String action = intent.getAction();
        if (action != null) {
            if (action.equals(Intent.ACTION_OPEN_DOCUMENT))
                Notifier.notice(context, CommonNotice.ACTION_OPEN_DOCUMENT);
            else if (action.equals(Intent.ACTION_CHOOSER))
                Notifier.notice(context, CommonNotice.ACTION_CHOOSER);
            else if (action.equals(MediaStore.ACTION_IMAGE_CAPTURE)) {
                Notifier.notice(context, CommonNotice.ACTION_CAMERA);
            }else if(action.equals(Intent.ACTION_PICK)) {
                if(intent.getType().startsWith("image")) { // 图片
                    Notifier.notice(context,"App opens image galley");
                } else if(intent.getType().startsWith("videos")) {// 视频

                }
            }
        }
    }
}

