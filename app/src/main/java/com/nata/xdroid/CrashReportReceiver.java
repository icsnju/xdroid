package com.nata.xdroid;

/**
 * Created by Calvin on 2016/11/26.
 */

import android.app.ApplicationErrorReport;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nata.xdroid.db.CrashInfo;
import com.nata.xdroid.db.RecordDao;
import com.nata.xdroid.utils.CrashUtil;

public class CrashReportReceiver extends BroadcastReceiver {
    public static final String EXTRA_NAME_CRASH_INFO = "crash_info";
    public static final String EXTRA_NAME_PACKAGE_NAME = "pkg_name";
    public static final String ACTION_REPORT_CRASH = "com.nata.xdroid.action.REPORT_CRASH";

    public static Intent getCrashBroadCastIntent(ApplicationErrorReport.CrashInfo info, String pkgName) {
        Intent intent = new Intent(ACTION_REPORT_CRASH);

        intent.putExtra(EXTRA_NAME_CRASH_INFO, CrashUtil.getCrashInfoDetail(info));
        intent.putExtra(EXTRA_NAME_PACKAGE_NAME, pkgName);
        return intent;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ACTION_REPORT_CRASH.equals(intent.getAction())) {
            String crashInfoDetail = (String) intent.getSerializableExtra(EXTRA_NAME_CRASH_INFO);
            String packageName = (String) intent.getSerializableExtra(EXTRA_NAME_PACKAGE_NAME);

            //存到数据库
            CrashInfo crashInfo = new CrashInfo();
            crashInfo.setPackageName(packageName);
            crashInfo.setStampTime((int) (System.currentTimeMillis() / 1000));
            crashInfo.setCrashInfo(crashInfoDetail);
//            crashInfo.setSimpleInfo(throwable.getMessage());

            RecordDao recordDao = new RecordDao(context);
            recordDao.insert(crashInfo);

//            CrashDialog.show(context, crashInfo);

        }
    }
}
