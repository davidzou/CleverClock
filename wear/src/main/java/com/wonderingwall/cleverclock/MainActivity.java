package com.wonderingwall.cleverclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends WearableActivity implements Observer{
    /***/
    private final int ORANGE = Color.parseColor("#FF6D3F");

    /***/
    private ImageView mHour8, mHour4, mHour2, mHour1;
    /***/
    private ImageView mMin1, mMin2, mMin4, mMin8, mMin16, mMin32;
    /***/
    private TextView mAM, mPM, mTitle;
    /***/
    private ImageView mSecond;

    private TimeChangeReceive mTimeChangeReceive;

    private Thread mSecondThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initTitle();
        initAmPm();
        initHour();
        initMinutes();

        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTimeChangeReceive == null) {
            mTimeChangeReceive = new TimeChangeReceive(this);
        }
        initSceondThread();
        registerTimeChangeReceive(this, mTimeChangeReceive);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mTimeChangeReceive);
        if (mSecondThread != null) {
            mSecondThread.interrupt();
            mSecondThread = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeChangeReceive != null) mTimeChangeReceive = null;

    }

    private final void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mHour8 = (ImageView) findViewById(R.id.hour_8);
        mHour4 = (ImageView) findViewById(R.id.hour_4);
        mHour2 = (ImageView) findViewById(R.id.hour_2);
        mHour1 = (ImageView) findViewById(R.id.hour_1);

        mMin1 = (ImageView) findViewById(R.id.minutes_1);
        mMin2 = (ImageView) findViewById(R.id.minutes_2);
        mMin4 = (ImageView) findViewById(R.id.minutes_4);
        mMin8 = (ImageView) findViewById(R.id.minutes_8);
        mMin16 = (ImageView) findViewById(R.id.minutes_16);
        mMin32 = (ImageView) findViewById(R.id.minutes_32);

        mAM = (TextView) findViewById(R.id.day_am);
        mPM = (TextView) findViewById(R.id.day_pm);

        mSecond = (ImageView) findViewById(R.id.second);
    }

    private final void initTitle() {
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);

        mTitle.setText(hour + ":" + minute);
    }

    private final void initAmPm() {
        int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
        mAM.setTextColor(Color.GRAY);
        mPM.setTextColor(Color.GRAY);

        if (am_pm == Calendar.AM) {
            mAM.setTextColor(ORANGE);
        }
        if (am_pm == Calendar.PM) {
            mPM.setTextColor(ORANGE);
        }
    }

    private final void initHour() {
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        mHour1.setColorFilter(Color.GRAY);
        mHour2.setColorFilter(Color.GRAY);
        mHour4.setColorFilter(Color.GRAY);
        mHour8.setColorFilter(Color.GRAY);

        if ((hour & 1) == 1) {
            mHour1.setColorFilter(ORANGE);
        }
        if ((hour & 2) == 2) {
            mHour2.setColorFilter(ORANGE);
        }
        if ((hour & 4) == 4) {
            mHour4.setColorFilter(ORANGE);
        }
        if ((hour & 8) == 8) {
            mHour8.setColorFilter(ORANGE);
        }
    }

    private final void initMinutes() {
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        mMin1.setColorFilter(Color.GRAY);
        mMin2.setColorFilter(Color.GRAY);
        mMin4.setColorFilter(Color.GRAY);
        mMin8.setColorFilter(Color.GRAY);
        mMin16.setColorFilter(Color.GRAY);
        mMin32.setColorFilter(Color.GRAY);

        if ((minute & 1) == 1) {
            mMin1.setColorFilter(ORANGE);
        }
        if ((minute & 2) == 2) {
            mMin2.setColorFilter(ORANGE);
        }
        if ((minute & 4) == 4) {
            mMin4.setColorFilter(ORANGE);
        }
        if ((minute & 8) == 8) {
            mMin8.setColorFilter(ORANGE);
        }
        if ((minute & 16) == 16) {
            mMin16.setColorFilter(ORANGE);
        }
        if ((minute & 32) == 32) {
            mMin32.setColorFilter(ORANGE);
        }
    }

    private void initSceondThread() {
        mSecondThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mSecond.setColorFilter(Color.GRAY);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mSecond.setColorFilter(ORANGE);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mSecondThread.start();
    }

    /**
     * 注册时间广播监听
     * @param context
     * @param receiver
     * @see Intent#ACTION_TIME_TICK
     */
    public void registerTimeChangeReceive(Context context, BroadcastReceiver receiver) {
        if (context == null) {
            throw new RuntimeException("Context is null");
        }
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void update(Observable o, Object arg) {
        initTitle();
        initAmPm();
        initHour();
        initMinutes();
    }
}
