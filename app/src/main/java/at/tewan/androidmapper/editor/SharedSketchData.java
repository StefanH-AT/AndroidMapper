package at.tewan.androidmapper.editor;

import android.util.Log;

import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import at.tewan.androidmapper.beatmap.info.Info;

public class SharedSketchData {

    public static final boolean RED = false;
    public static final boolean BLUE = true;

    private static final String LOG_TAG = "EditorData";

    static int lanes = 4;
    static int rows = 3;
    static ArrayList<DifficultyNote> notes = new ArrayList<>();
    static Info info;

    static float currentBeat = 0;
    static float totalBeats;
    static float songDuration = 60; // Song duration in seconds
    static int subBeatAmount = 4;

    public static boolean selectedColor = RED;

    public static void setInfo(Info info) {
        SharedSketchData.info = info;

        totalBeats = timeAsBeat(songDuration); // Need to divide by 60 because I need the time in minutes

        Log.i(LOG_TAG, "Total beats: " + totalBeats);
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
}
