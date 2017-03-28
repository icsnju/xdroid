package com.nata.xdroid.ui;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

/**
 * Created by Calvin on 2016/11/29.
 */

public class UserDataDialog {
    public static void show(final Context context, final String userData) {
        String packageName = context.getPackageName();
        AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle((packageName != null ? packageName : "") + " user data")
                .setMessage(userData)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Service.CLIPBOARD_SERVICE);
                        cm.setText(userData);
                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent target = new Intent(Intent.ACTION_SEND);
                        target.setType("text/plain");
                        target.putExtra(Intent.EXTRA_SUBJECT, "Send");
                        target.putExtra(Intent.EXTRA_TEXT, userData);

                        Intent shareIntent = Intent.createChooser(target, "Send");
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(shareIntent);
                    }
                })
                .create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }
}
