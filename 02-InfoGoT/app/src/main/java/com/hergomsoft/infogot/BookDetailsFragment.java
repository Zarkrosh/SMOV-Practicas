package com.hergomsoft.infogot;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hergomsoft.infogot.components.NonScrollListView;
import com.hergomsoft.infogot.utils.ScrappingTask;

import java.io.UnsupportedEncodingException;


public class BookDetailsFragment extends Fragment {
    private static final String TAG = BookDetailsFragment.class.getSimpleName();

    private ImageView cover;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String idBook = getActivity().getIntent().getStringExtra(getResources().getString(R.string.idBook));
        // TODO Obtener datos desde la BD

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);

        TextView name = (TextView) view.findViewById(R.id.name);
        cover = (ImageView) view.findViewById(R.id.cover);
        ImageButton browse = (ImageButton) view.findViewById(R.id.browse);
        TextView releaseDate = (TextView) view.findViewById(R.id.releaseDate);
        TextView authors = (TextView) view.findViewById(R.id.authors);
        NonScrollListView characters = (NonScrollListView) view.findViewById(R.id.characters);

        // Scraps first result image in Google Images
        String debugCharacterName = "A Knight of the Seven Kingdoms book cover";
        try {
            ScrappingTask scrTask = new ScrappingTask(debugCharacterName);
            scrTask.setTargetImageView(cover);
            scrTask.execute();
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Scrapping of book cover image failed: " + e.getMessage());
        }

        // Browse on the internet if button clicked
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO String query = bookName + " book";
                String query = "A Knight of the Seven Kingdoms book";
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query); // query contains search string
                startActivity(intent);
            }
        });

        // TODO Configurar vista a partir del modelo



        // TODO Datos desde BD
        String[] values = new String[] { "Jon Snow", "Daenerys Targaryen", "Arya Stark", "Sansa Stark"};
        ArrayAdapter<String> asw = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        characters.setAdapter(asw);
        characters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
