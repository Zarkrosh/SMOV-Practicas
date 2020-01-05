package com.hergomsoft.infogot;

import android.content.Intent;
import android.graphics.Paint;
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
import com.hergomsoft.infogot.utils.ScrappingTask;

import java.io.UnsupportedEncodingException;


public class CharacterDetailsFragment extends Fragment {
    private static final String TAG = CharacterDetailsFragment.class.getSimpleName();

    private ImageView characterImage;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String idCharacter = getActivity().getIntent().getStringExtra(getResources().getString(R.string.idCharacter));
        // TODO Obtener datos desde la BD

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_details, container, false);

        TextView name = (TextView) view.findViewById(R.id.name);
        characterImage = (ImageView) view.findViewById(R.id.characterImage);
        TextView gender = (TextView) view.findViewById(R.id.gender);
        TextView culture = (TextView) view.findViewById(R.id.culture);
        // Born Died (hidden if not)
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

        // Scraps first result image in Google Images
        String debugCharacterName = "site:gameofthrones.fandom.com Jon Snow";
        try {
            ScrappingTask scrTask = new ScrappingTask(debugCharacterName);
            scrTask.setTargetImageView(characterImage);
            scrTask.execute();
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Scrapping of character image failed: " + e.getMessage());
        }


        // TODO Configurar vista a partir del modelo


        // TODO Onclick para mostrar detalles
        // Underline to indicate link
        father.setPaintFlags(father.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mother.setPaintFlags(mother.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        spouse.setPaintFlags(spouse.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //playedBy.setPaintFlags(playedBy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        father.setPaintFlags(father.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        // TODO Comprobar si no hay
        if(false) rowDied.setVisibility(View.GONE);
        if(false) rowFather.setVisibility(View.GONE);
        if(false) rowMother.setVisibility(View.GONE);
        if(false) rowSpouse.setVisibility(View.GONE);
        if(false) rowPlayedBy.setVisibility(View.GONE);
        if(false) layoutTitles.setVisibility(View.GONE);
        if(false) layoutAliases.setVisibility(View.GONE);
        if(false) layoutAllegiances.setVisibility(View.GONE);
        if(false) layoutBooks.setVisibility(View.GONE);
        if(false) layoutTVSeries.setVisibility(View.GONE);


        // TODO Datos desde BD
        String[] all = new String[] { "House Stark of Winterfell"};
        ArrayAdapter<String> asw = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, all);
        allegiances.setAdapter(asw);
        allegiances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked = (String) parent.getAdapter().getItem(position);
                Toast.makeText(getActivity(), clicked, Toast.LENGTH_SHORT).show();

                //House clicked = (House) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), HouseDetailsActivity.class);
                //i.putExtra(getResources().getString(R.string.idHouse), clicked.getId());
                startActivity(i);
            }
        });

        // TODO Datos desde BD
        String[] boo = new String[] { "A Game of Thrones", "A Clash of Kings"};
        ArrayAdapter<String> aboo = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, boo);
        books.setAdapter(aboo);
        books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked = (String) parent.getAdapter().getItem(position);
                Toast.makeText(getActivity(), clicked, Toast.LENGTH_SHORT).show();

                //House clicked = (House) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), BookDetailsActivity.class);
                //i.putExtra(getResources().getString(R.string.idHouse), clicked.getId());
                startActivity(i);
            }
        });


        return view;
    }

}
