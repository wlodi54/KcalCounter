package com.example.sywlia.licznikkaloryczny.services;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.helpers.UnitConversion;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.example.sywlia.licznikkaloryczny.helpers.Constans.MIN_VAL_ONE_SEC;
import static com.example.sywlia.licznikkaloryczny.helpers.Constans.kcalMsgWhat;
import static com.example.sywlia.licznikkaloryczny.helpers.Constans.timerMsgWhat;


public class CounterThread extends Thread {

    private long timeInMil, timeSwapBuff, updatedTime, timeStart;

    private double met;
    private Handler myHandler;
    private DecimalFormatSymbols symbols;
    private long currentTime;
    private java.text.DecimalFormat dtime;
    private double weight;

    public CounterThread(double met, Handler myHandler, double weight) {
        this.met = met;
        this.myHandler = myHandler;
        if (weight != 0) {
            this.weight = weight;
        }
        setDecimalFormat();
    }

    public void startCounting(Boolean isFirstStart) {
        if (isFirstStart) {

            timeStart = SystemClock.uptimeMillis();
            myHandler.postDelayed(this, 0);
        } else {
            timeStart = SystemClock.uptimeMillis();
            myHandler.postDelayed(this, 0);
        }
    }

    public void pauseCounting() {
        timeSwapBuff += timeInMil;
        myHandler.removeCallbacks(this);
    }

    public void stopCounting() {
        myHandler.removeCallbacks(this);
    }

    private void setDecimalFormat() {
        symbols = new DecimalFormatSymbols(Locale.ROOT);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        dtime = new java.text.DecimalFormat("###.##", symbols);
    }

    private long timer() {
        timeInMil = SystemClock.uptimeMillis() - timeStart;
        updatedTime = timeSwapBuff + timeInMil;
        Message time = new Message();

        time.obj = String.valueOf(updatedTime);
        time.what = timerMsgWhat;
        myHandler.sendMessage(time);
        return updatedTime;
    }

    int x=0;
    @Override
    public void run() {

        this.currentTime=timer();
        liczKalorie();
        myHandler.postDelayed(this, 0);
    }

    private void liczKalorie() {

        if ((int) (currentTime/1000)!=x) {
            Message message = new Message();

            message.obj = MIN_VAL_ONE_SEC * ((3.5 * this.met * this.weight) / 200);
            message.what = kcalMsgWhat;
            myHandler.sendMessage(message);
            x = (int) currentTime / 1000;
        }
    }

    public void setMet(Double met) {
        this.met = met;
        Log.i(Constans.LOG_APP,"met in set met: "+String.valueOf(met));

    }
}
