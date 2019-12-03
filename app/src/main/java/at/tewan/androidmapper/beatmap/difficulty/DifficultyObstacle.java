package at.tewan.androidmapper.beatmap.difficulty;

/**
 * @author Stefan Heinz
 */
public class DifficultyObstacle extends DifficultyTrackItem {

    private float _duration;
    private int _width;

    public DifficultyObstacle(float time, int type, int lineIndex, float duration, int width) {
        super(time, type, lineIndex);
        this._duration = duration;
        this._width = width;
    }

    public float getDuration() {
        return _duration;
    }

    public int getWidth() {
        return _width;
    }



    public void setDuration(float duration) {
        this._duration = duration;
    }

    public void setWidth(int width) {
        this._width = width;
    }
}
