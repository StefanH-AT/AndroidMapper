package at.tewan.androidmapper.beatmap;

public class Difficulty {

    private String difficulty;
    private int _difficultyRank;
    private String _beatmapFilename;
    private float _noteJumpMovementSpeed;
    private float _noteJumpStartBeatOffset;
    private DifficultyCustomData _customData;

    public DifficultyCustomData getCustomData() {
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
        return difficulty;
    }

    public void setBeatmapFilename(String beatmapFilename) {
        this._beatmapFilename = beatmapFilename;
    }

    public void setCustomData(DifficultyCustomData customData) {
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
        this.difficulty = difficulty;
    }

}
