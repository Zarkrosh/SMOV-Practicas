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


public class HouseDetailsFragment extends Fragment {
    private static final String TAG = HouseDetailsFragment.class.getSimpleName();

    private ImageView coatImage;

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
        coatImage = (ImageView) view.findViewById(R.id.coat);
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

        // Scraps coat of arms image on Google Images
        String debugHouseName = "site:gameofthrones.fandom.com House Stark of Winterfell";
        try {
            ScrappingTask scrTask = new ScrappingTask(debugHouseName);
            scrTask.setTargetImageView(coatImage);
            scrTask.execute();
        } catch (Exception e) {
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
        if (false) rowCurrentLord.setVisibility(View.GONE);
        if (false) rowOverlord.setVisibility(View.GONE);
        if (false) rowHeir.setVisibility(View.GONE);
        if (false) rowDiedOut.setVisibility(View.GONE);
        if (false) layoutTitles.setVisibility(View.GONE);
        if (false) layoutAncestralWeapons.setVisibility(View.GONE);
        if (false) layoutCadetBranches.setVisibility(View.GONE);
        if (false) layoutSwornMembers.setVisibility(View.GONE);

        // TODO Datos desde BD
        String[] cad = new String[]{"House Greystark of Wolf's Den", "House Karstark of Karhold"};
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
        String[] sw = new String[]{"Jon Snow", "Arya Stark", "Sansa Stark"};
        ArrayAdapter<String> asw = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, sw);
        swornMembers.setAdapter(asw);
        swornMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked = (String) parent.getAdapter().getItem(position);
                Toast.makeText(getActivity(), clicked, Toast.LENGTH_SHORT).show();

                //Character clicked = (Character) parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), CharacterDetailsActivity.class);
                //i.putExtra(getResources().getString(R.string.idCharacter), clicked.getId());
                startActivity(i);

            }
        });

        return view;
    }

}
