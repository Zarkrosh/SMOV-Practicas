package com.hergomsoft.infogot;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;

import com.hergomsoft.infogot.db.InfoGotContract;
import com.hergomsoft.infogot.domain.House;

import java.util.ArrayList;


public class HouseListFragment extends ListFragment implements TextWatcher {

    ArrayList<String> allHousesNames;
    ArrayList<String> matches;

    TextView noResults;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO Obtener datos desde la BD
        allHousesNames = new ArrayList<>();
        allHousesNames.add("Stark");
        allHousesNames.add("Lannister");
        allHousesNames.add("Arryn");

        matches = (ArrayList<String>) allHousesNames.clone();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, matches);

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        TextView title = (TextView) view.findViewById(R.id.listTitle);
        EditText search = (EditText) view.findViewById(R.id.inputSearch);
        noResults = (TextView) view.findViewById(R.id.noResults);

        title.setText(getResources().getString(R.string.housesList));
        search.addTextChangedListener(this);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //House clicked = (House) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), HouseDetailsActivity.class);
        //i.putExtra(getResources().getString(R.string.idBook), clicked.getId());
        startActivity(i);
    }

    /**
     * Searches for a substring in the result list and displays matches.
     * @param search Search substring
     */
    public void filterResults(String search) {
        String upper = search.toUpperCase();
        matches.clear();

        for(String s : allHousesNames) {
            if(s.toUpperCase().contains(upper)) matches.add(s);
        }

        if(matches.isEmpty()) {
            // Shows no results message
            noResults.setVisibility(View.VISIBLE);
        } else {
            // Hides possible results message
            noResults.setVisibility(View.GONE);
        }

        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
    }


    @Override
    public void afterTextChanged(Editable s) {
        filterResults(s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not used
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Not used
    }

    private Cursor getHouses(String filter){
        Uri uri = InfoGotContract.HouseEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.HouseEntry.COLUMN_NAME, InfoGotContract.HouseEntry._ID};
        String selection = InfoGotContract.HouseEntry.COLUMN_NAME + " like '%" + filter + "%'";
        String[] selectionArgs = null;
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }
}
