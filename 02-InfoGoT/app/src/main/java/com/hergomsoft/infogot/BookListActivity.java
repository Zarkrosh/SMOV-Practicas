package com.hergomsoft.infogot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ¿Ya está creada?
        if(savedInstanceState == null) {
            // Crea el fragment
            BookListFragment fragment = new BookListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }

    }

}
