package com.hergomsoft.yamba;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Abel Herrero Gómez (abeherr)
 * Twitter: @Abel85985400
 */
public class ProgressPersonalizado {

    private static ProgressPersonalizado progress = null;
    private Dialog mDialog;

    /**
     * Devuelve una instancia única del ProgressPersonalizado.
     * @return Instancia
     */
    public static ProgressPersonalizado getInstance() {
        if(progress == null) {
            progress = new ProgressPersonalizado();
        }

        return progress;
    }

    public void showProgress(Context context, String mensaje, boolean cancelable) {
        mDialog = new Dialog(context);

        // Diálogo sin título ni fondo
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_progress_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // ProgressBar
        ProgressBar mProgressBar = (ProgressBar) mDialog.findViewById(R.id.progress_bar);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        // TextView
        TextView mProgressText = (TextView) mDialog.findViewById(R.id.progress_text);
        mProgressText.setText(mensaje);
        mProgressText.setVisibility(View.VISIBLE);

        // Muestra el diálogo
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
