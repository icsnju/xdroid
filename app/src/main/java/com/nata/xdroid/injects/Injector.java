package com.nata.xdroid.injects;

import android.content.Context;

/**
 * Created by Calvin on 2016/12/22.
 */

public class Injector {
    public static void mockContact(Context context) {
        ContactInjector.mockContacts(context);
    }
}
