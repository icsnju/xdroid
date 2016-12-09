package com.nata.xdroid;

import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.nata.xdroid.monkey.Monkey;
import com.nata.xdroid.services.CountDownTimerUtil;
import java.util.Timer;
import java.util.TimerTask;

import static com.nata.xdroid.utils.FormatUtil.formateTimer;
import static com.nata.xdroid.utils.XPreferencesUtils.inTestMode;

/**
 * Created by Calvin on 2016/12/5.
 */

public class TestRunner  extends Thread{
    private String LOG_RUNNER = "xdroid";
    private Context context;
    private Monkey monkey;
    private boolean active = false;
    private String packageName;

    // Timer
    private Timer timer;
    private MyTimerTask timerTask;
    private static long timer_couting = 0;
    private static long manual_timer = 0;
    private static long test_timer = 0;
    private static final long timer_unit =1000;
    private int timerStatus = CountDownTimerUtil.PREPARE;

    public TestRunner(Context context) {
        this.context = context;
        this.packageName = context.getPackageName();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Instrumentation instrumentation = new Instrumentation();
        PackageManager pm = context.getPackageManager();
        this.monkey = new Monkey(display, this.packageName, instrumentation, pm);

    }

    public void run() {
        startCountDown();

        while(true) {
            if(inTestMode() && active) {
                String event = monkey.nextRandomEvent();
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * count down task
     */
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timer_couting += timer_unit;
            if(inTestMode() && active) {
                test_timer += timer_unit;
            } else {
                manual_timer += timer_unit;
            }

            // 每10秒进行一次统计
            if((timer_couting / timer_unit) % 10 == 0) {
                Log.i(LOG_RUNNER,packageName +"=> " + "测试总时长:" + formateTimer(timer_couting) +
                        "人工测试:" + formateTimer(manual_timer) +
                        "自动测试:" + formateTimer(test_timer)
                );
            }

        }
    }


    public long getTest_timer() {
        return test_timer;
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
     * init timer status
     */
    private void initTimerStatus(){
        timer_couting = 0;
        manual_timer = 0;
        test_timer = 0;
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



    public void setActive(boolean active) {
        this.active = active;
    }



}

