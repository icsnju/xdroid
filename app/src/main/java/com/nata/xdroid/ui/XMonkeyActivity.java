package com.nata.xdroid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nata.xdroid.R;
import com.nata.xdroid.services.CountDownTimerListener;
import com.nata.xdroid.services.CountDownTimerService;
import com.nata.xdroid.services.CountDownTimerUtil;
import com.nata.xdroid.utils.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nata.xdroid.utils.FormatUtil.formateTimer;

public class XMonkeyActivity extends AppCompatActivity implements View.OnClickListener {
    //service countdown
    private Button btnServiceStart;
    private Button btnServiceStop;
    private TextView tvServiceTime;
    private TextView tvManual;
    private TextView tvTest;
    private Spinner spPackage;

    private CountDownTimerService countDownTimerService;
    SharedPreferences sp;

    String[] sut = {
            "com.fsck.k9",
            "com.eleybourn.bookcatalogue",
            "org.totschnig.myexpenses",
            "com.nloko.android.syncmypix",
            "org.wordpress.android",
            "aarddict.android",
            "org.liberty.android.fantastischmemo",
            "com.evancharlton.mileage",
            "com.hectorone.multismssender",
            "com.kvance.Nectroid",
            "com.fsck.k9",
            "com.android.keepass",
            "com.tencent.mobileqq",
            "com.borneq.heregpslocation",
            "com.nata.crashapplication",
            "tw.qtlin.mac.airunlocker"
    };


//    public static final String ZHNT_PACKAGE_NAME = "com.cvicse.zhnt";
//    public static final String LOGCAT_PACKAGE_NAME = "org.jtb.alogcat";
//    public static final String MM_PACKAGE_NAME = "com.tencent.mm";
//    public static final String MULTISMS_PACKAGE_NAME = "com.hectorone.multismssender";
//    public static final String CRASH_PACKAGE_NAME = "com.nata.crashapplication";


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                switch (countDownTimerService.getTimerStatus()) {
                    case CountDownTimerUtil.PREPARE:
                        String targetPackage = spPackage.getSelectedItem().toString();
                        if(AppUtil.isAppInstalled(this,targetPackage)) {
                            countDownTimerService.startCountDown();
                            btnServiceStart.setText(R.string.start);
                            spPackage.setEnabled(false);
                            this.startActivity(this.getPackageManager().getLaunchIntentForPackage(targetPackage));
                        } else {
                            Toast.makeText(this,"该应用还没有安装",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case CountDownTimerUtil.START:
                        countDownTimerService.pauseCountDown();
                        btnServiceStart.setText(R.string.resume);
                        break;
                    case CountDownTimerUtil.PASUSE:
                        countDownTimerService.startCountDown();
                        btnServiceStart.setText(R.string.pause);
                        break;
                }
                break;
            case R.id.btn_stop:
                btnServiceStart.setText(R.string.start);
                countDownTimerService.stopCountDown();
                spPackage.setEnabled(true);
                break;
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
        setContentView(R.layout.activity_xmonkey);

        tvManual = (TextView) findViewById(R.id.textView_manual);
        tvTest = (TextView) findViewById(R.id.textView_test);

        btnServiceStart = (Button) findViewById(R.id.btn_start);
        btnServiceStop = (Button) findViewById(R.id.btn_stop);
        tvServiceTime = (TextView) findViewById(R.id.tv_time);
        spPackage = (Spinner) findViewById(R.id.spPakcage);
        sp = this.getSharedPreferences("pref_mine", MODE_WORLD_READABLE);


        spPackage.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sut));  //生成下拉列表

        /*添加列表选择监听器*/
        spPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp.edit().putString("package", sut[position]).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btnServiceStart.setOnClickListener(this);
        btnServiceStop.setOnClickListener(this);

        countDownTimerService = CountDownTimerService.getInstance(new MyCountDownLisener()
                , getSharedPreferences("pref_mine", Context.MODE_WORLD_READABLE));

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
    private void initServiceCountDownTimerStatus() {
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
