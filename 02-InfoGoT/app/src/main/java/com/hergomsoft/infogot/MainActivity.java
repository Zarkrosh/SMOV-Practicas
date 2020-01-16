package com.hergomsoft.infogot;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hergomsoft.infogot.components.CustomProgress;
import com.hergomsoft.infogot.components.SettingsDialog;
import com.hergomsoft.infogot.db.DBHelper;
import com.hergomsoft.infogot.db.InfoGotContract;
import com.hergomsoft.infogot.services.DoYouKnowService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private SettingsDialog settingsDialog;

    private ImageButton btnSettings;
    private Button btnBooks;
    private Button btnCharacters;
    private Button btnHouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates settings dialog without showing it
        settingsDialog = new SettingsDialog(this);

        // Link views
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(this);
        btnBooks = (Button) findViewById(R.id.btnBooks);
        btnBooks.setOnClickListener(this);
        btnCharacters = (Button) findViewById(R.id.btnCharacters);
        btnCharacters.setOnClickListener(this);
        btnHouses = (Button) findViewById(R.id.btnHouses);
        btnHouses.setOnClickListener(this);

        // Downloads data from API if it's not already downloaded
        boolean downloaded = true;
        if(!downloaded) {
            // Deletes previous database
            // TODO Gestionar versionados en la API
            deleteDatabase(DBHelper.DATABASE_NAME);
            new DownloadTask().execute();
        } else {
            startService(new Intent(this, DoYouKnowService.class));
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnBooks:
                i = new Intent(this, BookListActivity.class);
                startActivity(i);
                break;
            case R.id.btnCharacters:
                i = new Intent(this, CharacterListActivity.class);
                startActivity(i);
                break;
            case R.id.btnHouses:
                i = new Intent(this, HouseListActivity.class);
                startActivity(i);
                break;
            case R.id.btnSettings:
                settingsDialog.show();
                break;
            default:
                Log.d(TAG, "Unknown click event source: " + v.getId());
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
                    //Meter en BD
                    for(int i=0;i<ja.length();i++){
                        insertBookEntry(ja.getJSONObject(i));
                    }
                    // Looks for last page
                    if(last == -1) {
                        last = getLastPage(API_BOOKS);
                        Log.d(TAG, "Books last: " + last);
                    }

                    // Checks if download is completed
                    if(page == last) completed = true;
                    page++;
                }

                Log.d(TAG,"books finished");

                // Characters' data
                page = 1;
                last = -1;
                completed = false;
                while(!completed) {
                    String response = getResponse(API_CHARACTERS, page);

                    JSONArray ja = new JSONArray(response);
                    Log.d(TAG, "Page " + page + " -> " + ja.length());
                    //Meter en BD
                    for(int i=0;i<ja.length();i++){
                        insertCharacterEntry(ja.getJSONObject(i));
                    }
                    // Looks for last page
                    if(last == -1) {
                        last = getLastPage(API_CHARACTERS);
                        Log.d(TAG, "Chars last: " + last);
                    }

                    // Checks if download is completed
                    if(page == last) completed = true;
                    page++;
                }

                Log.d(TAG,"characters finished");

                // Houses' data
                page = 1;
                last = -1;
                completed = false;
                while(!completed) {
                    String response = getResponse(API_HOUSES, page);

                    JSONArray ja = new JSONArray(response);
                    Log.d(TAG, "Page " + page + " -> " + ja.length());
                    //Meter en BD
                    for(int i=0;i<ja.length();i++){
                        insertHouseEntry(ja.getJSONObject(i));
                    }
                    // Looks for last page
                    if(last == -1) {
                        last = getLastPage(API_HOUSES);
                        Log.d(TAG, "Houses last: " + last);
                    }

                    // Checks if download is completed
                    if(page == last) completed = true;
                    page++;
                }

                Log.d(TAG,"houses finished");

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

            return true;
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

            // Starts service
            startService(new Intent(MainActivity.this, DoYouKnowService.class));
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

        /**
         * methods for inserting entries on their tables and its related tables
         */
        private void insertBookEntry(JSONObject book) throws JSONException {
            ContentValues values=new ContentValues();
            String urlID=book.getString("url");
            int id=Integer.parseInt(urlID.substring(urlID.lastIndexOf("/") + 1));
            values.put(InfoGotContract.BookEntry._ID,id);
            values.put(InfoGotContract.BookEntry.COLUMN_NAME,book.getString("name"));
            values.put(InfoGotContract.BookEntry.COLUMN_RELEASED,book.getString("released"));
            values.put(InfoGotContract.BookEntry.COLUMN_NPAGES,book.getInt("numberOfPages"));
            values.put(InfoGotContract.BookEntry.COLUMN_AUTHOR,book.getJSONArray("authors").getString(0));
            getContentResolver().insert(InfoGotContract.BookEntry.CONTENT_URI,values);

            JSONArray characters=book.getJSONArray("characters");
            for(int i=0;i<characters.length();i++){
                insertAppearanceEntry(id,characters.getString(i));
            }
            characters=book.getJSONArray("povCharacters");
            for(int i=0;i<characters.length();i++){
                insertAppearanceEntry(id,characters.getString(i));
            }
        }

        private void insertAppearanceEntry(int idb, String urlIDC) {
            if(urlIDC.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.AppearanceEntry.COLUMN_IDB,idb);
            values.put(InfoGotContract.AppearanceEntry.COLUMN_IDC,Integer.parseInt(urlIDC.substring(urlIDC.lastIndexOf("/") + 1)));
            getContentResolver().insert(InfoGotContract.AppearanceEntry.CONTENT_URI,values);
        }

        private void insertCharacterEntry(JSONObject character) throws JSONException {
            ContentValues values=new ContentValues();
            String urlID=character.getString("url");

            int id=Integer.parseInt(urlID.substring(urlID.lastIndexOf("/") + 1));
            values.put(InfoGotContract.CharacterEntry._ID,id);
            values.put(InfoGotContract.CharacterEntry.COLUMN_NAME,character.getString("name"));
            values.put(InfoGotContract.CharacterEntry.COLUMN_GENDER,character.getString("gender"));
            values.put(InfoGotContract.CharacterEntry.COLUMN_CULTURE,character.getString("culture"));
            values.put(InfoGotContract.CharacterEntry.COLUMN_BORN,character.getString("born"));
            values.put(InfoGotContract.CharacterEntry.COLUMN_DIED,character.getString("died"));
            values.put(InfoGotContract.CharacterEntry.COLUMN_PLAYEDBY,character.getJSONArray("playedBy").getString(0));
            String urlSpouse=character.getString("spouse");
            values.put(InfoGotContract.CharacterEntry.COLUMN_SPOUSE,urlSpouse.isEmpty() ? null : Integer.parseInt(urlSpouse.substring(urlSpouse.lastIndexOf("/") + 1)));
            String urlFather=character.getString("father");
            values.put(InfoGotContract.CharacterEntry.COLUMN_FATHER,urlFather.isEmpty() ? null : Integer.parseInt(urlFather.substring(urlFather.lastIndexOf("/") + 1)));
            String urlMother=character.getString("mother");
            values.put(InfoGotContract.CharacterEntry.COLUMN_MOTHER,urlMother.isEmpty() ? null : Integer.parseInt(urlMother.substring(urlMother.lastIndexOf("/") + 1)));
            getContentResolver().insert(InfoGotContract.CharacterEntry.CONTENT_URI,values);

            JSONArray characterTitles=character.getJSONArray("titles");
            for(int i=0;i<characterTitles.length();i++){
                insertCharacterTitlesEntry(id,characterTitles.getString(i));
            }
            JSONArray aliases=character.getJSONArray("aliases");
            for(int i=0;i<aliases.length();i++){
                insertAliasEntry(id,aliases.getString(i));
            }
            JSONArray allegiances=character.getJSONArray("allegiances");
            for(int i=0;i<allegiances.length();i++){
                insertMemberEntry(id,allegiances.getString(i));
            }
            JSONArray tvSeries=character.getJSONArray("tvSeries");
            for(int i=0;i<tvSeries.length();i++){
                insertTVseriesEntry(id,tvSeries.getString(i));
            }
        }

        private void insertMemberEntry(int idc, String urlIDH) {
            if(urlIDH.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.MemberEntry.COLUMN_IDC,idc);
            values.put(InfoGotContract.MemberEntry.COLUMN_IDH,Integer.parseInt(urlIDH.substring(urlIDH.lastIndexOf("/") + 1)));
            getContentResolver().insert(InfoGotContract.MemberEntry.CONTENT_URI,values);
        }

        private void insertAliasEntry(int idc, String alias) {
            if(alias.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.AliasEntry.COLUMN_IDC,idc);
            values.put(InfoGotContract.AliasEntry.COLUMN_ALIAS,alias);
            getContentResolver().insert(InfoGotContract.AliasEntry.CONTENT_URI,values);
        }

        private void insertTVseriesEntry(int idc, String season) {
            if(season.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.TVseriesEntry.COLUMN_IDC,idc);
            values.put(InfoGotContract.TVseriesEntry.COLUMN_SEASON,season);
            getContentResolver().insert(InfoGotContract.TVseriesEntry.CONTENT_URI,values);
        }

        private void insertCharacterTitlesEntry(int idc, String title) {
            if(title.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.CharacterTitleEntry.COLUMN_IDC,idc);
            values.put(InfoGotContract.CharacterTitleEntry.COLUMN_TITLE,title);
            getContentResolver().insert(InfoGotContract.CharacterTitleEntry.CONTENT_URI,values);
        }

        private void insertHouseEntry(JSONObject house) throws JSONException {
            ContentValues values=new ContentValues();
            String urlID=house.getString("url");
            int id=Integer.parseInt(urlID.substring(urlID.lastIndexOf("/") + 1));
            values.put(InfoGotContract.HouseEntry._ID,id);
            values.put(InfoGotContract.HouseEntry.COLUMN_NAME,house.getString("name"));
            values.put(InfoGotContract.HouseEntry.COLUMN_REGION,house.getString("region"));
            values.put(InfoGotContract.HouseEntry.COLUMN_WORDS,house.getString("words"));
            values.put(InfoGotContract.HouseEntry.COLUMN_COATOFARMS,house.getString("coatOfArms"));
            values.put(InfoGotContract.HouseEntry.COLUMN_DIED,house.getString("diedOut"));
            values.put(InfoGotContract.HouseEntry.COLUMN_FOUDED,house.getString("founded"));
            String urlOverlord=house.getString("overlord");
            values.put(InfoGotContract.HouseEntry.COLUMN_OVERLORD,urlOverlord.isEmpty() ? null : Integer.parseInt(urlOverlord.substring(urlOverlord.lastIndexOf("/") + 1)));
            String urlHeir=house.getString("heir");
            values.put(InfoGotContract.HouseEntry.COLUMN_HEIR,urlHeir.isEmpty() ? null : Integer.parseInt(urlHeir.substring(urlHeir.lastIndexOf("/") + 1)));
            String urlLord=house.getString("currentLord");
            values.put(InfoGotContract.HouseEntry.COLUMN_LORD,urlLord.isEmpty() ? null : Integer.parseInt(urlLord.substring(urlLord.lastIndexOf("/") + 1)));
            String urlFounder=house.getString("founder");
            values.put(InfoGotContract.HouseEntry.COLUMN_FOUNDER,urlFounder.isEmpty() ? null : Integer.parseInt(urlFounder.substring(urlFounder.lastIndexOf("/") + 1)));
            getContentResolver().insert(InfoGotContract.HouseEntry.CONTENT_URI,values);

            JSONArray HouseTitles=house.getJSONArray("titles");
            for(int i=0;i<HouseTitles.length();i++){
                insertHouseTitleEntry(id,HouseTitles.getString(i));
            }
            JSONArray seats=house.getJSONArray("seats");
            for(int i=0;i<seats.length();i++){
                insertSeatEntry(id,seats.getString(i));
            }
            JSONArray ancestralWeapons=house.getJSONArray("ancestralWeapons");
            for(int i=0;i<ancestralWeapons.length();i++){
                insertAncestralWeaponEntry(id,ancestralWeapons.getString(i));
            }
            JSONArray cadetBranches=house.getJSONArray("cadetBranches");
            for(int i=0;i<cadetBranches.length();i++){
                insertBranchEntry(id,cadetBranches.getString(i));
            }
        }

        private void insertHouseTitleEntry(int idh, String title) {
            if(title.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.HouseTitleEntry.COLUMN_IDH,idh);
            values.put(InfoGotContract.HouseTitleEntry.COLUMN_TITLE,title);
            getContentResolver().insert(InfoGotContract.HouseTitleEntry.CONTENT_URI,values);
        }

        private void insertSeatEntry(int idh, String seat) {
            if(seat.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.SeatEntry.COLUMN_IDH,idh);
            values.put(InfoGotContract.SeatEntry.COLUMN_SEAT,seat);
            getContentResolver().insert(InfoGotContract.SeatEntry.CONTENT_URI,values);
        }

        private void insertAncestralWeaponEntry(int idh, String weapon) {
            if(weapon.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.AncestralWeaponEntry.COLUMN_IDH,idh);
            values.put(InfoGotContract.AncestralWeaponEntry.COLUMN_WEAPON,weapon);
            getContentResolver().insert(InfoGotContract.AncestralWeaponEntry.CONTENT_URI,values);
        }

        private void insertBranchEntry(int idh, String urlIDHB) {
            if(urlIDHB.isEmpty())
                return;
            ContentValues values=new ContentValues();
            values.put(InfoGotContract.BranchEntry.COLUMN_IDH,idh);
            values.put(InfoGotContract.BranchEntry.COLUMN_IDHB,Integer.parseInt(urlIDHB.substring(urlIDHB.lastIndexOf("/") + 1)));
            getContentResolver().insert(InfoGotContract.BranchEntry.CONTENT_URI,values);
        }
    }
}
