package com.hergomsoft.infogot;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomProgress {

    private static CustomProgress progress = null;
    private Dialog mDialog;

    /**
     * Returns unique instance of the progress dialog.
     * @return Instance
     */
    public static CustomProgress getInstance() {
        if(progress == null) {
            progress = new CustomProgress();
        }

        return progress;
    }

    public void showProgress(Context context, String message, boolean cancelable) {
        mDialog = new Dialog(context);

        // Dialog without title and background
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_progress_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // ProgressBar
        ProgressBar mProgressBar = (ProgressBar) mDialog.findViewById(R.id.progress_bar);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        // TextView
        TextView mProgressText = (TextView) mDialog.findViewById(R.id.progress_text);
        mProgressText.setText(message);
        mProgressText.setVisibility(View.VISIBLE);

        // Shows dialog
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void hideProgress() {
        if(mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
