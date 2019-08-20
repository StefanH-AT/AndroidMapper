package at.tewan.androidmapper.editor;

import android.util.Log;

import com.google.common.logging.nano.Vr;

import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyEvent;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import at.tewan.androidmapper.beatmap.difficulty.DifficultyObstacle;
import at.tewan.androidmapper.beatmap.info.Info;

public class SharedSketchData {

    public static final boolean RED = false;
    public static final boolean BLUE = true;

    private static final String LOG_TAG = "EditorData";

    static int lanes = 4;
    static int rows = 3;
    static ArrayList<DifficultyNote> notes;
    static ArrayList<DifficultyEvent> events;
    static ArrayList<DifficultyObstacle> obstacles;
    static Info info;
    static Difficulty difficulty;

    static float currentBeat = 0;
    static float totalBeats;
    static float songDuration = 60; // Song duration in seconds // TODO: Use the actual song duration
    static int subBeatAmount = 4;

    static ToolMode toolMode;

    public static boolean selectedColor = RED;

    public static void init(Info info, Difficulty difficulty) {

        SharedSketchData.info = info;
        SharedSketchData.difficulty = difficulty;

        totalBeats = timeAsBeat(songDuration);
        Log.i(LOG_TAG, "Total beats: " + totalBeats);

        SharedSketchData.notes = difficulty.getNotes();
    }

    public static void setToolMode(ToolMode toolMode) {
        SharedSketchData.toolMode = toolMode;
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

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static Info getInfo() {
        return info;
    }
}
