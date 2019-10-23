package com.hergomsoft.yamba;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class StatusActivity extends AppCompatActivity implements View.OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_main);



        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey("NViYhW9DqCzIR7td2TOVwGglE")
                .setOAuthConsumerSecret("AsqDzFGAwmFf9YFYN2ghwaTUDurBj4FRvIEbUBihqaDbvE1GdF")
                .setOAuthAccessToken("1179828768250224640-gxEUiFvyX1KWd7b377UrABBbw4woEQ")
                .setOAuthAccessTokenSecret("qyliVoOnz0aNUKQRo1Dah1xCVudrbAPGgM4ql6W5nOkB0");
        TwitterFactory factory = new TwitterFactory(builder.build());
        twitter = factory.getInstance();
    }

    @Override
    public void onClick(View v) {
        String status = editStatus.getText().toString();
        Log.d(TAG, "onClicked");

        // Realiza el envío del estado
        new StatusTask().execute(status);
    }



}