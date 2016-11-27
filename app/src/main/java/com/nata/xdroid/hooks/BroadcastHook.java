package com.nata.xdroid.hooks;

import android.content.Intent;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/25.
 */

public class BroadcastHook implements Hook{
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
                        XposedBridge.log("hook IntentFirewall.checkBroadcast : " + "broadcast from " + callerUid + " to " + receivingUid);
                        Intent intent = (Intent) param.args[0];
                        String action = intent.getAction();
                        if (action == null)
                            return;
                        if (action.equals("android.intent.action.SCREEN_OFF"))
                            log("hook IntentFirewall.checkBroadcast : " + "screen off");
                        if (action.equals("android.intent.action.SCREEN_ON"))
                            log("hook IntentFirewall.checkBroadcast : " + "screen on");
                    }
                });
    }
}
