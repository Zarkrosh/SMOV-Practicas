package com.hergomsoft.yamba.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hergomsoft.yamba.db.DBHelper;
import com.hergomsoft.yamba.db.StatusContract;

public class StatusProvider extends ContentProvider {

    private static final String TAG = StatusProvider.class.getSimpleName();
    private DBHelper dbHelper;
    private static final UriMatcher sURIMatcher;

    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Todos los estados
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE, StatusContract.STATUS_DIR);
        // Un estado en concreto (numérico)
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE + "/#", StatusContract.STATUS_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        Log.d(TAG, "onCreated");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String where;
        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection;
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID
                        + "="
                        + id
                        + (selection.isEmpty() ? "" : " and ( " + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }

        // Abre conexión de lectura de la BD
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String orderBy = (sortOrder.isEmpty() ? StatusContract.DEFAULT_SORT : sortOrder);
        Cursor cursor = db.query(StatusContract.TABLE, projection, where,
                selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        Log.d(TAG, "Registros recuperados: " + cursor.getCount());

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                Log.d(TAG, "gotType: vnd.android.cursor.dir/vnd.com.hergomsoft.yamba.provider.status");
                return "vnd.android.cursor.dir/vnd.com.hergomsoft.yamba.provider.status";
            case StatusContract.STATUS_ITEM:
                Log.d(TAG, "gotType: vnd.android.cursor.item/vnd.com.hergomsoft.yamba.provider.status");
                return "vnd.android.cursor.item/vnd.com.hergomsoft.yamba.provider.status";
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri ret = null;

        // Comprueba que la URI es correcta
        if (sURIMatcher.match(uri) != StatusContract.STATUS_DIR) {
            throw new IllegalArgumentException("URI incorrecta: " + uri);
        }

        // Abre conexión a la base de datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Intenta hacer la operación INSERT
        long rowId = db.insertWithOnConflict(StatusContract.TABLE, null,
                values, SQLiteDatabase.CONFLICT_IGNORE);

        // ¿Se insertó correctamente?
        if (rowId != -1) {
            long id = values.getAsLong(StatusContract.Column.ID);
            ret = ContentUris.withAppendedId(uri, id);
            Log.d(TAG, "URI insertada: " + ret);
            // Notificar que los datos para la URI han cambiado
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ret;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String where;

        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection;
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID
                        + "="
                        + id
                        + (selection.isEmpty() ? "" : " and ( " + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("URI incorrecta: " + uri);
        }

        // Abre conexión a la BD
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Intenta la operación UPDATE
        int ret = db.delete(StatusContract.TABLE, where, selectionArgs);
        if (ret > 0) {
            // Notifica cambio
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "Registros borrados: " + ret);

        return ret;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String where;

        switch (sURIMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                where = selection;
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID
                        + "="
                        + id
                        + (selection.isEmpty() ? "" : " and ( " + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("URI incorrecta: " + uri);
        }

        // Abre conexión a la BD
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Intenta la operación UPDATE
        int ret = db.update(StatusContract.TABLE, values, where, selectionArgs);
        if (ret > 0) {
            // Notifica cambio
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "Registros actualizados: " + ret);

        return ret;
    }
}
