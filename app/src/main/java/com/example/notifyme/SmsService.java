package com.example.notifyme;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SmsService extends Service {
    private SMSBroadcastReceiver broadcastReceiver = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);

        broadcastReceiver = new SMSBroadcastReceiver();
        registerReceiver(broadcastReceiver, intentFilter);

        Log.d("SmsService", "Service onCreate : broadcast receiver is registered");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            Log.d("SmsService", "Service onDestroy : broadcast receiver is destroyed");
        }
    }
}
