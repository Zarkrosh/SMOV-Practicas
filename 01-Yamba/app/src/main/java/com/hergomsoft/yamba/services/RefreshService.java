package com.hergomsoft.yamba.services;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hergomsoft.yamba.R;
import com.hergomsoft.yamba.db.DBHelper;
import com.hergomsoft.yamba.db.StatusContract;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class RefreshService extends IntentService {

    static final String TAG = "RefreshService";

    static final int DELAY = 30000; // Medio minuto de espera entre actualizaciones
    private boolean runFlag = false;

    // Base de datos
    DBHelper dbHelper;
    SQLiteDatabase db;

    public RefreshService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runFlag = false;
        Log.d(TAG, "onDestroyed");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onStarted");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = prefs.getString(getResources().getString(R.string.keyAccessToken), "");
        String accessTokenSecret = prefs.getString(getResources().getString(R.string.keyAccessTokenSecret), "");

        runFlag = true;
        while (runFlag) {
            Log.d(TAG, "Actualizando tweets...");

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey("NViYhW9DqCzIR7td2TOVwGglE")
                        .setOAuthConsumerSecret("AsqDzFGAwmFf9YFYN2ghwaTUDurBj4FRvIEbUBihqaDbvE1GdF")
                        .setOAuthAccessToken(accessToken)
                        .setOAuthAccessTokenSecret(accessTokenSecret);
                TwitterFactory factory = new TwitterFactory(builder.build());
                Twitter twitter = factory.getInstance();

                try {
                    List<Status> timeline = twitter.getHomeTimeline();

                    // Abre la conexión con la BD
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    // Guarda los status en la BD
                    for (Status status : timeline) {
                        values.clear();
                        values.put(StatusContract.Column.ID, status.getId());
                        values.put(StatusContract.Column.USER, status.getUser().getName());
                        values.put(StatusContract.Column.MESSAGE, status.getText());
                        values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());
                        // Si hay conflicto ignora el insert
                        db.insertWithOnConflict(StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                    }

                    // Cierra la conexión
                    db.close();
                } catch (TwitterException e) {
                    Log.e(TAG, "Fallo al obtener el timeline", e);
                }

                Log.d(TAG, "Tweets actualizados");
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                runFlag = false;
            }
        }

    }

}
