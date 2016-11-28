package com.nata.xdroid.hooks;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.EditText;

import com.nata.xdroid.db.beans.UserData;
import com.nata.xdroid.db.daos.UserDataDao;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import static com.nata.xdroid.utils.PreferencesUtils.inMonitorMode;
import static com.nata.xdroid.utils.PreferencesUtils.inTestMode;
import static com.nata.xdroid.utils.ViewUtil.getAllChildViews;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * Created by Calvin on 2016/11/21.
 */

public class EditTextHook {
    private UserDataDao userDataDao;

    public EditTextHook(Context context) {
        try {
            Context moduleContext = AndroidAppHelper.currentApplication().createPackageContext("com.nata.xdroid", Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        userDataDao = new UserDataDao(context);
//        List<UserData> list = userDataDao.getAll();
//        log(Arrays.toString(list.toArray()));
    }

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
                    if(!text.trim().equals("")){
                        UserData data = new UserData();
                        data.setPackageName(rootActivity.getPackageName());
                        data.setActivityName(rootActivity.getLocalClassName());
                        data.setResourceId(id);
                        data.setContent(text);
                        data.setStampTime((int) (System.currentTimeMillis() / 1000));
                        userDataDao.insert(data);
                        log("onPause: " + id + " " + text);
                    }
                }
            }
            }
        });

        findAndHookMethod("android.app.Activity", loader, "onStart", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("android.app.Activity.onStart");

            if (inTestMode()) {
                Activity rootActivity = (Activity) param.thisObject;
                List<View> views = getAllChildViews(rootActivity.getWindow().getDecorView(), EditText.class);
                for (int i = 0; i < views.size(); i++) {
                    EditText et = (EditText) views.get(i);
                    int id = et.getId();
                    UserData data = userDataDao.getUserDataByQuery(rootActivity.getPackageName(),rootActivity.getLocalClassName(),id);
                    if(data != null) {
                        String text = data.getContent();
                        if(text != null && !text.trim().equals("")){
                            et.setText(text);
                            log("onStart put text: " + id + " " + text);
                        }
                    } else {
                        log("没有数据");
                    }
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
                    if(!text.trim().equals("")){
                        UserData data = new UserData();
                        Activity rootActivity = dialog.getOwnerActivity();
                        data.setPackageName(rootActivity.getPackageName());
                        data.setActivityName(rootActivity.getLocalClassName());
                        data.setResourceId(id);
                        data.setContent(text);
                        data.setStampTime((int) (System.currentTimeMillis() / 1000));
                        userDataDao.insert(data);
                        log("dismiss: " + id + " " + text);
                    }

                }
            }
            }
        });
    }
}
