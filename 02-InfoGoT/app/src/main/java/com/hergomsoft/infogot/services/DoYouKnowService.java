package com.hergomsoft.infogot.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hergomsoft.infogot.HouseDetailsActivity;
import com.hergomsoft.infogot.R;
import com.hergomsoft.infogot.components.SettingsDialog;
import com.hergomsoft.infogot.db.InfoGotContract;
import java.util.ArrayList;
import java.util.Collections;

public class DoYouKnowService extends IntentService {
    private static final String TAG = DoYouKnowService.class.getSimpleName();

    private final String QUIZ_CHANNEL_ID = "channel_DoYouKnow";
    private final String QUIZ_CHANNEL_DESCRIPTION = "Quiz of houses' words";

    private final int NOTIFICATION_ID = 101; // Unique identifier for notification

    private final String NOTIFICATION_TITLE = "Do you know?";
    private final String NOTIFICATION_TEXT_BEGIN = "Which house has the words: ";

    private boolean runFlag = false;

    private final Intent anotherIntent = new Intent("com.hergomsoft.ANOTHER_QUIZ");

    // List of houses
    private ArrayList<Pair<Integer, String>> housesAndWords;
    private int currentHouseIndex;

    public DoYouKnowService() { super(TAG); }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Creating " + TAG + " service");

        // Generates list of houses with any words
        housesAndWords = new ArrayList<>();
        Cursor c = getAllHouses();
        while(c.moveToNext()) {
            if(!c.getString(1).isEmpty()) {
                housesAndWords.add(new Pair<>(c.getInt(0), c.getString(1)));
            }
        }

        // Shuffles list
        Collections.shuffle(housesAndWords);
        currentHouseIndex = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runFlag = false;
        housesAndWords = null;
        Log.d(TAG, "Destroying " + TAG + " service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Notification Channel ID passed as a parameter here will be ignored for all the Android versions below 8.0
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), QUIZ_CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSmallIcon(R.drawable.ic_got);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_got));
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true); // Dismiss on tap
        builder.setVibrate(new long[] {
                500, // Delay
                500, // Vibrate
                500, // Sleep
                500
        });
        builder.setContentTitle(NOTIFICATION_TITLE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    QUIZ_CHANNEL_ID,
                    QUIZ_CHANNEL_DESCRIPTION,
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(QUIZ_CHANNEL_ID);
        }

        runFlag = true;
        while(runFlag) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            runFlag = preferences.getBoolean(getString(R.string.prefNotificationsEnabled), SettingsDialog.DEFAULT_ENABLED_NOTIFICATIONS);
            int timeout = preferences.getInt(getString(R.string.prefQuizTimeout), SettingsDialog.DEFAULT_QUIZ_TIMEOUT);
            Log.d(TAG, "Timeout: " + timeout);

            if(!runFlag) continue;

            try {
                if(housesAndWords.size() > 0) {
                    int id = housesAndWords.get(currentHouseIndex).first;
                    String words = housesAndWords.get(currentHouseIndex++).second;
                    Log.d(TAG, "Random: " + id + " -> " + words);
                    if(currentHouseIndex >= housesAndWords.size()) {
                        currentHouseIndex = 0;
                        Collections.shuffle(housesAndWords);
                    }

                    String message = NOTIFICATION_TEXT_BEGIN + words;
                    // Multiline text
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
                    builder.setContentText(message);

                    // TODO Button another quiz (is it possible?)
                    //PendingIntent anotherPendingIntent = PendingIntent.getBroadcast(this,0, anotherIntent, 0);
                    //builder.addAction(R.drawable.ic_got, "Yeah, ask me another", anotherPendingIntent);

                    // If user taps on notification, loads the house details
                    Intent i = new Intent(this, HouseDetailsActivity.class);
                    i.putExtra(getResources().getString(R.string.idHouse), id);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1001, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    // Shows notification
                    android.app.Notification notification = builder.build();
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                    notificationManagerCompat.notify(NOTIFICATION_ID, notification);

                    Thread.sleep(timeout * 1000 * 60); // Timeout in minutes
                } else {
                    Log.d(TAG, "No houses in list. Stopping service");
                    runFlag = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                runFlag = false;
            }

        }
    }

    private Cursor getAllHouses(){
        Uri uri = InfoGotContract.HouseEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.HouseEntry._ID, InfoGotContract.HouseEntry.COLUMN_WORDS};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        return getApplicationContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }
}
