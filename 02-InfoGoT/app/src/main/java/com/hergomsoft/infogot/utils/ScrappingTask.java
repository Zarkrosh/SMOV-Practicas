package com.hergomsoft.infogot.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Scraps first image from Google Images.
 */
public class ScrappingTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = ScrappingTask.class.getSimpleName();

    private final String GOOGLE_IMAGES_BASE = "https://www.google.com/search?&source=lnms&tbm=isch&q=";
    private static final String MARKER = "<div class=\"rg_meta notranslate\">";
    private static final String MARKER_END = "</div>";
    private static final String JSON_SRC = "ou";

    private String sUrl;
    private ImageView target;

    public ScrappingTask(String search) throws UnsupportedEncodingException {
        sUrl = GOOGLE_IMAGES_BASE + URLEncoder.encode(search, StandardCharsets.UTF_8.name());
        //Log.d(TAG, "Scrapping " + sUrl);
    }

    public void setTargetImageView(ImageView target) {
        this.target = target;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL(getURLFirstImage(sUrl));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Log.d(TAG, "An error ocurred when scrapping (" + sUrl + "): " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result != null) {
            // Download completed
            target.setImageBitmap(result);
        }
    }

    /**
     * Retrieves source of first result image.
     * @param sUrl Google images query
     * @return URL of first image
     */
    private String getURLFirstImage(String sUrl) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        try {
            url = new URL(sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // If I don't add more data, Google blocks connection
            conn.addRequestProperty ("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0");
            conn.addRequestProperty("Accept", "text/html");
            conn.addRequestProperty("Accept-Language", "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3");
            conn.addRequestProperty("Referer", "https://www.google.com/");
            conn.addRequestProperty("Host", "www.google.com");
            conn.connect();

            Log.d(TAG, "Code: " + conn.getResponseCode());
            is = conn.getInputStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            // Fills String
            StringBuffer buf = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                buf.append(line);
            }

            String sourceCode = buf.toString();
            int i = sourceCode.indexOf(MARKER);
            if(i == -1) throw new IllegalArgumentException("Google scrapping failed. Marker not found.");
            else i += MARKER.length();
            int j = sourceCode.indexOf(MARKER_END, i);
            JSONObject json = new JSONObject(sourceCode.substring(i, j));
            //Log.d(TAG, "Found: " + json.getString(JSON_SRC));
            return json.getString(JSON_SRC);
        } catch (Exception e) {
            Log.d(TAG, "An error ocurred when fetching URL of first image: " + e.getMessage());
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {}
        }

        return sUrl;
    }

}