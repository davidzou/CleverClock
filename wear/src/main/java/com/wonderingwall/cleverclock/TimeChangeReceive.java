package com.wonderingwall.cleverclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Observable;
import java.util.Observer;

/**
 * @author DavidZou -- wearecisco@gmail.com
 * @version 1.0.0
 * @date 2018-06-29  It's my birthday.
 */
public class TimeChangeReceive extends BroadcastReceiver {

    private Observable observable;

    public TimeChangeReceive(Observer observer) {
        observable = new Observable(){
            @Override
            public synchronized boolean hasChanged() {
                return true;
            }
        };
        observable.addObserver(observer);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            if (observable != null) {
                observable.notifyObservers();
            }
        }
    }
}
