package com.nata.xdroid.mocks;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.*;
import android.provider.ContactsContract.CommonDataKinds.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 2016/12/10.
 */

public class ContactMocker {
    /**
     * 批量添加通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    private static void BatchAddContact(Context context, List<ContactBean> list)
            throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = 0;
        for (ContactBean contact : list) {
            rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_TYPE, null)
                    .withValue(RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build());

            // 添加姓名
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(RawContacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(RawContacts.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.DISPLAY_NAME, contact.getName())
                    .withYieldAllowed(true).build());
            // 添加号码
            ops.add(ContentProviderOperation
                    .newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                    .withValue(Phone.NUMBER, contact.getNumber())
                    .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                    .withValue(Phone.LABEL, "").withYieldAllowed(true).build());
        }
        // 真正添加
        ContentProviderResult[] results = context.getContentResolver()
                .applyBatch(ContactsContract.AUTHORITY, ops);
    }

    public static void mockContacts(Context context) {
        List<ContactBean> list = new ArrayList<>();
        list.add(new ContactBean("Lily","1234567890"));
        list.add(new ContactBean("Andy","1234567891"));
        list.add(new ContactBean("Sandy","1234567892"));
        list.add(new ContactBean("Sucy","1234567893"));
        list.add(new ContactBean("Jack","1234567894"));
        list.add(new ContactBean("Mike","1234567895"));
        list.add(new ContactBean("Calvin","1234567896"));
        list.add(new ContactBean("John","1234567897"));
        list.add(new ContactBean("Sam","1234567898"));
        list.add(new ContactBean("Andrew","1234567899"));

        try {
            BatchAddContact(context,list);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }

    }


}
