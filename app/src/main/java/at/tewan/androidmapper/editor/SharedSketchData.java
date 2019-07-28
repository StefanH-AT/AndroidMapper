package at.tewan.androidmapper.editor;

import android.util.Log;

import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import at.tewan.androidmapper.beatmap.info.Info;

public class SharedSketchData {

    private static final String LOG_TAG = "EditorData";

    static int lanes = 4;
    static int rows = 3;
    static ArrayList<DifficultyNote> notes = new ArrayList<>();
    static Info info;

    static float currentBeat = 0;
    static float totalBeats;
    static float songDuration = 60; // Song duration in seconds

    public static void setInfo(Info info) {
        SharedSketchData.info = info;

        totalBeats = info.getBeatsPerMinute() * songDuration / 60; // Need to divide by 60 because I need the time in minutes

        Log.i(LOG_TAG, "Total beats: " + totalBeats);
    }
}
