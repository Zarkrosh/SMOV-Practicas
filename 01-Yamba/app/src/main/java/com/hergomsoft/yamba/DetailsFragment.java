package com.hergomsoft.yamba;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hergomsoft.yamba.db.StatusContract;

/**
 * @author Abel Herrero Gómez (abeherr)
 * Twitter: @Abel85985400
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";

    private final String[] projection = {
            StatusContract.Column.USER,
            StatusContract.Column.MESSAGE,
            StatusContract.Column.CREATED_AT };

    // Preferencias compartidas
    private SharedPreferences prefs;

    public DetailsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Obtiene el ID del tweet y luego toda la información
        String idTweet = getActivity().getIntent().getStringExtra(getResources().getString(R.string.idTweet));

        String selection = "id = ?";
        String[] args = { idTweet };
        Uri nUri = StatusContract.CONTENT_URI.buildUpon().appendPath(idTweet).build();
        Cursor cTweet = getActivity().getContentResolver().query(
                nUri, null, "", null, "");
        Toast.makeText(getContext(), "Cursor: " + cTweet, Toast.LENGTH_SHORT).show();

        // TODO USAR DATOS


        // Preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Enlaza views


        // Devuelve la vista personalizada
        return view;
    }

}
