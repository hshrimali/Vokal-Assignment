package com.example.notifyme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Intent Received: " + intent.getAction());
        String action = intent.getAction();

        if(action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Log.d(TAG, "IF part 1");
            String smsSender = "";
            String smsBody = "";

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.d(TAG, "IF part 2");
                for(SmsMessage msg : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsBody += msg.getMessageBody();
                    smsSender = msg.getDisplayOriginatingAddress();
                }
            }
            else {
                Log.d(TAG, "ELSE part");
                Bundle bundle = intent.getExtras();
                if(bundle!=null) {
                    Object[] pdus = (Object[])bundle.get("pdus");
                    if(pdus == null) {
                        Log.e(TAG, "SMS bundle has no pdus key");
                        return;
                    }
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<pdus.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        smsBody += msgs[i].getMessageBody();
                    }
                    smsSender = msgs[0].getOriginatingAddress();
                }
            }
            showNotification(context, smsSender, smsBody);
        }
    }

    private void showNotification(Context context, String sender, String text) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent content = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(sender)
                .setContentText(text);
        mBuilder.setContentIntent(content);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, mBuilder.build());
    }
}
