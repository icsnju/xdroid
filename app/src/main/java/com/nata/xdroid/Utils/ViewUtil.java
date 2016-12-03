package com.nata.xdroid.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

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
}
