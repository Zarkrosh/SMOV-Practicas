package com.hergomsoft.infogot;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hergomsoft.infogot.components.NonScrollListView;
import com.hergomsoft.infogot.db.InfoGotContract;
import com.hergomsoft.infogot.utils.ScrappingTask;

import java.io.UnsupportedEncodingException;


public class BookDetailsFragment extends Fragment {
    private static final String TAG = BookDetailsFragment.class.getSimpleName();

    private ImageView cover;

    private Cursor cursorBook;
    private int idBook;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        idBook = getActivity().getIntent().getIntExtra(getResources().getString(R.string.idBook), -1);
        if(idBook == -1) {
            Log.d(TAG, "No idBook");
            Toast.makeText(getActivity(), "An error ocurred", Toast.LENGTH_SHORT);
            getActivity().finish();
        }

        cursorBook = getBook(idBook);
        if(cursorBook == null) {
            Log.d(TAG, "No cursor book");
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);

        TextView name = (TextView) view.findViewById(R.id.name);
        cover = (ImageView) view.findViewById(R.id.cover);
        ImageButton browse = (ImageButton) view.findViewById(R.id.browse);
        TextView releaseDate = (TextView) view.findViewById(R.id.releaseDate);
        TextView authors = (TextView) view.findViewById(R.id.authors);
        TextView numberOfPages = (TextView) view.findViewById(R.id.numberOfPages);
        NonScrollListView characters = (NonScrollListView) view.findViewById(R.id.characters);

        // Gets data
        for(String s : cursorBook.getColumnNames()) Log.d(TAG, "Col " + s);
        cursorBook.moveToFirst();
        String sName = cursorBook.getString(cursorBook.getColumnIndex(InfoGotContract.BookEntry.COLUMN_NAME));
        String sReleaseDate = cursorBook.getString(cursorBook.getColumnIndex(InfoGotContract.BookEntry.COLUMN_RELEASED));
        //String sAuthors = cursorBook.getString(cursorBook.getColumnIndex(InfoGotContract.BookEntry.COLUMN_AUTHOR)); // TODO No column in cursor!
        String sNumberOfPages = cursorBook.getString(cursorBook.getColumnIndex(InfoGotContract.BookEntry.COLUMN_NPAGES));
        Cursor cCharacters = getCharacters(idBook);
        Log.d(TAG, "Chars: " + cCharacters.getCount()); // TODO Fix, only gets first character

        // Configures view from data
        name.setText(sName);
        releaseDate.setText(sReleaseDate);
        //authors.setText(sAuthors);
        numberOfPages.setText(sNumberOfPages);

        String[] fromAll = new String[] { InfoGotContract.CharacterEntry.COLUMN_NAME };
        int[] toAll = new int[] { android.R.id.text1 };
        final SimpleCursorAdapter adapterCharacters = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1, null, fromAll, toAll, 0);
        adapterCharacters.changeCursor(cCharacters);
        characters.setAdapter(adapterCharacters);
        characters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = adapterCharacters.getCursor();
                cursor.moveToPosition(position);
                int clickedId = cursor.getInt(cursor.getColumnIndex(InfoGotContract.CharacterEntry._ID));
                showCharacterDetails(clickedId);
            }
        });

        // Scraps first result image in Google Images
        final String queryCover = sName + " book cover";
        try {
            ScrappingTask scrTask = new ScrappingTask(queryCover);
            scrTask.setTargetImageView(cover);
            scrTask.execute();
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Scrapping of book cover image failed: " + e.getMessage());
        }

        // Browse on the internet if button clicked
        final String querySearch = sName + " book";
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, querySearch); // query contains search string
                startActivity(intent);
            }
        });

        return view;
    }

    private Cursor getBook(int id) {
        Uri uri = InfoGotContract.BookEntry.buildBookUri(id);
        String[] projection = new String[]{"*"};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private Cursor getCharacters(int idb){
        Uri uri = InfoGotContract.CharacterEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.CharacterEntry._ID, InfoGotContract.CharacterEntry.COLUMN_NAME};
        String selection = InfoGotContract.CharacterEntry._ID + " IN (SELECT A."+InfoGotContract.AppearanceEntry.COLUMN_IDC  +
                " FROM "+ InfoGotContract.AppearanceEntry.TABLE_NAME+" A " +
                "WHERE A." + InfoGotContract.AppearanceEntry.COLUMN_IDB+"= ?)";
        String[] selectionArgs = new String[]{String.valueOf(idb)};
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }


    private void showCharacterDetails(int idCharacter) {
        Intent i = new Intent(getActivity(), CharacterDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idCharacter), idCharacter);
        startActivity(i);
    }

}
