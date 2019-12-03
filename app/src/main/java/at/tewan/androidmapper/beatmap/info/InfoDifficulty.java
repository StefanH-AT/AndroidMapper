package at.tewan.androidmapper.beatmap.info;

/**
 * @author Stefan Heinz
 */
public class InfoDifficulty {

    private String _difficulty;
    private int _difficultyRank;
    private String _beatmapFilename;
    private float _noteJumpMovementSpeed;
    private float _noteJumpStartBeatOffset;
    private InfoDifficultyCustomData _customData;

    public InfoDifficultyCustomData getCustomData() {
        return _customData;
    }

    public float getNoteJumpMovementSpeed() {
        return _noteJumpMovementSpeed;
    }

    public float getNoteJumpStartBeatOffset() {
        return _noteJumpStartBeatOffset;
    }

    public int getDifficultyRank() {
        return _difficultyRank;
    }

    public String getBeatmapFilename() {
        return _beatmapFilename;
    }

    public String getDifficulty() {
        return _difficulty;
    }

    public void setBeatmapFilename(String beatmapFilename) {
        this._beatmapFilename = beatmapFilename;
    }

    public void setCustomData(InfoDifficultyCustomData customData) {
        this._customData = customData;
    }

    public void setDifficultyRank(int difficultyRank) {
        this._difficultyRank = difficultyRank;
    }

    public void setNoteJumpMovementSpeed(float noteJumpMovementSpeed) {
        this._noteJumpMovementSpeed = noteJumpMovementSpeed;
    }

    public void setNoteJumpStartBeatOffset(float noteJumpStartBeatOffset) {
        this._noteJumpStartBeatOffset = noteJumpStartBeatOffset;
    }

    public void setDifficulty(String difficulty) {
        this._difficulty = difficulty;
    }

}
