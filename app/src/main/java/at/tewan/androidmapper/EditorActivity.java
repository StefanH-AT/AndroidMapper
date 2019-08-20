package at.tewan.androidmapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.editor.InspectionSketch;
import at.tewan.androidmapper.editor.SharedSketchData;
import at.tewan.androidmapper.editor.ToolMode;
import at.tewan.androidmapper.editor.TrackSketch;
import processing.android.PFragment;

import static at.tewan.androidmapper.editor.SharedSketchData.BLUE;
import static at.tewan.androidmapper.editor.SharedSketchData.RED;

/**
 * The one and only beat map editor
 */
public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Editor";

    private Difficulty difficulty;

    private String beatmapHash, beatmapDifficulty;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        beatmapHash = intent.getStringExtra("beatmap_hash");
        beatmapDifficulty = intent.getStringExtra("beatmap_difficulty");


        setContentView(R.layout.activity_editor);

        Log.i(LOG_TAG, "Loading beatmap hash: " + beatmapHash);
        Log.i(LOG_TAG, "Beatmap difficulty file: " + beatmapDifficulty);



        Info info = Beatmaps.readStoredBeatmapInfo(beatmapHash);

        try {
            difficulty = Beatmaps.readDifficulty(beatmapHash, beatmapDifficulty);
        } catch (IOException ex) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error loading beatmap");
            builder.setMessage("Failed to load beatmap. Please send this error message to us: \n\n" + ex.getMessage());
            builder.create().show();

            finish(); // Stop activity
        }
        SharedSketchData.init(info, difficulty);


        // Canvases
        PFragment trackFragment = new PFragment(new TrackSketch());
        PFragment inspectionFragment = new PFragment(new InspectionSketch());

        trackFragment.setView(findViewById(R.id.trackFrame), this);
        inspectionFragment.setView(findViewById(R.id.inspectionFrame), this);


        // UI
        ToggleButton colorToggle = findViewById(R.id.colorToggle);

        colorToggle.setOnCheckedChangeListener((view, isChecked) -> {

            SharedSketchData.setToolMode(ToolMode.NOTE);

            // false = red
            // true = blue
            if(isChecked) {
                colorToggle.setBackgroundResource(R.drawable.note_blue);
                SharedSketchData.selectedColor = BLUE;
            } else {
                colorToggle.setBackgroundResource(R.drawable.note_red);
                SharedSketchData.selectedColor = RED;
            }

        });
    }

    public void delete(View view) {
        SharedSketchData.setToolMode(ToolMode.REMOVE);
    }

    public void save(View view) {
        difficulty = SharedSketchData.getDifficulty();

        boolean success = Beatmaps.saveDifficulty(difficulty, beatmapHash, beatmapDifficulty);

        Toast statusToast;

        if(success) {
            statusToast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
        } else {
            statusToast = Toast.makeText(this, "Error saving difficulty", Toast.LENGTH_LONG);
        }

        statusToast.show();





    }

    public void preview(View view) {

    }

    public void exit(View view) {

    }
}
