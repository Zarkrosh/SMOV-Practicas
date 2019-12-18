package com.hergomsoft.yamba.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    private final String CREATE_TABLE = "CREATE TABLE %s (%s INT PRIMARY KEY, %s TEXT, %s TEXT, %s INT)";
    private final String DROP_TABLE = "DROP TABLE IF EXISTS %s";

    // Constructor
    public DBHelper(Context context) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // En la primera ejecución crea la tabla
        String sql = String.format(CREATE_TABLE, StatusContract.TABLE, StatusContract.Column.ID,
                StatusContract.Column.USER, StatusContract.Column.MESSAGE, StatusContract.Column.CREATED_AT);

        Log.d(TAG, "onCreate con SQL: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui irían las sentencias del tipo ALTER TABLE, de momento lo hacemos mas sencillo...

        // Borramos la vieja base de datos
        db.execSQL(String.format(DROP_TABLE, StatusContract.TABLE));

        // Creamos una base de datos nueva
        onCreate(db);

        Log.d(TAG, "onUpgrade");
    }
}