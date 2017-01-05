package com.nata.xdroid.services;

/**
 * Created by Calvin on 2016/12/5.
 */


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.nata.xdroid.R;
import com.nata.xdroid.ui.FloatWindow;


/**
 * Created by MarioStudio on 2016/5/23.
 */

public class TestService extends Service implements TestInterface, View.OnClickListener {
    private FloatWindow floatWindow;
    private View menuView, floatView;
    private ImageButton btnPla;
    private SharedPreferences preferences;

    @Override
    public IBinder onBind(Intent intent) {
        return new TestBinder();
    }


    public class TestBinder extends Binder {
        public TestService getService() {
            return TestService.this;
        }
    }

    private boolean inTestMode() {
        return preferences.getBoolean("test_mode", true);
    }

    private void startTest() {
        preferences.edit().putBoolean("test_mode", true).apply();
    }

    private void stopTest() {
        preferences.edit().putBoolean("test_mode", false).apply();
    }




    @Override
    public void onCreate() {
        super.onCreate();
        initFloatWindow();

        preferences = getSharedPreferences("pref_mine", Context.MODE_WORLD_READABLE);


        if(inTestMode()) {
            btnPla.setImageResource(R.mipmap.landscape_player_btn_pause_normal);
        } else {
            btnPla.setImageResource(R.mipmap.landscape_player_btn_play_press);
        }

        show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismiss();
    }


    private void initFloatWindow() {
        floatView = LayoutInflater.from(this).inflate(R.layout.layout_float, null);
        menuView = LayoutInflater.from(this).inflate(R.layout.layout_menu, null);

        btnPla = (ImageButton) menuView.findViewById(R.id.player_play);

        btnPla.setOnClickListener(this);

        floatWindow = new FloatWindow(this);
        floatWindow.setFloatView(floatView);
        floatWindow.setPlayerView(menuView);

    }

    /**
     * 打开悬浮窗
     */
    public void show() {
        if (null != floatWindow) {
            floatWindow.show();
        }
    }

    /**
     * 关闭悬浮窗
     */
    public void dismiss() {
        if (null != floatWindow) {
            floatWindow.dismiss();
        }
    }

    @Override
    public void start() {
        startTest();
        btnPla.setImageResource(R.mipmap.landscape_player_btn_pause_normal);
    }

    @Override
    public void stop() {
        stopTest();
        btnPla.setImageResource(R.mipmap.landscape_player_btn_play_press);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.player_play:
                if (inTestMode()) {
                    stop();
                } else {
                    start();
                }
                break;
            default:
                break;
        }
    }

}

