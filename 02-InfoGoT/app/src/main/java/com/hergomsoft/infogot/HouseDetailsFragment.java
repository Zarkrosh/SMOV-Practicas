package com.hergomsoft.infogot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hergomsoft.infogot.components.NonScrollListView;

import java.util.concurrent.TimeoutException;


public class HouseDetailsFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO Obtener datos desde la BD

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_details, container, false);

        // Name of house
        TextView name = (TextView) view.findViewById(R.id.name);
        // Coat of house (image and description)
        ImageView coat = (ImageView) view.findViewById(R.id.coat);
        TextView coatDescription = (TextView) view.findViewById(R.id.coatDescription);
        // Words
        TextView words = (TextView) view.findViewById(R.id.words);
        // Region
        TextView region = (TextView) view.findViewById(R.id.region);
        // Current lord (shown if any)
        TableRow rowCurrentLord = (TableRow) view.findViewById(R.id.rowCurrentLord);
        TextView currentLord = (TextView) view.findViewById(R.id.currentLord);
        // Overlord (shown if any)
        TableRow rowOverlord = (TableRow) view.findViewById(R.id.rowOverlord);
        TextView overlord = (TextView) view.findViewById(R.id.overlord);
        // Heir (shown if any)
        TableRow rowHeir = (TableRow) view.findViewById(R.id.rowHeir);
        TextView heir = (TextView) view.findViewById(R.id.heir);
        // Founded and founder
        TextView founded = (TextView) view.findViewById(R.id.founded);
        TextView founder = (TextView) view.findViewById(R.id.founder);
        // Died out (shown in case)
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

        // TODO Buscar primera imagen en internet y ponerla
        // TODO Configurar vista a partir del modelo
        // TODO Onclick en fundador y overlord para mostrar detalles del personaje

        // TODO Comprobar si no hay
        if(false) rowCurrentLord.setVisibility(View.GONE);
        if(false) rowOverlord.setVisibility(View.GONE);
        if(false) rowHeir.setVisibility(View.GONE);
        if(false) rowDiedOut.setVisibility(View.GONE);
        if(false) layoutTitles.setVisibility(View.GONE);
        if(false) layoutAncestralWeapons.setVisibility(View.GONE);
        if(false) layoutCadetBranches.setVisibility(View.GONE);
        if(false) layoutSwornMembers.setVisibility(View.GONE);

        String[] cad = new String[] { "House Greystark of Wolf's Den", "House Karstark of Karhold"};
        ArrayAdapter<String> acad = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, cad);
        cadetBranches.setAdapter(acad);

        String[] sw = new String[] { "Jon Snow", "Arya Stark", "Sansa Stark"};
        ArrayAdapter<String> asw = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, sw);
        swornMembers.setAdapter(asw);

        return view;
    }

    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Charact clicked = (Charact) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), CharacterDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idCharacter), clicked.getId());
        startActivity(i);

    }
    */

}
