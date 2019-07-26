package at.tewan.androidmapper.beatmap.difficulty;

public class DifficultyObstacle extends DifficultyItem {

    private int _lineIndex;
    private float _duration;
    private int _width;

    public DifficultyObstacle(float time, int type, int lineIndex, float duration, int width) {
        super(time, type);
        this._lineIndex = lineIndex;
        this._duration = duration;
        this._width = width;
    }

    public int getLineIndex() {
        return _lineIndex;
    }

    public float getDuration() {
        return _duration;
    }

    public int getWidth() {
        return _width;
    }

    public void setLineIndex(int lineIndex) {
        this._lineIndex = lineIndex;
    }

    public void setDuration(float duration) {
        this._duration = duration;
    }

    public void setWidth(int width) {
        this._width = width;
    }
}
