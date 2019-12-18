package com.hergomsoft.yamba.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String TAG = "NotificationReceiver";
    private static final boolean ACTIVO = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String receiveNumbers = intent.getStringExtra("count");
        if(ACTIVO) {
            Toast.makeText(context, "Se han recibido " + receiveNumbers + "notificaciones", Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "onReceived (" + receiveNumbers + ")");
    }
}