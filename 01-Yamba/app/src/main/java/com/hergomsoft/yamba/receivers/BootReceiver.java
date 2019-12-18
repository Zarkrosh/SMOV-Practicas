package com.hergomsoft.yamba.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hergomsoft.yamba.services.RefreshService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Thread.sleep(3000);
            context.startService(new Intent(context, RefreshService.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("BootReceiver", "onReceived");
    }
}
