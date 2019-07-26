package at.tewan.androidmapper.beatmap.info;

import java.util.ArrayList;

public class InfoDifficultySet {

    private String _beatmapCharacteristicName;
    private ArrayList<InfoDifficulty> _difficultyBeatmaps;

    public ArrayList<InfoDifficulty> getDifficultyBeatmaps() {
        return _difficultyBeatmaps;
    }

    public String getBeatmapCharacteristicName() {
        return _beatmapCharacteristicName;
    }

    public void setBeatmapCharacteristicName(String beatmapCharacteristicName) {
        this._beatmapCharacteristicName = beatmapCharacteristicName;
    }

    public void setDifficultyBeatmaps(ArrayList<InfoDifficulty> difficultyBeatmaps) {
        this._difficultyBeatmaps = difficultyBeatmaps;
    }
}
