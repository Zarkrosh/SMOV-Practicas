package com.hergomsoft.infogot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;


public class BookDetailsFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO Obtener datos desde la BD
        String[] values = new String[] { "Jon Snow", "Daenerys Targaryen", "Arya Stark", "Sansa Stark"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, values);

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        /*
        Charact clicked = (Charact) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), CharacterDetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idCharacter), clicked.getId());
        startActivity(i);
        */
    }

}
