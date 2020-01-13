package com.hergomsoft.infogot.components;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.hergomsoft.infogot.R;
import com.hergomsoft.infogot.services.DoYouKnowService;

public class SettingsDialog extends Dialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = SettingsDialog.class.getSimpleName();

    public static final boolean DEFAULT_ENABLED_NOTIFICATIONS = true;
    public static final int DEFAULT_QUIZ_TIMEOUT = 30;

    private final int MIN_SEEKBAR = 1;
    private final int MAX_SEEKBAR = 90 - MIN_SEEKBAR;

    private Switch switchQuiz;
    private SeekBar seekBarQuizTimeout;
    private TextView quizTimeout;
    private Button btnSave;

    private final Activity activity;

    public SettingsDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.settings);

        setCanceledOnTouchOutside(true);

        switchQuiz = (Switch) findViewById(R.id.switchQuiz);
        seekBarQuizTimeout = (SeekBar) findViewById(R.id.seekBarQuizTimeout);
        quizTimeout = (TextView) findViewById(R.id.quizTimeout);
        btnSave = (Button) findViewById(R.id.btnSave);

        seekBarQuizTimeout.setMax(MAX_SEEKBAR);

        switchQuiz.setOnCheckedChangeListener(this);
        seekBarQuizTimeout.setOnSeekBarChangeListener(this);
        btnSave.setOnClickListener(this);

        updateConfiguration();
    }

    private void enableSeekBarQuizTimeout() {
        // Enables seekbar edition and changes color to text
        seekBarQuizTimeout.setEnabled(true);
        quizTimeout.setTextColor(activity.getResources().getColor(R.color.activeTimeout));
    }

    private void disableSeekBarQuizTimeout() {
        // Disables seekbar edition and changes color to text
        seekBarQuizTimeout.setEnabled(false);
        quizTimeout.setTextColor(activity.getResources().getColor(R.color.inactiveTimeout));
    }

    private void updateConfiguration() {
        // Updates view configuration from preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean enabled = sharedPref.getBoolean(activity.getString(R.string.prefNotificationsEnabled), DEFAULT_ENABLED_NOTIFICATIONS);
        int timeout = sharedPref.getInt(activity.getString(R.string.prefQuizTimeout), DEFAULT_QUIZ_TIMEOUT);

        switchQuiz.setChecked(enabled);
        seekBarQuizTimeout.setProgress(timeout - MIN_SEEKBAR);

        if(enabled) enableSeekBarQuizTimeout();
        else disableSeekBarQuizTimeout();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSave:
                boolean notificationsEnabled = switchQuiz.isChecked();
                int timeout = seekBarQuizTimeout.getProgress() + MIN_SEEKBAR; // Minutes

                // Saves to SharedPreferences
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(activity.getString(R.string.prefNotificationsEnabled), notificationsEnabled);
                editor.putInt(activity.getString(R.string.prefQuizTimeout), timeout);
                editor.commit();

                dismiss();

                if(notificationsEnabled) {
                    // Runs quiz service
                    activity.startService(new Intent(activity, DoYouKnowService.class));
                }
                break;
            default:
                Log.d(TAG, "Unknown click event source: " + v.getId());
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case R.id.switchQuiz:
                if(isChecked) {
                    enableSeekBarQuizTimeout();
                } else {
                    disableSeekBarQuizTimeout();
                }
                break;
            default:
                Log.d(TAG, "Unknown check event source: " + buttonView.getId());
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int minutes = progress + MIN_SEEKBAR;
        quizTimeout.setText(String.format("%d minutes", minutes));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Not used
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Not used
    }

    @Override
    public void show() {
        updateConfiguration();
        super.show();
    }
}
