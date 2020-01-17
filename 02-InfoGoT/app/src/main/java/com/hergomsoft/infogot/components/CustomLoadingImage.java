package com.hergomsoft.infogot.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hergomsoft.infogot.R;

public class CustomLoadingImage extends RelativeLayout {

    private ImageView image;
    private TextView textImage;
    private ProgressBar progress;

    public CustomLoadingImage(Context context) {
        this(context, null);
    }

    public CustomLoadingImage(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomLoadingImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Inflate XML
        inflate(getContext(), R.layout.loading_image, this);

        // Link views
        image = (ImageView) findViewById(R.id.loadingImage);
        textImage = (TextView) findViewById(R.id.textError);
        progress = (ProgressBar) findViewById(R.id.progress);

        // Text initially gone
        hideText();
    }

    public void setTextAndShow(String text) {
        hideProgress();
        textImage.setText(text);
        textImage.setVisibility(View.VISIBLE);
    }

    public void hideText() { textImage.setVisibility(View.GONE); }
    public void hideProgress() { progress.setVisibility(View.GONE); }

    public void setImageFromBitmap(Bitmap b) {
        image.setImageBitmap(b);
        hideProgress();
        hideText();
    }


}
