package com.nata.xdroid.receivers;

/**
 * Created by Calvin on 2016/11/26.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nata.xdroid.db.beans.CrashInfo;
import com.nata.xdroid.db.daos.RecordDao;
import com.nata.xdroid.utils.FormatUtil;

public class CrashReportReceiver extends BroadcastReceiver {
    public static final String EXTRA_NAME_CRASH_INFO = "crash_info";
    public static final String EXTRA_NAME_PACKAGE_NAME = "pkg_name";
    public static final String ACTION_REPORT_CRASH = "com.nata.xdroid.action.REPORT_CRASH";

    public static Intent getCrashBroadCastIntent(Throwable throwable, String pkgName) {
        Intent intent = new Intent(ACTION_REPORT_CRASH);
        intent.putExtra(EXTRA_NAME_CRASH_INFO, throwable);
        intent.putExtra(EXTRA_NAME_PACKAGE_NAME, pkgName);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_REPORT_CRASH.equals(intent.getAction())) {
            Throwable throwable = (Throwable) intent.getSerializableExtra(EXTRA_NAME_CRASH_INFO);
            String packageName = (String) intent.getSerializableExtra(EXTRA_NAME_PACKAGE_NAME);

            final String exceptionDetail = FormatUtil.getExceptionDetail(throwable);

            CrashInfo crashInfo = new CrashInfo();
            crashInfo.setPackageName(packageName);
            crashInfo.setStampTime((int) (System.currentTimeMillis() / 1000));
            crashInfo.setCrashInfo(exceptionDetail);
            crashInfo.setSimpleInfo(throwable.getMessage());

            RecordDao recordDao = new RecordDao(context);
            recordDao.insert(crashInfo);
        }
    }
}
