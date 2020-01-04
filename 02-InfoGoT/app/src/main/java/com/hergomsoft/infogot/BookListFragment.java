package com.hergomsoft.infogot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;


public class BookListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO Obtener datos desde la BD
        String[] values = new String[] { "Juego de tronos","Choque de reyes","Tormenta de espadas","Festín de cuervos","Danza de dragones","Vientos de invierno","Sueño de primavera" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, values);

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        TextView title = (TextView) view.findViewById(R.id.listTitle);
        title.setText(getResources().getString(R.string.bookList));

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Book clicked = (Book) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(), BookDetailsActivity.class);
        //i.putExtra(getResources().getString(R.string.idBook), clicked.getId());
        startActivity(i);
    }

}
