package com.hergomsoft.infogot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.IllegalFormatException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnBooks;
    private Button btnCharacters;
    private Button btnHouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link views
        btnBooks = (Button) findViewById(R.id.btnBooks);
        btnBooks.setOnClickListener(this);
        btnCharacters = (Button) findViewById(R.id.btnCharacters);
        btnCharacters.setOnClickListener(this);
        btnHouses = (Button) findViewById(R.id.btnHouses);
        btnHouses.setOnClickListener(this);

        // Downloads data from API if it's not already downloaded
        new DownloadTask().execute();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnBooks:
                i = new Intent(this, BooksActivity.class);
                startActivity(i);
                break;
            case R.id.btnCharacters:
                i = new Intent(this, CharactersActivity.class);
                startActivity(i);
                break;
            case R.id.btnHouses:
                i = new Intent(this, HousesActivity.class);
                startActivity(i);
                break;
        }
    }

    /**
     * Downloads data from API.
     */
    private class DownloadTask extends AsyncTask<String, Void, Boolean> {
        private final String API_BOOKS = "https://www.anapioficeandfire.com/api/books";
        private final String API_CHARACTERS = "https://www.anapioficeandfire.com/api/characters";
        private final String API_HOUSES = "https://www.anapioficeandfire.com/api/houses";
        private final String PAGINATION_HEADER = "link"; // Header with pagination data
        private final int MAX_RES = 50; // Maximum number of result per request

        @Override
        protected void onPreExecute() {
            // Shows download dialog
            CustomProgress.getInstance().showProgress(
                MainActivity.this,
                getResources().getString(R.string.downloadMessage), false);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean completed = false;

            try {
                // Books' data
                int page = 1;
                int last = -1;
                while(!completed) {
                    String response = getResponse(API_BOOKS, page);

                    JSONArray ja = new JSONArray(response);
                    Log.d(TAG, "Page " + page + " -> " + ja.length());
                    // TODO Meter en BD

                    // Looks for last page
                    if(last == -1) {
                        last = getLastPage(API_BOOKS);
                        Log.d(TAG, "Books last: " + last);
                    }

                    // Checks if download is completed
                    if(page == last) completed = true;
                    page++;
                }

                // Characters' data
                page = 1;
                last = -1;
                completed = false;
                while(!completed) {
                    String response = getResponse(API_CHARACTERS, page);

                    JSONArray ja = new JSONArray(response);
                    Log.d(TAG, "Page " + page + " -> " + ja.length());
                    // TODO Meter en BD

                    // Looks for last page
                    if(last == -1) {
                        last = getLastPage(API_CHARACTERS);
                        Log.d(TAG, "Chars last: " + last);
                    }

                    // Checks if download is completed
                    if(page == last) completed = true;
                    page++;
                }

                // Houses' data
                page = 1;
                last = -1;
                completed = false;
                while(!completed) {
                    String response = getResponse(API_HOUSES, page);

                    JSONArray ja = new JSONArray(response);
                    Log.d(TAG, "Page " + page + " -> " + ja.length());
                    // TODO Meter en BD

                    // Looks for last page
                    if(last == -1) {
                        last = getLastPage(API_HOUSES);
                        Log.d(TAG, "Houses last: " + last);
                    }

                    // Checks if download is completed
                    if(page == last) completed = true;
                    page++;
                }
            } catch (MalformedURLException e) {
                Log.d(TAG, "Malformed URL: " + e.toString());
            } catch (IOException e) {
                Log.d(TAG, "Download failed: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.d(TAG, e.getMessage());
            } catch (JSONException e) {
                Log.d(TAG, "Bad JSON: " + e.getMessage());
            } catch (Exception e) {
                Log.d(TAG, "Bad JSON: " + e.getMessage());
            } finally {
                if(!completed) return false;
            }

            // Dummy wait to admire the dialog
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {}

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Hides download dialog
            CustomProgress.getInstance().hideProgress();

            if(!result) {
                // Download failed
                Toast.makeText(MainActivity.this,
                    getResources().getString(R.string.downloadError), Toast.LENGTH_LONG).show();
            }
        }

        private String getResponse(String sUrl, int page) throws IOException {
            URL url = new URL(String.format("%s?pageSize=%d&page=%d", sUrl, MAX_RES, page));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(conn.getResponseCode() != 200) {
                throw new IllegalArgumentException("Wrong response (" + conn.getResponseCode() + ")");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer response = new StringBuffer();
            String str;
            while((str = reader.readLine())!= null){
                response.append(str);
            }

            conn.disconnect();

            return response.toString();
        }

        private int getLastPage(String sUrl) throws IOException {
            HttpURLConnection conn = (HttpURLConnection) new URL(sUrl + "?pageSize=" + MAX_RES).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(conn.getResponseCode() != 200) {
                throw new IllegalArgumentException("Wrong response (" + conn.getResponseCode() + ")");
            }

            List<String> l = conn.getHeaderFields().get(PAGINATION_HEADER);
            conn.disconnect();
            if(l == null) {
                throw new IllegalArgumentException("Null pagination header");
            }

            String[] parts = l.get(0).split(", ");
            String sURLLast = parts[parts.length-1].split(";")[0];
            sURLLast = sURLLast.substring(1, sURLLast.length() - 1);
            Uri uriLast = Uri.parse(sURLLast);
            return Integer.parseInt(uriLast.getQueryParameter("page"));
        }
    }
}
