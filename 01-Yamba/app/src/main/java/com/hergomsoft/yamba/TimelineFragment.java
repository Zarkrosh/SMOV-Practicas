package com.hergomsoft.yamba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.hergomsoft.yamba.db.StatusContract;

public class TimelineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = TimelineFragment.class.getSimpleName();

    private SimpleCursorAdapter mAdapter;

    private static final String[] FROM = {StatusContract.Column.ID, StatusContract.Column.USER,
            StatusContract.Column.MESSAGE, StatusContract.Column.CREATED_AT};

    private static final int[] TO = {R.id.list_item_text_id, R.id.list_item_text_user,
            R.id.list_item_text_message, R.id.list_item_text_created_at};

    private static final int LOADER_ID = 27;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.timeline_item,
                null, FROM, TO, 0);
        mAdapter.setViewBinder(new TimelineViewBinder());
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id != LOADER_ID) return null;
        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(getActivity(), StatusContract.CONTENT_URI, null, null,
                null, StatusContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished with cursor: " + data.getCount());
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Obtiene ID del tweet seleccionado
        Cursor c = (Cursor) mAdapter.getItem(position);
        String idTweet = c.getString(0);

        // Muestra el tweet en la actividad de detalles
        Intent i = new Intent(getActivity(), DetailsActivity.class);
        i.putExtra(getResources().getString(R.string.idTweet), idTweet);
        startActivity(i);
    }

    // Para gestionar la vista del item
    class TimelineViewBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() != R.id.list_item_text_created_at) return false;

            // Conversión del timestamp a tiempo relativo
            long timestamp = cursor.getLong(columnIndex);
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp);
            ((TextView) view).setText(relativeTime);

            return true;
        }
    }
}
