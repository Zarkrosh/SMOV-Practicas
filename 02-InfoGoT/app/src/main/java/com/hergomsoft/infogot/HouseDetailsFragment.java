package com.hergomsoft.infogot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hergomsoft.infogot.components.NonScrollListView;
import com.hergomsoft.infogot.domain.Character;
import com.hergomsoft.infogot.domain.House;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class HouseDetailsFragment extends Fragment {
    private static final String TAG = HouseDetailsFragment.class.getSimpleName();

    private final String GOOGLE_IMAGES_BASE = "https://www.google.es/search?tbm=isch&q=site%3Agameofthrones.fandom.com+";

    private ImageView coat;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String idHouse = getActivity().getIntent().getStringExtra(getResources().getString(R.string.idHouse));
        // TODO Obtener datos desde la BD

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_details, container, false);

        // Name of house
        TextView name = (TextView) view.findViewById(R.id.name);
        // Coat of house (image and description)
        coat = (ImageView) view.findViewById(R.id.coat);
        TextView coatDescription = (TextView) view.findViewById(R.id.coatDescription);
        // Words
        TextView words = (TextView) view.findViewById(R.id.words);
        // Region
        TextView region = (TextView) view.findViewById(R.id.region);
        // Current lord (hidden if none)
        TableRow rowCurrentLord = (TableRow) view.findViewById(R.id.rowCurrentLord);
        TextView currentLord = (TextView) view.findViewById(R.id.currentLord);
        // Overlord (hidden if none)
        TableRow rowOverlord = (TableRow) view.findViewById(R.id.rowOverlord);
        TextView overlord = (TextView) view.findViewById(R.id.overlord);
        // Heir (hidden if none)
        TableRow rowHeir = (TableRow) view.findViewById(R.id.rowHeir);
        TextView heir = (TextView) view.findViewById(R.id.heir);
        // Founded and founder
        TextView founded = (TextView) view.findViewById(R.id.founded);
        TextView founder = (TextView) view.findViewById(R.id.founder);
        // Died out (hidden if none)
        TableRow rowDiedOut = (TableRow) view.findViewById(R.id.rowDiedOut);
        TextView diedOut = (TextView) view.findViewById(R.id.diedOut);
        // Titles of house
        LinearLayout layoutTitles = (LinearLayout) view.findViewById(R.id.layoutTitles);
        TextView titles = (TextView) view.findViewById(R.id.titles);
        // Ancestral weapons of house
        LinearLayout layoutAncestralWeapons = (LinearLayout) view.findViewById(R.id.layoutAncestralWeapons);
        TextView ancestralWeapons = (TextView) view.findViewById(R.id.ancestralWeapons);
        // Cadet branches of house
        LinearLayout layoutCadetBranches = (LinearLayout) view.findViewById(R.id.layoutCadetBranches);
        NonScrollListView cadetBranches = (NonScrollListView) view.findViewById(R.id.cadetBranches);
        // Sworn members of house
        LinearLayout layoutSwornMembers = (LinearLayout) view.findViewById(R.id.layoutSwornMembers);
        NonScrollListView swornMembers = (NonScrollListView) view.findViewById(R.id.swornMembers);

        // Scraps first result image in Google Images
        String debugHouseName = "House Stark of Winterfell";
        try {
            String url = GOOGLE_IMAGES_BASE + URLEncoder.encode(debugHouseName, StandardCharsets.UTF_8.name());
            Log.d(TAG, "Scrapping " + url);
            new ScrappingTask().execute(url);
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Scrapping of coat failed: " + e.getMessage());
        }


        // TODO Configurar vista a partir del modelo


        // TODO Onclick para mostrar detalles
        // Underline to indicate link
        currentLord.setPaintFlags(currentLord.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        overlord.setPaintFlags(overlord.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        heir.setPaintFlags(heir.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        founder.setPaintFlags(founder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // TODO Comprobar si no hay
        if(false) rowCurrentLord.setVisibility(View.GONE);
        if(false) rowOverlord.setVisibility(View.GONE);
        if(false) rowHeir.setVisibility(View.GONE);
        if(false) rowDiedOut.setVisibility(View.GONE);
        if(false) layoutTitles.setVisibility(View.GONE);
        if(false) layoutAncestralWeapons.setVisibility(View.GONE);
        if(false) layoutCadetBranches.setVisibility(View.GONE);
        if(false) layoutSwornMembers.setVisibility(View.GONE);

        // TODO Datos desde BD
        String[] cad = new String[] { "House Greystark of Wolf's Den", "House Karstark of Karhold"};
        ArrayAdapter<String> acad = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, cad);
        cadetBranches.setAdapter(acad);
        cadetBranches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked = (String) parent.getAdapter().getItem(position);
                Toast.makeText(getActivity(), clicked, Toast.LENGTH_SHORT).show();
                /*
                House clicked = (House) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), BookDetailsActivity.class);
                i.putExtra(getResources().getString(R.string.idHouse), clicked.getId());
                startActivity(i);
                 */
            }
        });

        // TODO Datos desde BD
        String[] sw = new String[] { "Jon Snow", "Arya Stark", "Sansa Stark"};
        ArrayAdapter<String> asw = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, sw);
        swornMembers.setAdapter(asw);
        swornMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked = (String) parent.getAdapter().getItem(position);
                Toast.makeText(getActivity(), clicked, Toast.LENGTH_SHORT).show();
                /*
                Character clicked = (Character) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), CharacterDetailsActivity.class);
                i.putExtra(getResources().getString(R.string.idCharacter), clicked.getId());
                 */

            }
        });

        return view;
    }

    /**
     * Scraps first image from Google Image
     */
    private class ScrappingTask extends AsyncTask<String, Void, Bitmap> {
        private static final String MARKER = "<div class=\"rg_meta notranslate\">";
        private static final String MARKER_END = "</div>";
        private static final String JSON_SRC = "ou";

        @Override
        protected void onPreExecute() {
            // Shows download dialog?
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String sUrl = strings[0];

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
                coat.setImageBitmap(result);
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
                URLConnection conn = url.openConnection();
                // If I don't add this Google returns a 403 response code
                conn.addRequestProperty ("User-Agent", "Mozilla / 5.0 (Windows NT 6.1; WOW64) AppleWebKit / 537.36 (KHTML, like Gecko) Chrome / 40.0.2214.91 Safari / 537.36");
                conn.connect();

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
                return json.getString(JSON_SRC);
            } catch (Exception e) {
                Log.d(TAG, "An error ocurred when fetching URL of first image (" + sUrl + "): " + e.getMessage());
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException ioe) {}
            }

            return sUrl;
        }

    }

}
