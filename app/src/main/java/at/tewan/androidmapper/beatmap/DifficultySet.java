package at.tewan.androidmapper.beatmap;

import java.util.ArrayList;

public class DifficultySet {

    private String _beatmapCharacteristicName;
    private ArrayList<Difficulty> _difficultyBeatmaps;

    public ArrayList<Difficulty> getDifficultyBeatmaps() {
        return _difficultyBeatmaps;
    }

    public String getBeatmapCharacteristicName() {
        return _beatmapCharacteristicName;
    }

    public void setBeatmapCharacteristicName(String beatmapCharacteristicName) {
        this._beatmapCharacteristicName = beatmapCharacteristicName;
    }

    public void setDifficultyBeatmaps(ArrayList<Difficulty> difficultyBeatmaps) {
        this._difficultyBeatmaps = difficultyBeatmaps;
    }
}
