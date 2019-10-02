package com.hergomsoft.hellouser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SaludoActivity extends AppCompatActivity {

    private TextView tvHolaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saludo);

        tvHolaUsuario = (TextView) findViewById(R.id.tvHolaUsuario);

        Bundle b = getIntent().getExtras();
        tvHolaUsuario.setText(String.format(getResources().getString(R.string.holaNombre), b.getString("NOMBRE")));
    }
}
