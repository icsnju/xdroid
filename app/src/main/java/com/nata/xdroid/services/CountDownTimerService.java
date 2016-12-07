package com.nata.xdroid.services;

/**
 * Created by Calvin on 2016/12/7.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * CountDownTimer Service
 * Created by Eminem Lu on 18/8/15.
 * Email arjinmc@hotmail.com
 */
public class CountDownTimerService extends Service {

    private static final long timer_unit =1000;
    private static long mDistination_total;
    private Timer timer;
    private MyTimerTask timerTask;
    private SharedPreferences preferences;

    private static long timer_couting = 0;
    private static long manual_timer = 0;


    private int timerStatus = CountDownTimerUtil.PREPARE;

    public static CountDownTimerService countDownTimerService;

    private static CountDownTimerListener mCountDownTimerListener;

    public static CountDownTimerService getInstance(CountDownTimerListener countDownTimerListener
            ,long distination_total){
        if(countDownTimerService==null){
            countDownTimerService = new CountDownTimerService();
        }
        setCountDownTimerListener(countDownTimerListener);
        mDistination_total = distination_total;
        if(timer_couting==0) {
            timer_couting = mDistination_total;
        }
        return  countDownTimerService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences("pref_mine", Context.MODE_WORLD_READABLE);
    }

    private boolean inTestMode() {
        return preferences.getBoolean("test_mode", false);
    }


    public long getManual_timer() {
        return manual_timer;
    }

    /**
     * get countdowan time
     * @return
     */
    public long getCountingTime(){
        return timer_couting;
    }

    /**
     * get current timer status
     * @return
     */
    public int getTimerStatus(){
        return  timerStatus;
    }

    /**
     * start
     */
    public void startCountDown(){
        startTimer();
        timerStatus = CountDownTimerUtil.START;
    }

    /**
     * paust
     */
    public void pauseCountDown(){
        timer.cancel();
        timerStatus = CountDownTimerUtil.PASUSE;
    }

    /**
     * stop
     */
    public void stopCountDown(){
        if(timer!=null){
            timer.cancel();
            initTimerStatus();
            mCountDownTimerListener.onChange();
        }
    }

    public static void  setCountDownTimerListener(CountDownTimerListener countDownTimerListener){
        mCountDownTimerListener = countDownTimerListener;
    }

    /**
     * count down task
     */
    private class MyTimerTask extends TimerTask {


        @Override
        public void run() {
            timer_couting -=timer_unit;
            if(inTestMode()) {
                manual_timer += timer_unit;
            }
            mCountDownTimerListener.onChange();
            if(timer_couting==0){
                cancel();
                initTimerStatus();
            }
        }
    }

    /**
     * init timer status
     */
    private void initTimerStatus(){
        timer_couting = mDistination_total;
        timerStatus = CountDownTimerUtil.PREPARE;
    }

    /**
     * start count down
     */
    private void startTimer(){
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, timer_unit);
    }

}
