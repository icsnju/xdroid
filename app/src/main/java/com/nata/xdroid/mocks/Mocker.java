package com.nata.xdroid.mocks;

import android.content.Context;

/**
 * Created by Calvin on 2016/12/22.
 */

public class Mocker {
    public static void mockContact(Context context) {
        ContactMocker.mockContacts(context);
    }
}
