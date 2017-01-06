package com.nata.xdroid.hooks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.nata.xdroid.notifier.CommonNotice;
import com.nata.xdroid.notifier.Notifier;
import com.nata.xdroid.receivers.ContactMockReceiver;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/12/22.
 */

public class ContentsHook implements Hook{
    private Context context;

    public ContentsHook(Context context) {
        this.context = context;
    }
    @Override
    public void hook(ClassLoader loader) {
        findAndHookMethod("android.content.ContentResolver", loader, "query", Uri.class, String[].class, String.class, String[].class, String.class
                , new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("afterHookedMethod: " + "query");
                        Uri uri = (Uri) param.args[0];
                        Cursor cursor = (Cursor) param.getResult();
                        ContentsProcessor.solve(context, uri, cursor);
                    }
                });
    }
}
