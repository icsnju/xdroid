package com.nata.xdroid.hooks;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.nata.xdroid.notifier.CommonNotice;
import com.nata.xdroid.notifier.Notifier;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/12/22.
 */

public class BluetoothHook implements Hook{
    Context context;

    public BluetoothHook(Context context) {
        this.context =context;
    }


    @Override
    public void hook(ClassLoader loader) {
        findAndHookMethod("android.bluetooth.BluetoothAdapter", loader, "getDefaultAdapter", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                BluetoothAdapter bluetoothAdapter = (BluetoothAdapter)param.getResult();
                if(bluetoothAdapter == null) {
                    Notifier.notice(context, CommonNotice.NO_BLUETOOTH);
                    return;
                }

                if(!bluetoothAdapter.isEnabled()){
                    Notifier.notice(context, CommonNotice.BLUETOOTH_NOT_ENABLED);
                }
            }
        });
    }
}
