package at.tewan.androidmapper.editor;

import android.util.Log;

import com.google.common.logging.nano.Vr;

import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyEvent;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyObstacle;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.preferences.Preferences;

public class SharedSketchData {

    private static final String LOG_TAG = "EditorData";

    public static final boolean RED = false;
    public static final boolean BLUE = true;

    public static int colorLeftR, colorLeftG, colorLeftB;
    public static int colorRightR, colorRightG, colorRightB;

    private static ArrayList<DifficultyNote> notesToRemove = new ArrayList<>();

    static boolean theme;

    static int backgroundColor;
    static int strokeColor;

    static int lanes = 4;
    static int rows = 3;
    private static ArrayList<DifficultyNote> notes;
    private static ArrayList<DifficultyEvent> events;
    private static ArrayList<DifficultyObstacle> obstacles;
    static Info info;
    static Difficulty difficulty;

    static float currentBeat = 0;
    static float totalBeats;
    static float songDuration = 60; // Song duration in seconds // TODO: Use the actual song duration
    static int subBeatAmount = 4;

    static ToolMode toolMode;

    public static boolean selectedColor = RED;

    public static void init(Info info, Difficulty difficulty, boolean theme) {

        SharedSketchData.info = info;
        SharedSketchData.difficulty = difficulty;

        totalBeats = timeAsBeat(songDuration);
        Log.i(LOG_TAG, "Total beats: " + totalBeats);

        SharedSketchData.notes = difficulty.getNotes();


        // Theme colors
        if(theme == Preferences.THEME_LIGHT) {
            backgroundColor = 255;
            strokeColor = 20;
        } else {
            backgroundColor = 40;
            strokeColor = 230;
        }

        // Draw Custom colors or default colors?

        // TODO: Implement custom color drawing

    //    if(!Preferences.doesDrawCustomColors()) { // <=== Uncomment this line when implementing custom colors

            // Default colors
            colorLeftR = 244;
            colorLeftG = 43;
            colorLeftB = 32;

            colorRightR = 25;
            colorRightG = 134;
            colorRightB = 242;
    //    }

    }

    public static void setToolMode(ToolMode toolMode) {
        SharedSketchData.toolMode = toolMode;
    }

    public static ToolMode getToolMode() {
        return toolMode;
    }

    static float beatAsTime(float beat) {
        return beat / info.getBeatsPerMinute() * 60 * subBeatAmount;
    }

    static float timeAsBeat(float time) {
        return info.getBeatsPerMinute() * time / 60 / subBeatAmount;
    }

    static int colorAsType(boolean color) {
        if(color)
            return 1;
        else
            return 0;
    }

    static boolean typeAsColor(int type) {
        return type == 1;
    }

    static float snapFloat(float value, float steps) {
        return Math.round(value / steps) * steps;
    }

    static int snapInt(int value, int steps) {
        return Math.round((float) value / steps) * steps;
    }

    /**
     * Adds note to an array of disposable notes,
     * which will be removed on the next call of {@link SharedSketchData#getNotes() getNotes()} to avoid {@link java.util.ConcurrentModificationException ConcurrentModificationException}
     *
     * @param note to remove
     */
    static void requestNoteRemoval(DifficultyNote note) {
        notesToRemove.add(note);
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static Info getInfo() {
        return info;
    }

    public static ArrayList<DifficultyNote> getNotes() {

        notes.removeAll(notesToRemove);
        notesToRemove.clear();

        return notes;
    }
}
