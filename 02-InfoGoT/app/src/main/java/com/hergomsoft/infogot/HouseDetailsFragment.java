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
import android.widget.ImageButton;
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


public class HouseDetailsFragment extends Fragment {
    private static final String TAG = HouseDetailsFragment.class.getSimpleName();

    private Cursor cursorHouse;
    private int idHouse;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        idHouse = getActivity().getIntent().getIntExtra(getResources().getString(R.string.idHouse), -1);
        Log.d(TAG, "" + idHouse);
        if(idHouse == -1) {
            Log.d(TAG, "No idHouse");
            Toast.makeText(getActivity(), "An error ocurred", Toast.LENGTH_SHORT);
            getActivity().finish();
        }

        cursorHouse = getHouse(idHouse);
        if(cursorHouse == null) {
            Log.d(TAG, "No cursor house");
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_details, container, false);

        // Name of house
        TextView name = (TextView) view.findViewById(R.id.name);
        // Coat of house (image and description)
        CustomLoadingImage coatImage = (CustomLoadingImage) view.findViewById(R.id.coat);
        ImageButton browse = (ImageButton) view.findViewById(R.id.browse);
        TextView coatDescription = (TextView) view.findViewById(R.id.coatDescription);
        // Words
        TableRow rowWords = (TableRow) view.findViewById(R.id.rowWords);
        TextView words = (TextView) view.findViewById(R.id.words);
        // Region
        TableRow rowRegion = (TableRow) view.findViewById(R.id.rowRegion);
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
        TableRow rowFounded = (TableRow) view.findViewById(R.id.rowFounded);
        TextView founded = (TextView) view.findViewById(R.id.founded);
        TableRow rowFounder = (TableRow) view.findViewById(R.id.rowFounder);
        TextView founder = (TextView) view.findViewById(R.id.founder);
        // Died out
        TableRow rowDiedOut = (TableRow) view.findViewById(R.id.rowDiedOut);
        TextView diedOut = (TextView) view.findViewById(R.id.diedOut);
        // Titles of house
        LinearLayout layoutTitles = (LinearLayout) view.findViewById(R.id.layoutTitles);
        TextView titles = (TextView) view.findViewById(R.id.titles);
        // Seats of house
        LinearLayout layoutSeats = (LinearLayout) view.findViewById(R.id.layoutSeats);
        TextView seats = (TextView) view.findViewById(R.id.seats);
        // Ancestral weapons of house
        LinearLayout layoutAncestralWeapons = (LinearLayout) view.findViewById(R.id.layoutAncestralWeapons);
        TextView ancestralWeapons = (TextView) view.findViewById(R.id.ancestralWeapons);
        // Cadet branches of house
        LinearLayout layoutCadetBranches = (LinearLayout) view.findViewById(R.id.layoutCadetBranches);
        NonScrollListView cadetBranches = (NonScrollListView) view.findViewById(R.id.cadetBranches);
        // Sworn members of house
        LinearLayout layoutSwornMembers = (LinearLayout) view.findViewById(R.id.layoutSwornMembers);
        NonScrollListView swornMembers = (NonScrollListView) view.findViewById(R.id.swornMembers);

        // Gets data
        cursorHouse.moveToFirst();
        String sName = cursorHouse.getString(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_NAME));
        String sCoat = cursorHouse.getString(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_COATOFARMS));
        String sWords = cursorHouse.getString(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_WORDS));
        String sRegion = cursorHouse.getString(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_REGION));
        final Pair<Integer, String> sLord = getCharacterPairIDName(cursorHouse.getInt(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_LORD)));
        final Pair<Integer, String> sOverlord = getHousePairIDName(cursorHouse.getInt(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_OVERLORD)));
        final Pair<Integer, String> sHeir = getCharacterPairIDName(cursorHouse.getInt(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_HEIR)));
        String sFounded = cursorHouse.getString(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_FOUNDED));
        final Pair<Integer, String> sFounder = getCharacterPairIDName(cursorHouse.getInt(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_FOUNDER)));
        String sDied = cursorHouse.getString(cursorHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_DIED));
        String[] sTitles = getTitles(idHouse);
        String[] sSeats = getSeats(idHouse);
        String[] sWeapons = getAncestralWeapons(idHouse);
        Cursor cCadetBranches = getBranches(idHouse);
        Cursor cSwornMembers = getMembers(idHouse);

        // Configures view from data
        name.setText(sName);
        coatDescription.setText(sCoat);
        words.setText(sWords);
        region.setText(sRegion);
        if(sLord != null) {
            currentLord.setText(sLord.second);
            currentLord.setPaintFlags(currentLord.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            currentLord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCharacterDetails(sLord.first);
                }
            });
        }
        if(sOverlord != null) {
            overlord.setText(sOverlord.second);
            overlord.setPaintFlags(overlord.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            overlord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showHouseDetails(sOverlord.first);
                }
            });
        }
        if(sHeir != null) {
            heir.setText(sHeir.second);
            heir.setPaintFlags(heir.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            heir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCharacterDetails(sHeir.first);
                }
            });
        }
        founded.setText(sFounded);
        if(sFounder != null) {
            founder.setText(sFounder.second);
            founder.setPaintFlags(founder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            founder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCharacterDetails(sFounder.first);
                }
            });
        }
        diedOut.setText(sDied);
        titles.setText(joinStrings("\n", sTitles));
        seats.setText(joinStrings("\n", sSeats));
        ancestralWeapons.setText(joinStrings("\n", sWeapons));

        // Hides void sections
        if (sWords == null || sWords.isEmpty()) rowWords.setVisibility(View.GONE);
        if (sRegion == null || sRegion.isEmpty()) rowRegion.setVisibility(View.GONE);
        if (sLord == null) rowCurrentLord.setVisibility(View.GONE);
        if (sOverlord == null) rowOverlord.setVisibility(View.GONE);
        if (sHeir == null) rowHeir.setVisibility(View.GONE);
        if (sFounded == null || sFounded.isEmpty()) rowFounded.setVisibility(View.GONE);
        if (sFounder == null) rowFounder.setVisibility(View.GONE);
        if (sDied == null || sDied.isEmpty()) rowDiedOut.setVisibility(View.GONE);
        if (sTitles.length == 0) layoutTitles.setVisibility(View.GONE);
        if (sSeats.length == 0) layoutSeats.setVisibility(View.GONE);
        if (sWeapons.length == 0) layoutAncestralWeapons.setVisibility(View.GONE);
        if (cCadetBranches.getCount() == 0) layoutCadetBranches.setVisibility(View.GONE);
        if (cSwornMembers.getCount() == 0) layoutSwornMembers.setVisibility(View.GONE);

        // Setups list of cadet branches
        String[] fromBra = new String[] { InfoGotContract.HouseEntry.COLUMN_NAME };
        int[] toBra = new int[] { android.R.id.text1 };
        final SimpleCursorAdapter adapterBranches = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1, null, fromBra, toBra, 0);
        adapterBranches.changeCursor(cCadetBranches);
        cadetBranches.setAdapter(adapterBranches);
        cadetBranches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = adapterBranches.getCursor();
                cursor.moveToPosition(position);
                int clickedId = cursor.getInt(cursor.getColumnIndex(InfoGotContract.HouseEntry._ID));
                showHouseDetails(clickedId);
            }
        });

        // Setups list of sworn members
        String[] fromMem = new String[] { InfoGotContract.CharacterEntry.COLUMN_NAME };
        int[] toMem = new int[] { android.R.id.text1 };
        final SimpleCursorAdapter adapterMembers = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1, null, fromMem, toMem, 0);
        adapterMembers.changeCursor(cSwornMembers);
        swornMembers.setAdapter(adapterMembers);
        swornMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = adapterMembers.getCursor();
                cursor.moveToPosition(position);
                int clickedId = cursor.getInt(cursor.getColumnIndex(InfoGotContract.CharacterEntry._ID));
                showCharacterDetails(clickedId);
            }
        });

        // Scraps coat of arms image on Google Images
        final String queryImage = "site:gameofthrones.fandom.com " + sName;
        try {
            ScrappingTask scrTask = new ScrappingTask(queryImage);
            scrTask.setTargetImageView(coatImage);
            scrTask.execute();
        } catch (Exception e) {
            Log.d(TAG, "Scrapping of coat failed: " + e.getMessage());
        }

        // Browse on the internet if button clicked
        final String querySearch = sName + " info";
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

    private Cursor getHouse(int id){
        Uri uri = InfoGotContract.HouseEntry.buildHouseUri(id);
        String[] projection = new String[]{"*"};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private String[] getTitles(int idh){
        Uri uri = InfoGotContract.HouseTitleEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.HouseTitleEntry.COLUMN_TITLE};
        String selection = InfoGotContract.HouseTitleEntry.COLUMN_IDH+"= ?";
        String[] selectionArgs = new String[]{String.valueOf(idh)};
        String sortOrder = null;
        Cursor cursor = getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        return getStringArrayFromCursor(cursor);
    }

    private String[] getSeats(int idh){
        Uri uri = InfoGotContract.SeatEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.SeatEntry.COLUMN_SEAT};
        String selection = InfoGotContract.SeatEntry.COLUMN_IDH+"= ?";
        String[] selectionArgs = new String[]{String.valueOf(idh)};
        String sortOrder = null;
        Cursor cursor = getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        return getStringArrayFromCursor(cursor);
    }

    private String[] getAncestralWeapons(int idh){
        Uri uri = InfoGotContract.AncestralWeaponEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.AncestralWeaponEntry.COLUMN_WEAPON};
        String selection = InfoGotContract.AncestralWeaponEntry.COLUMN_IDH+"= ?";
        String[] selectionArgs = new String[]{String.valueOf(idh)};
        String sortOrder = null;
        Cursor cursor = getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        return getStringArrayFromCursor(cursor);
    }

    private Cursor getCharacter(int id){
        Uri uri = InfoGotContract.CharacterEntry.buildCharacterUri(id);
        String[] projection = new String[]{InfoGotContract.CharacterEntry._ID,InfoGotContract.CharacterEntry.COLUMN_NAME};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private Cursor getBranches(int idh){
        Uri uri = InfoGotContract.HouseEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.HouseEntry._ID, InfoGotContract.HouseEntry.COLUMN_NAME};
        String selection = InfoGotContract.HouseEntry._ID + " IN (SELECT B."+InfoGotContract.BranchEntry.COLUMN_IDHB+
                " FROM "+InfoGotContract.BranchEntry.TABLE_NAME+" B " +
                "WHERE B." +InfoGotContract.BranchEntry.COLUMN_IDH+"= ?)";
        String[] selectionArgs = new String[]{String.valueOf(idh)};
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private Cursor getMembers(int idh){
        Uri uri = InfoGotContract.CharacterEntry.CONTENT_URI;
        String[] projection = new String[]{InfoGotContract.CharacterEntry._ID, InfoGotContract.CharacterEntry.COLUMN_NAME};
        String selection = InfoGotContract.CharacterEntry._ID + " IN (SELECT M."+InfoGotContract.MemberEntry.COLUMN_IDC+
                " FROM "+InfoGotContract.MemberEntry.TABLE_NAME+" M " +
                "WHERE M." +InfoGotContract.MemberEntry.COLUMN_IDH+"= ?)";
        String[] selectionArgs = new String[]{String.valueOf(idh)};
        String sortOrder = null;
        return getContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
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

    private Pair<Integer, String> getHousePairIDName(int idHouse) {
        Cursor cHouse = getHouse(idHouse);
        if(cHouse.getCount() > 0) {
            cHouse.moveToFirst();
            String name = cHouse.getString(cHouse.getColumnIndex(InfoGotContract.HouseEntry.COLUMN_NAME));
            return new Pair<>(idHouse, name);
        } else {
            return null;
        }
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

    private void showHouseDetails(int idHouse) {
        Intent i = new Intent(getActivity(), HouseDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idHouse), idHouse);
        startActivity(i);
    }
}
