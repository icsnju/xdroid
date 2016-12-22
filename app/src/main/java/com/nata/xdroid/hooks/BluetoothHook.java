package com.nata.xdroid.hooks;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.nata.xdroid.notices.CommonNotice;

import de.robv.android.xposed.XC_MethodHook;

import static com.nata.xdroid.notices.ToastNotifier.makeToast;
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
                log("afterHookedMethod getDefaultAdapter");
                BluetoothAdapter bluetoothAdapter = (BluetoothAdapter)param.getResult();
                if(bluetoothAdapter == null) {
                    makeToast(context, CommonNotice.BLUETOOTH);
                    return;
                }

                if(!bluetoothAdapter.isEnabled()){
                    makeToast(context, "应用请求蓝牙信息, 但手机没有开启蓝牙");
                }
            }
        });
    }
}
