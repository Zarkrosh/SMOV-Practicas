package com.hergomsoft.infogot.components;


import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.hergomsoft.infogot.R;

public class SettingsDialog extends Dialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = SettingsDialog.class.getSimpleName();

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
        // TODO Update view configuration from preferences
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSave:
                boolean notificationsActivated = switchQuiz.isChecked();
                int timeout = seekBarQuizTimeout.getProgress() + MIN_SEEKBAR; // Minutes

                // TODO Save to shared preferences

                dismiss();
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
