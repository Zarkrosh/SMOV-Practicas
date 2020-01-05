package com.hergomsoft.infogot.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hergomsoft.infogot.HouseDetailsActivity;
import com.hergomsoft.infogot.R;
import com.hergomsoft.infogot.domain.House;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DoYouKnowService extends IntentService {
    private static final String TAG = DoYouKnowService.class.getSimpleName();

    private final String NOTIFICATION_CHANNEL_ID = "channel_DoYouKnow";
    private final int NOTIFICATION_ID = 101; // Unique identifier for notification

    private final String NOTIFICATION_TITLE = "Do you know";
    private final String NOTIFICATION_TEXT_BEGIN = "Which house has the words: ";

    private final int DEFAULT_DELAY = 60000; // Wait between updates (ms)
    private boolean runFlag = false;

    private final Intent anotherIntent = new Intent("com.hergomsoft.ANOTHER_QUIZ");

    // List of houses
    private ArrayList<House> houses;
    private int currentHouseIndex;

    public DoYouKnowService() { super(TAG); }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Creating " + TAG + " service");

        // Generates list of houses with any words
        houses = new ArrayList<>();
        // ArrayList<House> allHouses = // TODO Get list of all houses or in constructor
        ArrayList<House> allHouses = new ArrayList<>();
        for(House h : allHouses) {
            if(!h.getWords().isEmpty()) houses.add(h);
        }

        // Shuffles list
        Collections.shuffle(houses);
        currentHouseIndex = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runFlag = false;
        houses = null;
        Log.d(TAG, "Destroying " + TAG + " service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Notification Channel ID passed as a parameter here will be ignored for all the Android versions below 8.0
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
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

        runFlag = true;
        while(runFlag) {
            // TODO Obtain configuration from preferences

            try {
                if(houses.size() > 0) {
                    House next = houses.get(currentHouseIndex++);
                    if(currentHouseIndex >= houses.size()) {
                        currentHouseIndex = 0;
                        Collections.shuffle(houses);
                    }

                    String message = NOTIFICATION_TEXT_BEGIN + next.getWords();
                    // Multiline text
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
                    builder.setContentText(message);

                    // TODO Button another quiz
                    //PendingIntent anotherPendingIntent = PendingIntent.getBroadcast(this,0, anotherIntent, 0);
                    //builder.addAction(R.drawable.ic_got, "Yeah, ask me another", anotherPendingIntent);

                    // If user taps on notification, loads the house details
                    Intent i = new Intent(this, HouseDetailsActivity.class);
                    //i.putExtra(getResources().getString(R.string.idHouse), next.getId());
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1001, i, 0);
                    builder.setContentIntent(pendingIntent);

                    // Shows notification
                    android.app.Notification notification = builder.build();
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                    notificationManagerCompat.notify(NOTIFICATION_ID, notification);

                    Thread.sleep(DEFAULT_DELAY);
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
}
