package com.nata.xdroid.utils;

import android.app.ApplicationErrorReport;

import com.nata.xdroid.db.beans.UserData;

import java.util.List;

import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Calvin on 2016/11/26.
 */

public class FormatUtil {

    /**
     * formate timer shown in textview
     * @param time
     * @return
     */
    public static String formateTimer(long time){
        String str = "00:00:00";
        int hour = 0;
        if(time>=1000*3600){
            hour = (int)(time/(1000*3600));
            time -= hour*1000*3600;
        }
        int minute = 0;
        if(time>=1000*60){
            minute = (int)(time/(1000*60));
            time -= minute*1000*60;
        }
        int sec = (int)(time/1000);
        str = formateNumber(hour)+":"+formateNumber(minute)+":"+formateNumber(sec);
        return str;
    }

    /**
     * formate time number with two numbers auto add 0
     * @param time
     * @return
     */
    public static String formateNumber(int time){
        return String.format("%02d", time);
    }



    public static String getExceptionDetail(Throwable t) {
        if (t == null) return "";

        StringBuilder err = new StringBuilder();
        err.append(t.toString());
        err.append("\n");

        StackTraceElement[] stack = t.getStackTrace();
        if (stack != null) {
            for (StackTraceElement aStack : stack) {
                err.append("\tat ");
                err.append(aStack.toString());
                err.append("\n");
            }

        }
        Throwable cause = t.getCause();
        if (cause != null) {
            err.append("Caused by: ");
            String causeString = getExceptionDetail(cause);
            err.append(causeString);
        }
        return err.toString();
    }

    public static String getCrashInfoDetail(ApplicationErrorReport.CrashInfo info){
        if(info == null) return "";

        StringBuilder err = new StringBuilder();
        err.append(info.throwClassName);
        err.append("\n");
        err.append(info.exceptionClassName);
        err.append("\n");
        err.append(info.exceptionMessage);
        err.append("\n");
        err.append(info.stackTrace);
        err.append("\n");

        return err.toString();
    }

    public static String getUserDataInfo(List<UserData> userDatas) {
        StringBuilder sb =new StringBuilder();
        for(int i = 0 ; i < userDatas.size(); i++) {
            UserData userData = userDatas.get(i);
            sb.append(userData.getResourceName() + " " + userData.getContent() + "\n");
        }
        return sb.toString();
    }
}
