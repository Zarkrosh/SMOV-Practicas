package com.hergomsoft.yamba;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import twitter4j.Twitter;
import twitter4j.TwitterException;


public class StatusFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "StatusFragment";

    private EditText editStatus;
    private Button buttonTweet;

    private Twitter twitter;


    public StatusFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        // Enlaza views
        editStatus = (EditText) view.findViewById(R.id.editStatus);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        buttonTweet.setOnClickListener(this);




        // Devuelve la vista personalizada
        return view;
    }

    /**
     * Actualiza el estado en Twitter de forma asíncrona.
     */
    private class StatusTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                twitter.updateStatus(params[0]);
                return "Tweet enviado correctamente";
            } catch (TwitterException e) {
                Log.e(TAG, "No hay conexión con Twitter");
                e.printStackTrace();
                return "No hay conexión con Twitter";
            } catch (Exception e) {
                Log.e(TAG, "Error inesperado");
                e.printStackTrace();
                return "Ocurrió un error inesperado";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Al finalizar la tarea muestra un snack con el mensaje
            /*
            Snackbar snack = Snackbar.make(findViewById(R.id.layoutStatus), result, Snackbar.LENGTH_SHORT);
            snack.getView().setBackgroundColor(Color.WHITE);
            TextView snack_text = snack.getView().findViewById(R.id.snackbar_text);
            snack_text.setTextColor(Color.BLACK);
            snack.show();*/
            Toast.makeText(StatusFragment.this.getActivity(), result, Toast.LENGTH_LONG).show();
        }
    }

}
