package at.tewan.androidmapper.beatmap.difficulty;

public abstract class DifficultyTrackItem extends DifficultyItem {

    private int _lineIndex;

    protected DifficultyTrackItem(float time, int type, int lineIndex) {
        super(time, type);
        this._lineIndex = lineIndex;
    }

    public int getLineIndex() {
        return _lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this._lineIndex = lineIndex;
    }

}
