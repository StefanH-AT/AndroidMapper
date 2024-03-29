package at.tewan.androidmapper.editor;

import android.util.Log;

import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyEvent;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyObstacle;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.preferences.Preferences;

/**
 * @author Stefan Heinz
 *
 *
 *
 */
public class SharedSketchData {

    private static final String LOG_TAG = "EditorData";

    public static final boolean RED = false;
    public static final boolean BLUE = true;
    public static final int BOMB_COLOR = 90;
    public static final boolean FORWARD = true;
    public static final boolean BACKWARD = false;

    public static int colorLeftR, colorLeftG, colorLeftB;
    public static int colorRightR, colorRightG, colorRightB;

    private static ArrayList<DifficultyNote> disposableNotes = new ArrayList<>();
    private static ArrayList<DifficultyNote> addableNotes = new ArrayList<>();

    static int backgroundColor;
    static int strokeColor;
    static int debugColorR, debugColorG, debugColorB;
    static int baseColor;

    static int lanes;
    static int rows;
    private static ArrayList<DifficultyNote> notes;
    private static ArrayList<DifficultyEvent> events;
    private static ArrayList<DifficultyObstacle> obstacles;
    static Info info;
    static Difficulty difficulty;

    static float currentBeat;
    static float totalBeats;
    static float songDuration = 60; // Song duration in seconds // TODO: Use the actual song duration
    static int subBeatAmount;
    static float bpm;

    static ToolMode toolMode;

    public static boolean selectedColor = RED;

    public static void init(Info info, Difficulty difficulty, boolean theme) {

        // Default values
        currentBeat = 0;
        lanes = 4;
        rows = 3;

        SharedSketchData.info = info;
        SharedSketchData.difficulty = difficulty;

        bpm = info.getBeatsPerMinute();

        // Sub beat count
        subBeatAmount = Preferences.getSubBeatCountKey();
        Log.i(LOG_TAG, "Sub Beat Amount: " + subBeatAmount);

        // Total beat count
        totalBeats = timeAsBeat(songDuration);                  // NOTICE: This code must be executed AFTER subBeatAmount is initialized!!
        Log.i(LOG_TAG, "Total beats: " + totalBeats);

        SharedSketchData.notes = difficulty.getNotes();


        // Theme colors
        if(theme == Preferences.THEME_LIGHT) {
            backgroundColor = 255;
            strokeColor = 20;
            baseColor = 70;

            debugColorR = 0;
            debugColorG = 130;
            debugColorB = 0;

        } else {
            backgroundColor = 40;
            strokeColor = 230;
            baseColor = 160;

            debugColorR = 0;
            debugColorG = 230;
            debugColorB = 0;

        }

        // Draw Custom colors or default colors?

        // TODO: Implement custom color drawing

    //    if(!Preferences.doesDrawCustomColors()) { // <=== Uncomment this if statement when implementing custom colors

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
    static void disposeNote(DifficultyNote note) {
        disposableNotes.add(note);

        Log.i(LOG_TAG, "Added note '" + note.toString() + "' as disposable note");
    }

    static void addNote(DifficultyNote note) {
        addableNotes.add(note);

        Log.i(LOG_TAG, "Adding note '" + note.toString() + "' as disposable note");
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static Info getInfo() {
        return info;
    }

    static ArrayList<DifficultyNote> getNotes() {

        if(disposableNotes.size() > 0) {
            notes.removeAll(disposableNotes);
            disposableNotes.clear();
        }

        if(addableNotes.size() > 0) {
            notes.addAll(addableNotes);
            addableNotes.clear();
        }

        return notes;
    }

    public static void moveCurrentBeat(boolean direction) {
        currentBeat += (float) (direction ? 1 : -1) / subBeatAmount;
    }
}
