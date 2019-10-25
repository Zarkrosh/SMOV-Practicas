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


public class StatusActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ¿Ya está creada?
        if(savedInstanceState == null) {
            // Crea el fragment
            StatusFragment fragment = new StatusFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }

    }

}