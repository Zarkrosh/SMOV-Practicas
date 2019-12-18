package com.hergomsoft.yamba.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.hergomsoft.yamba.services.RefreshService;

public class NetworkReceiver extends BroadcastReceiver {

    public static final String TAG = "NetworkReceiver";

    public void onReceive(Context context, Intent intent) {
        boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if(isNetworkDown) {
            Log.d(TAG, "onReceive: NOT connected, stopping RefreshService");
            context.stopService(new Intent(context, RefreshService.class));
        } else {
            Log.d(TAG, "onReceive: connected, starting RefreshService");
            context.startService(new Intent(context, RefreshService.class));
        }
    }
}