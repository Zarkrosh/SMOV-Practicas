package com.hergomsoft.yamba;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprobar que no se ha creado la actividad antes
        if (savedInstanceState == null) {
            // Crear el fragment
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
        
        // Mostrar mensaje de redirección (si lo hay)
        String mensaje = getIntent().getStringExtra(getResources().getString(R.string.extraMensaje));
        if (mensaje != null) {
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        }
    }
}