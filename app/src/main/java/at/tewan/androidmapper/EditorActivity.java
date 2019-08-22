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
import at.tewan.androidmapper.preferences.Preferences;
import processing.android.PFragment;

import static at.tewan.androidmapper.editor.SharedSketchData.*;
import static at.tewan.androidmapper.util.ActivityArguments.*;

/**
 * The one and only beat map editor
 *
 *
 * Tasks:
 * TODO: Implement Bombs
 * TODO: Implement Walls
 * TODO: Cut/Copy Paste
 * TODO: Light Event Editor
 *
 *
 *
 *
 */
public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Editor";

    private Difficulty difficulty;

    private String beatmapContainer, beatmapDifficulty;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set dark theme
        if(Preferences.isDarkTheme()) {
            setTheme(R.style.AppTheme_Dark_Fullscreen);
        }

        // Load layout
        setContentView(R.layout.activity_editor);

        // Read beatmap difficulty
        Intent intent = getIntent();

        beatmapContainer = intent.getStringExtra(BEATMAP_CONTAINER);
        beatmapDifficulty = intent.getStringExtra(BEATMAP_DIFFICULTY);

        Log.i(LOG_TAG, "Loading beatmap hash: " + beatmapContainer);
        Log.i(LOG_TAG, "Beatmap difficulty file: " + beatmapDifficulty);



        Info info = Beatmaps.readBeatmapInfo(beatmapContainer);

        try {
            difficulty = Beatmaps.readDifficulty(beatmapContainer, beatmapDifficulty);
        } catch (IOException ex) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error loading beatmap");
            builder.setMessage("Failed to load beatmap. Please send this error message to us: \n\n" + ex.getMessage());
            builder.create().show();

            finish(); // Stop activity
        }
        SharedSketchData.init(info, difficulty, Preferences.isDarkTheme());


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

        boolean success = Beatmaps.saveDifficulty(difficulty, beatmapContainer, beatmapDifficulty);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_editor_exit);
        builder.setMessage(R.string.message_editor_exit);

        builder.setPositiveButton(R.string.yes, (dialog, which) -> finish());
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());


        builder.create().show();

    }

    @Override
    public void onBackPressed() {
        exit(null);
    }
}
