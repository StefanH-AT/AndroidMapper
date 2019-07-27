package at.tewan.androidmapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * The one and only beat map editor
 */
public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Editor";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        Log.i(LOG_TAG, "Loading beatmap hash: " + intent.getStringExtra("beatmap_hash"));
        Log.i(LOG_TAG, "Beatmap difficulty hash: " + intent.getStringExtra("beatmap_difficulty_hash"));

        setContentView(R.layout.activity_editor);
    }
}
