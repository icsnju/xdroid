package com.nata.xdroid.utils;

import android.app.ApplicationErrorReport;

/**
 * Created by Calvin on 2016/11/26.
 */

public class CrashUtil {
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
        err.append(info.toString());
        err.append("\n");

        return err.toString();
    }
}