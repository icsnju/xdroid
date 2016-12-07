package com.nata.xdroid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nata.xdroid.R;
import com.nata.xdroid.services.CountDownTimerListener;
import com.nata.xdroid.services.CountDownTimerService;
import com.nata.xdroid.services.CountDownTimerUtil;

public class TimeCountActivity extends AppCompatActivity implements View.OnClickListener{
    private int manualTime = 0;
    private int testTime = 0;

    //service countdown
    private Button btnServiceStart;
    private Button btnServiceStop;
    private TextView tvServiceTime;
    private TextView tvManual;

    private CountDownTimerService countDownTimerService;
    private long timer_unit = 1000;
    private long service_distination_total = timer_unit*30*60;


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                switch (countDownTimerService.getTimerStatus()){
                    case CountDownTimerUtil.PREPARE:
                        countDownTimerService.startCountDown();
                        btnServiceStart.setText("PAUSE");
                        break;
                    case CountDownTimerUtil.START:
                        countDownTimerService.pauseCountDown();
                        btnServiceStart.setText("RESUME");
                        break;
                    case CountDownTimerUtil.PASUSE:
                        countDownTimerService.startCountDown();
                        btnServiceStart.setText("PAUSE");
                        break;
                }
                break;
            case R.id.btn_stop:
                btnServiceStart.setText("START");
                countDownTimerService.stopCountDown();
                break;
        }
    }

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    tvServiceTime.setText(formateTimer(countDownTimerService.getCountingTime()));
                    tvManual.setText(formateTimer(countDownTimerService.getManual_timer()));
                    if(countDownTimerService.getTimerStatus()==CountDownTimerUtil.PREPARE){
                        btnServiceStart.setText("START");
                    }
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_count);

        tvManual = (TextView) findViewById(R.id.textView_manual);
        final TextView textViewTest= (TextView) findViewById(R.id.textView_test);

        btnServiceStart = (Button) findViewById(R.id.btn_start);
        btnServiceStop = (Button) findViewById(R.id.btn_stop);
        tvServiceTime = (TextView) findViewById(R.id.tv_time);

        btnServiceStart.setOnClickListener(this);
        btnServiceStop.setOnClickListener(this);

        countDownTimerService = CountDownTimerService.getInstance(new MyCountDownLisener()
                ,service_distination_total);
        initServiceCountDownTimerStatus();

    }



    private class MyCountDownLisener implements CountDownTimerListener {

        @Override
        public void onChange() {
            mHandler.sendEmptyMessage(2);
        }
    }






    /**
     * formate timer shown in textview
     * @param time
     * @return
     */
    private String formateTimer(long time){
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
    private String formateNumber(int time){
        return String.format("%02d", time);
    }

    /**
     * init countdowntimer buttons status for servce
     */
    private void initServiceCountDownTimerStatus(){
        switch (countDownTimerService.getTimerStatus()) {
            case CountDownTimerUtil.PREPARE:
                btnServiceStart.setText("START");
                break;
            case CountDownTimerUtil.START:
                btnServiceStart.setText("PAUSE");
                break;
            case CountDownTimerUtil.PASUSE:
                btnServiceStart.setText("RESUME");
                break;
        }
        tvServiceTime.setText(formateTimer(countDownTimerService.getCountingTime()));
        tvManual.setText(formateTimer(countDownTimerService.getManual_timer()));
    }

}
