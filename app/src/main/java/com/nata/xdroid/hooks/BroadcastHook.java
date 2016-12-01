package com.nata.xdroid.hooks;

import android.content.Intent;

import com.j256.ormlite.stmt.query.In;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/25.
 */

public class BroadcastHook implements Hook {
    private int uid;

    public BroadcastHook(int uid) {
        this.uid = uid;
    }

    @Override
    public void hook(ClassLoader loader) {
        findAndHookMethod("com.android.server.firewall.IntentFirewall",
                loader,
                "checkBroadcast",
                Intent.class,   // intent
                int.class,  // callerUid
                int.class,  // callerPid
                String.class,   // resolvedType
                int.class,  // receivingUid
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        int callerUid = (int) param.args[1];
                        int receivingUid = (int) param.args[4];
                        if (receivingUid == uid) {
                            Intent intent = (Intent) param.args[0];
                            String action = intent.getAction();
                            XposedBridge.log("hook IntentFirewall.checkBroadcast : " + "broadcast action -> " + action);
                        }
                    }
                });
    }
}
