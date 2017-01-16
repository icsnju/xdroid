package com.nata.xdroid.hooks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.nata.xdroid.notifier.CommonNotice;
import com.nata.xdroid.notifier.Notifier;
import com.nata.xdroid.receivers.AudiosMockReceiver;
import com.nata.xdroid.receivers.ContactMockReceiver;
import com.nata.xdroid.receivers.ImagesMockReceiver;

/**
 * Created by Calvin on 2017/1/6.
 */

public class ContentsProcessor {
    public static void solve(Context context, Uri uri, Cursor cursor) {
        if(cursor == null || cursor.getCount() != 0) return ;

        // 联系人相关
        if (isContacts(uri)) {
            Notifier.notice(context, CommonNotice.CONTACT);

            // inject
            Intent intent = ContactMockReceiver.getMockContactsIntent();
            context.sendBroadcast(intent);
            return;
        }

        // 日历相关
        if (isCalendar(uri)) {
            Notifier.notice(context, CommonNotice.CALENDAR);

            // 产生日历数据
            return ;
        }

        // 图片相关
        if(isImages(uri)) {
            Notifier.notice(context, CommonNotice.IMAGES);

            // inject
            Intent imageIntent = ImagesMockReceiver.getMockImagesIntent();
            context.sendBroadcast(imageIntent);
            return;
        }

        if(isAudios(uri)) {
            Notifier.notice(context, CommonNotice.AUDIOS);

            // inject
            Intent audioIntent = AudiosMockReceiver.getMockAudiosIntent();
            context.sendBroadcast(audioIntent);
            return;
        }

    }

    private static boolean isContacts(Uri uri) {

        boolean isContactRawContactsURI = uri.equals(ContactsContract.RawContacts.CONTENT_URI);
        boolean isContactContactsURI = uri.equals(ContactsContract.Contacts.CONTENT_URI);
        boolean isContactDataURI = uri.equals(ContactsContract.Data.CONTENT_URI);

        return isContactRawContactsURI || isContactContactsURI || isContactDataURI;
    }

    private static boolean isCalendar(Uri uri) {
        boolean isCalendarEventURI = uri.equals(CalendarContract.Events.CONTENT_URI);
        boolean isCalendarRemindersURI = uri.equals(CalendarContract.Reminders.CONTENT_URI);
        boolean isCalendarCalendarsURI = uri.equals(CalendarContract.Calendars.CONTENT_URI);
        boolean isCalendarInstancesURI = uri.equals(CalendarContract.Instances.CONTENT_URI);
        boolean isCalendarAttendeesURI = uri.equals(CalendarContract.Attendees.CONTENT_URI);

        return isCalendarEventURI || isCalendarRemindersURI || isCalendarCalendarsURI || isCalendarInstancesURI || isCalendarAttendeesURI;

    }

    private static boolean isImages(Uri uri) {
        return uri.equals(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    private static boolean isAudios(Uri uri) {
        return uri.equals(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

}
