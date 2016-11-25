package com.nata.xdroid.Hooks;

import android.app.Activity;
import android.net.Uri;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class ContentHook implements Hook{
    @Override
    public void hook(ClassLoader loader) {
        findAndHookMethod("android.content.ContentResolver", loader, "query",Uri.class,String[].class,String.class,String[].class,String.class,CancellationSignal.class
                , new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("beforeHookedMethod: " + "query1");
                Uri uri = (Uri)param.args[0];
                log(uri.toString());
            }
        });

        findAndHookMethod("android.content.ContentResolver", loader, "query",Uri.class,String[].class,String.class,String[].class,String.class
                , new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("beforeHookedMethod: " + "query2");
                        Uri uri = (Uri)param.args[0];
                        log(uri.toString());
                    }
                });
    }
}
