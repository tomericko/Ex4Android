package com.example.tomer.ex4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class TimerService extends Service {
    public static String BROADCAST_ACTION = "nana";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            public void run() {
                Intent intent = new Intent();
                intent.setAction(TimerService.BROADCAST_ACTION);
                sendBroadcast(intent);
            }
        }, 5000, 5000);

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}