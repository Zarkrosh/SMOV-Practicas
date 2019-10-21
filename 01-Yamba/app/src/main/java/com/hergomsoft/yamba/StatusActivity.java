package com.hergomsoft.yamba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class StatusActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StatusActivity";
    private EditText editStatus;
    private Button buttonTweet;

    private Twitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_main);

        // Enlaza views
        editStatus = (EditText) findViewById(R.id.editStatus);
        buttonTweet = (Button) findViewById(R.id.buttonTweet);
        buttonTweet.setOnClickListener(this);

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

        try {
            twitter.updateStatus(status);
            Log.d(TAG, "Enviado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Fallo en el envío");
            e.printStackTrace();
        }
    }
}
