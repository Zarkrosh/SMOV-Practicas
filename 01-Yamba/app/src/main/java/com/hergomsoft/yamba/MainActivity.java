package com.hergomsoft.yamba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hergomsoft.yamba.db.StatusContract;
import com.hergomsoft.yamba.services.RefreshService;

/**
 * @author Abel Herrero Gómez (abeherr)
 * Twitter: @Abel85985400
 * Access token: 1179828768250224640-gxEUiFvyX1KWd7b377UrABBbw4woEQ
 * Access token secret: qyliVoOnz0aNUKQRo1Dah1xCVudrbAPGgM4ql6W5nOkB0
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_tweet:
                startActivity(new Intent(this, StatusActivity.class));
                return true;
            case R.id.itemServiceStart:
                startService(new Intent(this, RefreshService.class));
                return true;
            case R.id.itemServiceStop:
                stopService(new Intent(this, RefreshService.class));
                return true;
            case R.id.action_purge:
                int rows = getContentResolver().delete(StatusContract.CONTENT_URI, null, null);
                Toast.makeText(this, rows + " entradas de la base de datos borradas", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
