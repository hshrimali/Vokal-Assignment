package com.example.notifyme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private SMSBroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broadcastReceiver = new SMSBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
            Log.d("permission", "permission denied to read sms");
            String[] permissions = {Manifest.permission.READ_SMS};
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST_CODE);
        }
        else {
            showSMS();
            startSmsService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startSmsService() {
        Intent backgroundService = new Intent(getApplicationContext(), SmsService.class);
        startService(backgroundService);

        Log.d("MainActivity", "Service Started");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showSMS();
                    startSmsService();
                } else {
                    Toast.makeText(this, "Permission is required for the app", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void showSMS() {
        ListView view = (ListView) findViewById(R.id.smsList);
        if (getSms() != null) {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getSms());
            view.setAdapter(adapter);
        }
        return;
    }

    public ArrayList getSms() {
        ArrayList sms = new ArrayList();

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, new String[]{"_id", "address", "date", "body"}, null, null, null);

        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            String address = cursor.getString(1);
            String date = cursor.getString(2);
            String body = cursor.getString(3);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(date));

            int day = calendar.get(Calendar.DATE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            Calendar calendar1 = Calendar.getInstance();
            int currDay = calendar1.get(Calendar.DAY_OF_MONTH);
            int currentHour = calendar1.get(Calendar.HOUR_OF_DAY);

            int diff = currDay-day;
            int hourDiff = 0;
            if(diff==0) {
                hourDiff = currentHour-hour;
                if(hourDiff==0 || hourDiff==1 || hourDiff==2 || hourDiff==3 || hourDiff==6 || hourDiff==12 || hourDiff==24) {
                        sms.add(address + "\n" + hourDiff + " hour ago\n" + body);
                }
            }
            else if(diff==1) {
                sms.add(address + "\n" + "1 day ago\n" + body);
            }
        }
        cursor.close();
        return sms;
    }
}
