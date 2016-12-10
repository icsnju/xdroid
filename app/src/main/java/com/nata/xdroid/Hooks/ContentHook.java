package com.nata.xdroid.hooks;

import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.RemoteException;
import android.provider.CalendarContract;
import android.provider.ContactsContract;

import com.nata.xdroid.receivers.ContactMockReceiver;
import com.nata.xdroid.utils.ContactUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static com.nata.xdroid.utils.ToastUtil.makeToast;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Calvin on 2016/11/21.
 */

public class ContentHook implements Hook {
    private Context context;

    public ContentHook(Context context) {
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
                        processURI(uri, cursor);
                    }
                });
    }

    private void processURI(Uri uri, Cursor cursor) {
        log(uri.toString());

        // 联系人相关
        boolean isContactRawContactsURI = uri.equals(ContactsContract.RawContacts.CONTENT_URI);
        boolean isContactContactsURI = uri.equals(ContactsContract.Contacts.CONTENT_URI);
        boolean isContactDataURI = uri.equals(ContactsContract.Data.CONTENT_URI);

        boolean isContact = isContactRawContactsURI || isContactContactsURI || isContactDataURI;

        // 日历相关
        boolean isCalendarEventURI = uri.equals(CalendarContract.Events.CONTENT_URI);
        boolean isCalendarRemindersURI = uri.equals(CalendarContract.Reminders.CONTENT_URI);
        boolean isCalendarCalendarsURI = uri.equals(CalendarContract.Calendars.CONTENT_URI);
        boolean isCalendarInstancesURI = uri.equals(CalendarContract.Instances.CONTENT_URI);
        boolean isCalendarAttendeesURI = uri.equals(CalendarContract.Attendees.CONTENT_URI);

        boolean isCalendar = isCalendarEventURI || isCalendarRemindersURI || isCalendarCalendarsURI || isCalendarInstancesURI || isCalendarAttendeesURI;


        if (isContact && cursor.getCount() == 0) {
            makeToast(context, "应用请求联系人数据, 但手机中没有联系人信息,请添加或使用xdroid提供的工具");

            Intent intent = ContactMockReceiver.getUserDataIntent();
            context.sendBroadcast(intent);
        }

        if (isCalendar && cursor.getCount() == 0) {
            makeToast(context, "应用请求日历信息, 但日历中中没有相关信息,请添加或使用xdroid提供的工具");
        }

    }
}
