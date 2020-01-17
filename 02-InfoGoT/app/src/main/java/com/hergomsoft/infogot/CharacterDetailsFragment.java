package com.hergomsoft.infogot;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hergomsoft.infogot.components.CustomLoadingImage;
import com.hergomsoft.infogot.components.NonScrollListView;
import com.hergomsoft.infogot.db.InfoGotContract;
import com.hergomsoft.infogot.utils.ScrappingTask;

import java.io.UnsupportedEncodingException;


public class CharacterDetailsFragment extends Fragment {
    private static final String TAG = CharacterDetailsFragment.class.getSimpleName();

    private Cursor cursorCharacter;
    private int idCharacter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        idCharacter = getActivity().getIntent().getIntExtra(getResources().getString(R.string.idCharacter), -1);
        if(idCharacter == -1) {
            Log.d(TAG, "No idCharacter");
            Toast.makeText(getActivity(), "An error ocurred", Toast.LENGTH_SHORT);
            getActivity().finish();
        }

        cursorCharacter = getCharacter(idCharacter);
        if(cursorCharacter == null) {
            Log.d(TAG, "No cursor character");
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_details, container, false);

        TextView name = (TextView) view.findViewById(R.id.name);
        CustomLoadingImage characterImage = (CustomLoadingImage) view.findViewById(R.id.characterImage);
        ImageButton browse = (ImageButton) view.findViewById(R.id.browse);
        // Gender
        TableRow rowGender = (TableRow) view.findViewById(R.id.rowGender);
        TextView gender = (TextView) view.findViewById(R.id.gender);
        // Culture
        TableRow rowCulture = (TableRow) view.findViewById(R.id.rowCulture);
        TextView culture = (TextView) view.findViewById(R.id.culture);
        // Born Died
        TableRow rowBorn = (TableRow) view.findViewById(R.id.rowBorn);
        TextView born = (TextView) view.findViewById(R.id.born);
        TableRow rowDied = (TableRow) view.findViewById(R.id.rowDied);
        TextView died = (TextView) view.findViewById(R.id.died);
        // Father Mother Spouse (hidden if none)
        TableRow rowFather = (TableRow) view.findViewById(R.id.rowFather);
        TextView father = (TextView) view.findViewById(R.id.father);
        TableRow rowMother = (TableRow) view.findViewById(R.id.rowMother);
        TextView mother = (TextView) view.findViewById(R.id.mother);
        TableRow rowSpouse = (TableRow) view.findViewById(R.id.rowSpouse);
        TextView spouse = (TextView) view.findViewById(R.id.spouse);
        // Played by (hidden if none)
        TableRow rowPlayedBy = (TableRow) view.findViewById(R.id.rowPlayedBy);
        TextView playedBy = (TextView) view.findViewById(R.id.playedBy);
        // Titles (hidden if none)
        LinearLayout layoutTitles = (LinearLayout) view.findViewById(R.id.layoutTitles);
        TextView titles = (TextView) view.findViewById(R.id.titles);
        // Aliases (hidden if none)
        LinearLayout layoutAliases = (LinearLayout) view.findViewById(R.id.layoutAliases);
        TextView aliases = (TextView) view.findViewById(R.id.aliases);
        // Allegiances (hidden if none)
        LinearLayout layoutAllegiances = (LinearLayout) view.findViewById(R.id.layoutAllegiances);
        NonScrollListView allegiances = (NonScrollListView) view.findViewById(R.id.allegiances);
        // Books (hidden if none)
        LinearLayout layoutBooks = (LinearLayout) view.findViewById(R.id.layoutBooks);
        NonScrollListView books = (NonScrollListView) view.findViewById(R.id.books);
        // TV Series (hidden if none)
        LinearLayout layoutTVSeries = (LinearLayout) view.findViewById(R.id.layoutTVSeries);
        TextView tvSeries = (TextView) view.findViewById(R.id.tvSeries);

        // Gets data
        cursorCharacter.moveToFirst();
        String sName = cursorCharacter.getString(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_NAME));
        Log.d(TAG, sName);
        String sGender = cursorCharacter.getString(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_GENDER));
        String sCulture = cursorCharacter.getString(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_CULTURE));
        String sBorn = cursorCharacter.getString(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_BORN));
        String sDied = cursorCharacter.getString(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_DIED));
        final Pair<Integer, String> sFather = getCharacterPairIDName(cursorCharacter.getInt(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_FATHER)));
        final Pair<Integer, String> sMother = getCharacterPairIDName(cursorCharacter.getInt(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_MOTHER)));
        final Pair<Integer, String> sSpouse = getCharacterPairIDName(cursorCharacter.getInt(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_SPOUSE)));
        String sPlayedBy = cursorCharacter.getString(cursorCharacter.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_PLAYEDBY));
        String[] sTitles = getTitles(idCharacter);
        String[] sAliases = getAliases(idCharacter);
        Cursor cAllegiances = getAllegiances(idCharacter);
        Cursor cBooks = getBooks(idCharacter);
        String[] sTvSeries = getTVseries(idCharacter); // Throws exception!

        // Configures view from data
        name.setText(sName);
        gender.setText(sGender);
        culture.setText(sCulture);
        born.setText(sBorn);
        died.setText(sDied);
        if(sFather != null) father.setText(sFather.second);
        if(sMother != null) mother.setText(sMother.second);
        if(sSpouse != null) spouse.setText(sSpouse.second);
        playedBy.setText(sPlayedBy);
        titles.setText(joinStrings("\n", sTitles));
        aliases.setText(joinStrings("\n", sAliases));
        tvSeries.setText(joinStrings("\n", sTvSeries));

        if(sGender == null || sGender.isEmpty()) rowGender.setVisibility(View.GONE);
        if(sCulture == null || sCulture.isEmpty()) rowCulture.setVisibility(View.GONE);
        if(sBorn == null || sBorn.isEmpty()) rowBorn.setVisibility(View.GONE);
        if(sDied == null || sDied.isEmpty()) rowDied.setVisibility(View.GONE);
        if(sFather == null) rowFather.setVisibility(View.GONE);
        if(sMother == null) rowMother.setVisibility(View.GONE);
        if(sSpouse == null) rowSpouse.setVisibility(View.GONE);
        if(sPlayedBy == null || sPlayedBy.isEmpty()) rowPlayedBy.setVisibility(View.GONE);
        if(sTitles.length == 0) layoutTitles.setVisibility(View.GONE);
        if(sAliases.length == 0) layoutAliases.setVisibility(View.GONE);
        if(cAllegiances.getCount() == 0) layoutAllegiances.setVisibility(View.GONE);
        if(cBooks.getCount() == 0) layoutBooks.setVisibility(View.GONE);
        if(sTvSeries.length == 0) layoutTVSeries.setVisibility(View.GONE);

        // Underline to indicate link
        father.setPaintFlags(father.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mother.setPaintFlags(mother.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        spouse.setPaintFlags(spouse.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Setups listeners
        father.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCharacterDetails(sFather.first);
            }
        });
        mother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCharacterDetails(sMother.first);
            }
        });
        spouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCharacterDetails(sSpouse.first);
            }
        });

        Log.d(TAG, "Allegiances count: " + cAllegiances.getCount());
        String[] fromAll = new String[] { InfoGotContract.HouseEntry.COLUMN_NAME };
        int[] toAll = new int[] { android.R.id.text1 };
        final SimpleCursorAdapter adapterAllegiances = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1, null, fromAll, toAll, 0);
        adapterAllegiances.changeCursor(cAllegiances);
        allegiances.setAdapter(adapterAllegiances);
        allegiances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = adapterAllegiances.getCursor();
                cursor.moveToPosition(position);
                int clickedId = cursor.getInt(cursor.getColumnIndex(InfoGotContract.HouseEntry._ID));
                showHouseDetails(clickedId);
            }
        });

        String[] fromBoo = new String[] { InfoGotContract.BookEntry.COLUMN_NAME };
        int[] toBoo = new int[] { android.R.id.text1 };
        final SimpleCursorAdapter adapterBooks = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1, null, fromBoo, toBoo, 0);
        adapterBooks.changeCursor(cBooks);
        books.setAdapter(adapterBooks);
        books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = adapterBooks.getCursor();
                cursor.moveToPosition(position);
                int clickedId = cursor.getInt(cursor.getColumnIndex(InfoGotContract.BookEntry._ID));
                showBookDetails(clickedId);
            }
        });

        // Scraps first result image in Google Images
        final String queryImage = "site:gameofthrones.fandom.com image " + sName + " " + ((sAliases.length > 0) ? sAliases[0] : "");
        try {
            ScrappingTask scrTask = new ScrappingTask(queryImage);
            scrTask.setTargetImageView(characterImage);
            scrTask.execute();
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Scrapping of character image failed: " + e.getMessage());
        }

        // Browse on the internet if button clicked
        final String queryCharacter = sName + " character ice and fire";
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, queryCharacter); // query contains search string
                startActivity(intent);
            }
        });

        return view;
    }

    private Cursor getCharacter(int id){
        Uri uri = InfoGotContract.CharacterEntry.buildCharacterUri(id);
        String[] projection = new String[]{"*"};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private String[] getTVseries(int idc){
        Uri uri = InfoGotContract.TVseriesEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.TVseriesEntry.COLUMN_SEASON};
        String selection = InfoGotContract.TVseriesEntry.COLUMN_IDC+"= ?";
        String[] selectionArgs = new String[]{String.valueOf(idc)};
        String sortOrder = null;
        Cursor cursor = getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        return getStringArrayFromCursor(cursor);
    }

    private String[] getTitles(int idc){
        Uri uri = InfoGotContract.CharacterTitleEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.CharacterTitleEntry.COLUMN_TITLE};
        String selection = InfoGotContract.CharacterTitleEntry.COLUMN_IDC+"= ?";
        String[] selectionArgs = new String[]{String.valueOf(idc)};
        String sortOrder = null;
        Cursor cursor = getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        return getStringArrayFromCursor(cursor);
    }

    private String[] getAliases(int idc){
        Uri uri = InfoGotContract.AliasEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.AliasEntry.COLUMN_ALIAS};
        String selection = InfoGotContract.AliasEntry.COLUMN_IDC+"= ?";
        String[] selectionArgs = new String[]{String.valueOf(idc)};
        String sortOrder = null;
        Cursor cursor = getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        return getStringArrayFromCursor(cursor);
    }

    private Cursor getBooks(int idc){
        Uri uri = InfoGotContract.BookEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.BookEntry._ID, InfoGotContract.BookEntry.COLUMN_NAME};
        String selection = InfoGotContract.BookEntry._ID + " IN (SELECT A."+InfoGotContract.AppearanceEntry.COLUMN_IDB +
                " FROM "+ InfoGotContract.AppearanceEntry.TABLE_NAME+" A " +
                "WHERE A." + InfoGotContract.AppearanceEntry.COLUMN_IDC+"= ?)";
        String[] selectionArgs = new String[]{String.valueOf(idc)};
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private Cursor getAllegiances(int idc){
        Uri uri = InfoGotContract.HouseEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.HouseEntry._ID, InfoGotContract.HouseEntry.COLUMN_NAME};
        String selection = InfoGotContract.HouseEntry._ID + " IN (SELECT M."+InfoGotContract.MemberEntry.COLUMN_IDH+
                " FROM "+InfoGotContract.MemberEntry.TABLE_NAME+" M " +
                "WHERE M." +InfoGotContract.MemberEntry.COLUMN_IDC+"= ?)";
        String[] selectionArgs = new String[]{String.valueOf(idc)};
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private String[] getStringArrayFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        String[] result = new String[cursor.getCount()];
        for(int i = 0; i < result.length; i++) {
            result[i] = cursor.getString(0);
            cursor.moveToNext();
        }

        return result;
    }

    private Pair<Integer, String> getCharacterPairIDName(int idCharacter) {
        Cursor cChar = getCharacter(idCharacter);
        if(cChar.getCount() > 0) {
            cChar.moveToFirst();
            String name = cChar.getString(cChar.getColumnIndex(InfoGotContract.CharacterEntry.COLUMN_NAME));
            return new Pair<>(idCharacter, name);
        } else {
            return null;
        }
    }

    private String joinStrings(String separator, String[] strings) {
        String res = "";
        if(strings.length > 0) {
            for (String s : strings) res += s + "\n";
            res = res.substring(0, res.length() - 1);
        }
        return res;
    }

    private void showCharacterDetails(int idCharacter) {
        Intent i = new Intent(getActivity(), CharacterDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idCharacter), idCharacter);
        startActivity(i);
    }

    private void showBookDetails(int idBook) {
        Intent i = new Intent(getActivity(), BookDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idBook), idBook);
        startActivity(i);
    }

    private void showHouseDetails(int idHouse) {
        Intent i = new Intent(getActivity(), HouseDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idHouse), idHouse);
        startActivity(i);
    }
}
