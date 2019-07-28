package at.tewan.androidmapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.editor.InspectionSketch;
import at.tewan.androidmapper.editor.SharedSketchData;
import at.tewan.androidmapper.editor.TrackSketch;
import processing.android.PFragment;

/**
 * The one and only beat map editor
 */
public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Editor";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String beatmapHash = intent.getStringExtra("beatmap_hash");
        String beatmapDifficultyHash = intent.getStringExtra("beatmap_difficulty_hash");

        setContentView(R.layout.activity_editor);

        Log.i(LOG_TAG, "Loading beatmap hash: " + beatmapHash);
        Log.i(LOG_TAG, "Beatmap difficulty hash: " + beatmapDifficultyHash);



        Info info = Beatmaps.readStoredBeatmap(beatmapHash);

        // Canvases

        SharedSketchData.setInfo(info);
        PFragment trackFragment = new PFragment(new TrackSketch());
        PFragment inspectionFragment = new PFragment(new InspectionSketch());

        trackFragment.setView(findViewById(R.id.trackFrame), this);
        inspectionFragment.setView(findViewById(R.id.inspectionFrame), this);


        // UI
        ToggleButton colorToggle = findViewById(R.id.colorToggle);

        colorToggle.setOnCheckedChangeListener((view, isChecked) -> {

            // false = red
            // true = blue
            if(isChecked) {
                colorToggle.setBackgroundResource(R.drawable.note_blue);
            } else {
                colorToggle.setBackgroundResource(R.drawable.note_red);
            }
        });
    }
}
