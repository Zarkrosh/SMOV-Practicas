package com.hergomsoft.hellouser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText inputNombre;
    private Button btnHoli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNombre = (EditText) findViewById(R.id.inputNombre);
        btnHoli = (Button) findViewById(R.id.btnHoli);
    }

    public void clickBotonHoli(View view) {
        Intent intent = new Intent(MainActivity.this, SaludoActivity.class);

        Bundle b = new Bundle();
        b.putString("NOMBRE", inputNombre.getText().toString());

        intent.putExtras(b);

        startActivity(intent);
    }
}
