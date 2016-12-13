package com.nata.xdroid.ui;

import android.content.Context;
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

import static com.nata.xdroid.utils.FormatUtil.formateTimer;

public class TimeCountActivity extends AppCompatActivity implements View.OnClickListener{
    //service countdown
    private Button btnServiceStart;
    private Button btnServiceStop;
    private TextView tvServiceTime;
    private TextView tvManual;
    private TextView tvTest;

    private CountDownTimerService countDownTimerService;


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
                    tvTest.setText(formateTimer(countDownTimerService.getTest_timer()));
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_count);

        tvManual = (TextView) findViewById(R.id.textView_manual);
        tvTest= (TextView) findViewById(R.id.textView_test);

        btnServiceStart = (Button) findViewById(R.id.btn_start);
        btnServiceStop = (Button) findViewById(R.id.btn_stop);
        tvServiceTime = (TextView) findViewById(R.id.tv_time);

        btnServiceStart.setOnClickListener(this);
        btnServiceStop.setOnClickListener(this);

        countDownTimerService = CountDownTimerService.getInstance(new MyCountDownLisener()
                ,getSharedPreferences("pref_mine", Context.MODE_WORLD_READABLE));

        initServiceCountDownTimerStatus();

    }



    private class MyCountDownLisener implements CountDownTimerListener {

        @Override
        public void onChange() {
            mHandler.sendEmptyMessage(2);
        }
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
        tvTest.setText(formateTimer(countDownTimerService.getTest_timer()));
    }

}