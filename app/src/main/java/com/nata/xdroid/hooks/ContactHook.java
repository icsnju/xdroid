package com.nata.xdroid.hooks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.nata.xdroid.receivers.ContactMockReceiver;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static com.nata.xdroid.notices.ToastNotifier.makeToast;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/12/22.
 */

public class ContactHook implements Hook{
    private Context context;

    public ContactHook(Context context) {
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

                        // 联系人相关
                        boolean isContactRawContactsURI = uri.equals(ContactsContract.RawContacts.CONTENT_URI);
                        boolean isContactContactsURI = uri.equals(ContactsContract.Contacts.CONTENT_URI);
                        boolean isContactDataURI = uri.equals(ContactsContract.Data.CONTENT_URI);

                        boolean isContact = isContactRawContactsURI || isContactContactsURI || isContactDataURI;

                        if (isContact && cursor.getCount() == 0) {
                            makeToast(context, "应用请求联系人数据, 但手机中没有联系人信息,请添加或使用xdroid提供的工具");

                            Intent intent = ContactMockReceiver.getUserDataIntent();
                            context.sendBroadcast(intent);
                        }
                    }
                });
    }
}
