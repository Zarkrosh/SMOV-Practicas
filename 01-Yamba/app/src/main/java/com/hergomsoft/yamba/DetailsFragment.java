package com.hergomsoft.yamba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hergomsoft.yamba.db.StatusContract;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author Abel Herrero Gómez (abeherr)
 * Twitter: @Abel85985400
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";

    // Cursor tweet
    private Cursor cTweet;
    int iColID;
    int iColNombre;
    int iColNombrePantalla;
    int iColMensaje;
    int iColEsFavorito;
    int iColEsRetweet;
    int iColNumFavs;
    int iColNumRets;

    // Elementos de la vista
    private TextView nombreUsuario;
    private TextView nombrePantallaUsuario;
    private TextView textoTweet;
    private TextView numFavoritos;
    private TextView numRetweets;
    private ImageButton botonRetweet;
    private ImageButton botonFavorito;
    private ImageButton botonCompartir;
    private TextView textoRespuesta;
    private Button botonEnviarRespuesta;

    // Variables
    private boolean favorited;
    private boolean retweeted;

    private Twitter twitter;

    // Preferencias compartidas
    private SharedPreferences prefs;

    // Parámetros AsyncTask
    private final int ACTION_FAVORITE = 0;
    private final int ACTION_NO_FAVORITE = 1;
    private final int ACTION_RETWEET = 2;
    private final int ACTION_NO_RETWEET = 3;
    private final int ACTION_REPLY = 4;

    public DetailsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setupTwitter();

        // Obtiene el ID del tweet y el cursor
        String idTweet = getActivity().getIntent().getStringExtra(getResources().getString(R.string.idTweet));
        Uri nUri = StatusContract.CONTENT_URI.buildUpon().appendPath(idTweet).build();
        cTweet = getActivity().getContentResolver().query(
                nUri, null, "", null, "");

        // Enlaza views
        nombreUsuario = (TextView) view.findViewById(R.id.nombreUsuario);
        nombrePantallaUsuario = (TextView) view.findViewById(R.id.nombrePantallaUsuario);
        textoTweet = (TextView) view.findViewById(R.id.textoTweet);
        numFavoritos = (TextView) view.findViewById(R.id.numeroFavoritos);
        numRetweets = (TextView) view.findViewById(R.id.numeroRetweets);
        botonRetweet = (ImageButton) view.findViewById(R.id.botonRetweet);
        botonFavorito = (ImageButton) view.findViewById(R.id.botonMeGusta);
        botonCompartir = (ImageButton) view.findViewById(R.id.botonCompartir);
        textoRespuesta = (TextView) view.findViewById(R.id.textoRespuesta);
        botonEnviarRespuesta = (Button) view.findViewById(R.id.botonEnviar);

        // Actualiza la vista según los datos recibidos
        iColID = cTweet.getColumnIndex(StatusContract.Column.ID);
        iColNombre = cTweet.getColumnIndex(StatusContract.Column.USER);
        iColNombrePantalla = cTweet.getColumnIndex(StatusContract.Column.SCREEN_NAME);
        iColMensaje = cTweet.getColumnIndex(StatusContract.Column.MESSAGE);
        iColEsFavorito = cTweet.getColumnIndex(StatusContract.Column.FAVORITED);
        iColEsRetweet = cTweet.getColumnIndex(StatusContract.Column.RETWEETED);
        iColNumFavs = cTweet.getColumnIndex(StatusContract.Column.FAVORITES_COUNT);
        iColNumRets = cTweet.getColumnIndex(StatusContract.Column.RETWEETS_COUNT);
        cTweet.moveToPosition(0);

        nombreUsuario.setText(cTweet.getString(iColNombre));
        nombrePantallaUsuario.setText("@" + cTweet.getString(iColNombrePantalla));
        textoTweet.setText(cTweet.getString(iColMensaje));
        muestraEstadisticas(cTweet.getInt(iColNumFavs), cTweet.getInt(iColNumRets));

        favorited = (cTweet.getInt(iColEsFavorito) == 0) ? false : true;
        retweeted = (cTweet.getInt(iColEsRetweet) == 0) ? false : true;
        if(favorited) marcarFavorito();
        else desmarcarFavorito();
        if(retweeted) marcarRetweet();
        else desmarcarRetweet();

        // Configura listeners
        botonFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorited) {
                    // Desmarca favorito
                    desmarcarFavorito();
                    new DetailsTask().execute(ACTION_NO_FAVORITE);
                } else {
                    // Marca favorito
                    marcarFavorito();
                    new DetailsTask().execute(ACTION_FAVORITE);
                }
            }
        });
        botonRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(retweeted) {
                    // Desmarca retweet
                    desmarcarRetweet();
                    new DetailsTask().execute(ACTION_NO_RETWEET);
                } else {
                    // Marca retweet
                    marcarRetweet();
                    new DetailsTask().execute(ACTION_RETWEET);
                }
            }
        });
        botonEnviarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String respuesta = textoRespuesta.getText().toString();
                if(respuesta.isEmpty()) {
                    Toast.makeText(getActivity(), "Introduce texto melón", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "No operativo", Toast.LENGTH_SHORT).show();
                    /* NO FUNCIONA, NO LO ENVÍA COMO RESPUESTA SINO COMO TWEET NORMAL
                    DetailsTask task = new DetailsTask();
                    task.respuesta = respuesta;
                    task.execute(ACTION_REPLY);
                    */
                }
            }
        });

        // Devuelve la vista personalizada
        return view;
    }

    private void muestraEstadisticas(int numFavs, int numRets) {
        numFavoritos.setText(String.format("%s FAVS", numFavs));
        numRetweets.setText(String.format("%s RETS", numRets));
    }

    private void marcarFavorito() {
        favorited = true;
        botonFavorito.setImageDrawable(getResources().getDrawable(R.drawable.heart_on));
    }

    private void desmarcarFavorito() {
        favorited = false;
        botonFavorito.setImageDrawable(getResources().getDrawable(R.drawable.heart_off));
    }

    private void marcarRetweet() {
        retweeted = true;
        botonRetweet.setImageDrawable(getResources().getDrawable(R.drawable.retweet_on));
    }

    private void desmarcarRetweet() {
        retweeted = false;
        botonRetweet.setImageDrawable(getResources().getDrawable(R.drawable.retweet_off));
    }

    private void setupTwitter() {
        String accessToken = prefs.getString(getResources().getString(R.string.keyAccessToken), "");
        String accessTokenSecret = prefs.getString(getResources().getString(R.string.keyAccessTokenSecret), "");

        // Comprueba que no están vacíos
        if(accessToken.isEmpty() || accessTokenSecret.isEmpty()) {
            Log.e(TAG, getResources().getString(R.string.resultado_sin_autenticar));
            // Redirige a la pantalla de configuración y muestra un mensaje
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            i.putExtra(getResources().getString(R.string.extraMensaje),
                    getResources().getString(R.string.resultado_sin_autenticar));
            getActivity().startActivity(i);
            return;
        }

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey("NViYhW9DqCzIR7td2TOVwGglE")
                .setOAuthConsumerSecret("AsqDzFGAwmFf9YFYN2ghwaTUDurBj4FRvIEbUBihqaDbvE1GdF")
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory factory = new TwitterFactory(builder.build());
        twitter = factory.getInstance();
    }


    class DetailsTask extends AsyncTask<Integer, Void, Boolean> {
        public String respuesta;
        private int action;
        protected Boolean doInBackground(Integer... params) {
            action = params[0];
            boolean result = true;
            long idTweet = cTweet.getLong(iColID);
            try {
                switch (action) {
                    case ACTION_FAVORITE:
                        twitter.createFavorite(idTweet);
                        break;
                    case ACTION_NO_FAVORITE:
                        twitter.destroyFavorite(idTweet);
                        break;
                    case ACTION_RETWEET:
                        twitter.retweetStatus(idTweet);
                        break;
                    case ACTION_NO_RETWEET:
                        twitter.unRetweetStatus(idTweet);
                        break;
                    case ACTION_REPLY:
                        StatusUpdate update = new StatusUpdate(respuesta);
                        update.setInReplyToStatusId(idTweet);
                        twitter.updateStatus(update);
                        break;
                    default:
                        result = false;
                        break;
                }
            } catch (TwitterException e) {
                e.printStackTrace();
                result = false;
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                if(action == ACTION_REPLY) {
                    textoRespuesta.setText("");
                    // Esconde el teclado
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {}
                } else {
                    int numFavs = cTweet.getInt(iColNumFavs);
                    int numRets = cTweet.getInt(iColNumRets);
                    if (action == ACTION_FAVORITE) numFavs++;
                    else if (action == ACTION_RETWEET) numRets++;

                    muestraEstadisticas(numFavs, numRets);
                }
            } else {
                Toast.makeText(getActivity(), "Error inesperado", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
