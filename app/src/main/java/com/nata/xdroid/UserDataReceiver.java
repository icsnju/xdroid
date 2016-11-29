package com.nata.xdroid;

/**
 * Created by Calvin on 2016/11/26.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nata.xdroid.db.beans.UserData;
import com.nata.xdroid.db.daos.UserDataDao;

public class UserDataReceiver extends BroadcastReceiver {
    public static final String EXTRA_NAME_USER_DATA = "user_data";
    public static final String ACTION_LOG_DATA = "com.nata.xdroid.action.LOG_DATA";

    public static Intent getUserDataIntent(UserData data) {
        Intent intent = new Intent(ACTION_LOG_DATA);
        intent.putExtra(EXTRA_NAME_USER_DATA, data);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_LOG_DATA.equals(intent.getAction())) {
            UserData userData = (UserData) intent.getExtras().getSerializable(EXTRA_NAME_USER_DATA);
            UserDataDao userDataDao = new UserDataDao(context);
            userDataDao.insert(userData);
        }
    }
}
