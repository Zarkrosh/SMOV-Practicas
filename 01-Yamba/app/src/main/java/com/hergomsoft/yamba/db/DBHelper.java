package com.hergomsoft.yamba.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    /* ESTRUCTURA DE LA BD:
        - ID                INT PRIMARY KEY
        - USER              TEXT
        - SCREEN_NAME       TEXT
        - MESSAGE           TEXT
        - CREATED_AT        INT
        - FAVORITED         INT (1,0)
        - RETWEETED         INT (1,0)
        - FAVORITES_COUNT   INT
        - RETWEETS_COUNT    INT
     */
    private final String CREATE_TABLE = "CREATE TABLE %s (" +
            "%s INT PRIMARY KEY, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s INT, " +
            "%s INT, " +
            "%s INT, " +
            "%s INT, " +
            "%s INT)"; // TODO
    private final String DROP_TABLE = "DROP TABLE IF EXISTS %s";

    // Constructor
    public DBHelper(Context context) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // En la primera ejecución crea la tabla
        String sql = String.format(CREATE_TABLE, StatusContract.TABLE,
                StatusContract.Column.ID,
                StatusContract.Column.USER,
                StatusContract.Column.SCREEN_NAME,
                StatusContract.Column.MESSAGE,
                StatusContract.Column.CREATED_AT,
                StatusContract.Column.FAVORITED,
                StatusContract.Column.RETWEETED,
                StatusContract.Column.FAVORITES_COUNT,
                StatusContract.Column.RETWEETS_COUNT
        );

        Log.d(TAG, "onCreate con SQL: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borramos la vieja base de datos
        db.execSQL(String.format(DROP_TABLE, StatusContract.TABLE));

        // Creamos una base de datos nueva
        onCreate(db);

        Log.d(TAG, "onUpgrade");
    }
}