package com.hergomsoft.infogot;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CharacterDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ¿Ya está creada?
        if(savedInstanceState == null) {
            // Crea el fragment
            /*CharacterDetailsFragment fragment = new CharacterDetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();*/
        }

    }
}
