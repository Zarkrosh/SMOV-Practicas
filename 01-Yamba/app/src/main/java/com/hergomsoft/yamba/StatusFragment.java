package com.hergomsoft.yamba;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Abel Herrero Gómez (abeherr)
 * Twitter: @Abel85985400
 */
public class StatusFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private static final String TAG = "StatusFragment";

    private static final int MAX_CHARS = 280;
    private static final int LIMIT_WARNING_CHARS = 15;

    private EditText editStatus;
    private Button buttonTweet;

    // Contador de caracteres
    private TextView textContador;

    private Twitter twitter;


    public StatusFragment() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey("NViYhW9DqCzIR7td2TOVwGglE")
                .setOAuthConsumerSecret("AsqDzFGAwmFf9YFYN2ghwaTUDurBj4FRvIEbUBihqaDbvE1GdF")
                .setOAuthAccessToken("1179828768250224640-gxEUiFvyX1KWd7b377UrABBbw4woEQ")
                .setOAuthAccessTokenSecret("qyliVoOnz0aNUKQRo1Dah1xCVudrbAPGgM4ql6W5nOkB0");
        TwitterFactory factory = new TwitterFactory(builder.build());
        twitter = factory.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        // Enlaza views
        editStatus = (EditText) view.findViewById(R.id.editStatus);
        editStatus.addTextChangedListener(this);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        buttonTweet.setOnClickListener(this);
        textContador = (TextView) view.findViewById(R.id.textContador);
        textContador.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_CHARS)}); // Limite de caracteres
        actualizaContadorCaracteres(editStatus.getText().toString());

        // Devuelve la vista personalizada
        return view;
    }

    @Override
    public void onClick(View v) {
        updateStatus();
    }

    /**
     * Obtiene el estado escrito y lo envía de forma asíncrona.
     */
    private void updateStatus() {
        String status = editStatus.getText().toString();
        Log.d(TAG, "onClicked");

        // Realiza el envío del estado
        new StatusTask().execute(status);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No se usa
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // No se usa
    }

    @Override
    public void afterTextChanged(Editable statusText) {
        actualizaContadorCaracteres(statusText.toString());
    }

    private void actualizaContadorCaracteres(String texto) {
        int count = MAX_CHARS - texto.length();
        textContador.setText(Integer.toString(count));
        if(count == 0) {
            textContador.setTextColor(getResources().getColor(R.color.colorContadorLleno));
        } else if(count < LIMIT_WARNING_CHARS) {
            textContador.setTextColor(getResources().getColor(R.color.colorContadorAviso));
        } else {
            textContador.setTextColor(getResources().getColor(R.color.colorContadorBien));
        }
    }

    /**
     * Actualiza el estado en Twitter de forma asíncrona.
     */
    private class StatusTask extends AsyncTask<String, Void, Integer> {
        // Resultados
        private final int RESULTADO_CORRECTO = 0;
        private final int RESULTADO_SIN_CONEXION = 1;
        private final int RESULTADO_RATE_LIMIT = 2;
        private final int RESULTADO_RECURSO_NO_ENCONTRADO = 3;
        private final int RESULTADO_DESCONOCIDO = 4;
        private final int RESULTADO_INESPERADO = 5;

        @Override
        protected void onPreExecute() {
            // Muestra el diálogo de envío del tweet
            ProgressPersonalizado.getInstance().showProgress(
                    StatusFragment.this.getContext(),
                    getResources().getString(R.string.enviando_tweet), false);
        }

        @Override
        protected Integer doInBackground(String... params) {
            // Envía el estado a Twitter de forma asíncrona y devuelve un resultado
            try {
                // Primero espera 1 segundo, para que se vea el diálogo de progreso
                SystemClock.sleep(1000);
                // Envía el estado a Twitter
                twitter.updateStatus(params[0]);
                return RESULTADO_CORRECTO;
            } catch (TwitterException e) {
                e.printStackTrace();
                if(e.isCausedByNetworkIssue()) {
                    Log.e(TAG, "No hay conexión con Twitter");
                    return RESULTADO_SIN_CONEXION;
                } else if(e.exceededRateLimitation()) {
                    Log.e(TAG, "Se ha excedido el límite temporal de tweets");
                    return RESULTADO_RATE_LIMIT;
                } else if(e.resourceNotFound()) {
                    Log.e(TAG, "No se ha encontrado el recurso requerido");
                    return RESULTADO_RECURSO_NO_ENCONTRADO;
                } else {
                    // Excepción desconocida
                    Log.e(TAG, "Excepción de Twitter desconocida");
                    return RESULTADO_DESCONOCIDO;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error inesperado");
                e.printStackTrace();
                return RESULTADO_INESPERADO;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Oculta el diálogo de envío del tweet
            ProgressPersonalizado.getInstance().hideProgress();

            // Al finalizar la tarea muestra un snack con el mensaje correspondiente
            Snackbar snack = Snackbar.make(StatusFragment.this.getActivity().findViewById(R.id.layoutStatus), "", Snackbar.LENGTH_LONG);
            snack.getView().setBackgroundColor(Color.WHITE);
            TextView snack_text = snack.getView().findViewById(R.id.snackbar_text);
            switch (result) {
                case RESULTADO_CORRECTO:
                    snack.setText(getResources().getString(R.string.resultado_correcto));
                    snack_text.setTextColor(Color.BLACK);
                    // Borra el texto del tweet actual para por si quiere enviar otro
                    editStatus.setText("");
                    break;
                case RESULTADO_SIN_CONEXION:
                    snack.setText(getResources().getString(R.string.resultado_sin_conexion));
                    snack_text.setTextColor(Color.RED);
                    // Añade un botón de acción para reenviar el tweet
                    snack.setAction(R.string.reintentar_tweet, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Intenta reenviar el tweet
                            updateStatus();
                        }
                    });
                    break;
                case RESULTADO_RATE_LIMIT:
                    snack.setText(getResources().getString(R.string.resultado_limite_excedido));
                    snack_text.setTextColor(Color.RED);
                    break;
                case RESULTADO_RECURSO_NO_ENCONTRADO:
                    snack.setText(getResources().getString(R.string.resultado_recurso_no_encontrado));
                    snack_text.setTextColor(Color.RED);
                    break;
                case RESULTADO_DESCONOCIDO:
                    snack.setText(getResources().getString(R.string.resultado_desconocido));
                    snack_text.setTextColor(Color.RED);
                    break;
                case RESULTADO_INESPERADO:
                default:
                    snack.setText(getResources().getString(R.string.resultado_inesperado));
                    snack_text.setTextColor(Color.RED);
                    break;
            }

            // Muestra el snackbar
            snack.show();
        }
    }

}
