package at.tewan.androidmapper;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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
 */
public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Editor";

    private Difficulty difficulty;
    private String beatmapContainer, beatmapDifficulty;

    private FloatingActionMenu toolMenu;
    private FloatingActionButton toolNote, toolWall, toolBomb, toolDelete;

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
        toolMenu = findViewById(R.id.toolMenu);
        toolMenu.setIconAnimated(false);
        toolMenu.setIconAnimationOpenInterpolator(new OvershootInterpolator());
        toolNote = findViewById(R.id.toolNote);
        toolBomb = findViewById(R.id.toolBomb);
        toolDelete = findViewById(R.id.toolDelete);

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
            
            ErrorPrinter.msg(this, "Failed to load beatmap. Please send this error message to us:", ex);

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

    public void selectTool(View view) {

        // I'm not proud of this

        TypedValue accentValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, accentValue, true);

        toolMenu.setMenuButtonColorNormal(accentValue.data);

        switch (view.getId()) {
            case R.id.toolNote:

                // Toggle color when note tool is already selected
                if(getToolMode() == ToolMode.NOTE) {
                    selectedColor = !selectedColor;
                }

                // Set background color
                if(selectedColor == RED)
                    toolMenu.setMenuButtonColorNormal(Color.rgb(colorLeftR, colorLeftG, colorLeftB));
                else
                    toolMenu.setMenuButtonColorNormal(Color.rgb(colorRightR, colorRightG, colorRightB));

                toolMenu.getMenuIconView().setImageResource(R.drawable.ic_action_note);
                toolMenu.setMenuButtonLabelText(getString(R.string.note));

                setToolMode(ToolMode.NOTE);

            break;

            case R.id.toolBomb:
                setToolMode(ToolMode.BOMB);
                toolMenu.getMenuIconView().setImageResource(R.drawable.ic_action_bomb);
                toolMenu.setMenuButtonLabelText(getString(R.string.bomb));
            break;

            case R.id.toolDelete:
                setToolMode(ToolMode.REMOVE);
                toolMenu.getMenuIconView().setImageResource(R.drawable.ic_delete_white_24dp);
                toolMenu.setMenuButtonLabelText(getString(R.string.delete));
            break;
        }
    }

}
