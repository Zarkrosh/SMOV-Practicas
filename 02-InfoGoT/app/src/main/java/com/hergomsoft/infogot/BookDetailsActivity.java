package com.hergomsoft.infogot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ¿Ya está creada?
        if(savedInstanceState == null) {
            // Crea el fragment
            BookDetailsFragment fragment = new BookDetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }

    }
}
