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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;

import com.hergomsoft.infogot.db.InfoGotContract;


public class CharacterListFragment extends ListFragment implements TextWatcher {
    private SimpleCursorAdapter adapter;
    private TextView noResults;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] from = new String[] { InfoGotContract.CharacterEntry.COLUMN_NAME };
        int[] to = new int[] { android.R.id.text1 };
        adapter = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1, getCharacters(""), from, to, 0);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        TextView title = (TextView) view.findViewById(R.id.listTitle);
        EditText search = (EditText) view.findViewById(R.id.inputSearch);
        noResults = (TextView) view.findViewById(R.id.noResults);

        title.setText(getResources().getString(R.string.charactersList));
        search.addTextChangedListener(this);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(position);
        int clickedId = cursor.getInt(cursor.getColumnIndex(InfoGotContract.CharacterEntry._ID));
        Intent i = new Intent(getActivity(), CharacterDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idCharacter), clickedId);
        startActivity(i);
    }

    public void filterResults(String search) {
        Cursor filtered = getCharacters(search);
        adapter.changeCursor(filtered);

        if(filtered.getCount() == 0) {
            // Shows no results message
            noResults.setVisibility(View.VISIBLE);
        } else {
            // Hides possible results message
            noResults.setVisibility(View.GONE);
        }
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

    private Cursor getCharacters(String filter){
        Uri uri = InfoGotContract.CharacterEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.CharacterEntry._ID, InfoGotContract.CharacterEntry.COLUMN_NAME};
        String selection = InfoGotContract.CharacterEntry.COLUMN_NAME + " like '%" + filter + "%'";
        String[] selectionArgs = null;
        String sortOrder = InfoGotContract.CharacterEntry.COLUMN_NAME + " ASC";
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

}
