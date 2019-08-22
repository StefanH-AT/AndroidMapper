package at.tewan.androidmapper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.editor.InspectionSketch;
import at.tewan.androidmapper.editor.SharedSketchData;
import at.tewan.androidmapper.editor.ToolMode;
import at.tewan.androidmapper.editor.TrackSketch;
import at.tewan.androidmapper.preferences.Preferences;
import at.tewan.androidmapper.util.ErrorPrinter;
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


    private ImageView noteTool, bombTool, deleteTool;

    /**
     * Big ass onCreate method. I hate writing long methods but I guess I have to get used to it.
     * @param savedInstanceState Whatever this is
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set dark theme
        if(Preferences.isDarkTheme()) {
            setTheme(R.style.AppTheme_Dark_Fullscreen);
        }

        // Load layout
        setContentView(R.layout.activity_editor);

        noteTool = findViewById(R.id.noteTool);
        bombTool = findViewById(R.id.bombTool);
        deleteTool = findViewById(R.id.deleteTool);

        // Read beatmap difficulty
        Intent intent = getIntent();

        beatmapContainer = intent.getStringExtra(BEATMAP_CONTAINER);
        beatmapDifficulty = intent.getStringExtra(BEATMAP_DIFFICULTY);

        Log.i(LOG_TAG, "Beatmap container name: " + beatmapContainer);
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

        // Create https://processing.org/ sketches
        PFragment trackFragment = new PFragment(new TrackSketch());
        PFragment inspectionFragment = new PFragment(new InspectionSketch());

        trackFragment.setView(findViewById(R.id.trackFrame), this);
        inspectionFragment.setView(findViewById(R.id.inspectionFrame), this);

    }

    /**
     * Gets run when someone presses the delete tool button
     *
     * @param view The view that calls this event
     */
    public void delete(View view) {
        setTool(view, ToolMode.REMOVE);
    }

    public void note(View view) {

        // Toggle color when note tool is already selected
        if(getToolMode() == ToolMode.NOTE) {

            selectedColor = !selectedColor;

            // Set background color
            if(selectedColor == RED)
                noteTool.setColorFilter(Color.rgb(colorLeftR, colorLeftG, colorLeftB));

            else
                noteTool.setColorFilter(Color.rgb(colorRightR, colorRightG, colorRightB));

        }

        setTool(view, ToolMode.NOTE);
    }

    /**
     * Gets run when someone presses the save button.
     * Calls {@link at.tewan.androidmapper.beatmap.Beatmaps#saveDifficulty(Difficulty, String, String) Beatmaps::saveDifficulty} and prints a status Toast
     *
     * @param view The view that calls this event
     */
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

    /**
     * Gets run when someone presses the preview button
     *
     * @param view The view that calls this event
     */
    public void preview(View view) {
        ErrorPrinter.notify(this, getString(R.string.feature_missing));
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

    private void setTool(View view, ToolMode mode) {

        noteTool.setBackgroundColor(getColor(R.color.invisible));
        bombTool.setBackgroundColor(getColor(R.color.invisible));
        deleteTool.setBackgroundColor(getColor(R.color.invisible));

        view.setBackgroundColor(R.attr.colorAccent);
        SharedSketchData.setToolMode(mode);
    }
}
