package com.nata.xdroid.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nata.xdroid.receivers.UserDataReceiver;

import java.util.ArrayList;
import java.util.List;

import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Calvin on 2016/11/21.
 */

public class ViewUtil {
    public static List<View> getAllChildViews(View parent, Class<?> T) {

        List<View> allchildren = new ArrayList<View>();

        if (parent instanceof ViewGroup) {

            ViewGroup vp = (ViewGroup) parent;

            for (int i = 0; i < vp.getChildCount(); i++) {

                View viewchild = vp.getChildAt(i);

                if (T.isAssignableFrom(viewchild.getClass())) {
                    allchildren.add(viewchild);
                }

                allchildren.addAll(getAllChildViews(viewchild, T));

            }

        }

        return allchildren;
    }




    public static void exploreTest(View parent) {
        if (parent instanceof ViewGroup) {

            ViewGroup vp = (ViewGroup) parent;

            for (int i = 0; i < vp.getChildCount(); i++) {

                View viewchild = vp.getChildAt(i);

                if(EditText.class.isAssignableFrom(viewchild.getClass())) {
                    ((EditText) viewchild).setText("hello, filled by xdroid");
                }

                // 长按
                if(viewchild.isLongClickable())
                    viewchild.performLongClick();

                // 点击
                if(viewchild.isClickable())
                    viewchild.performClick();


                exploreTest(viewchild);

            }

        }
    }

    public static void persistUserData(Context context, List<View> views) {
        for (int i = 0; i < views.size(); i++) {
            EditText et = (EditText) views.get(i);
            String text = et.getText().toString();
            int id = et.getId();
            if (!text.trim().equals("")) {
                String resourceName = context.getResources().getResourceName(id);
                Intent intent = UserDataReceiver.getUserDataIntent(resourceName,text);
                context.sendBroadcast(intent);
            }

        }
    }

    public static void fillUserData(Context context, List<View> views) {
        for (int i = 0; i < views.size(); i++) {
            EditText et = (EditText) views.get(i);
            int id = et.getId();
            String resourceName = context.getResources().getResourceName(id);
            String text = XDataUtils.query(resourceName);
            if (!text.trim().equals("")) {
                et.setText(text);
                log("onResume put text: " + id + " " + text);
            }
        }
    }
}
