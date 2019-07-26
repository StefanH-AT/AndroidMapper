package at.tewan.androidmapper.beatmap.difficulty;

public abstract class DifficultyItem {

    private float _time;
    private int _type;

    protected DifficultyItem(float time, int type) {
        this._time = time;
        this._type = type;
    }

    public float getTime() {
        return _time;
    }

    public int getType() {
        return _type;
    }

    public void setTime(float time) {
        this._time = time;
    }

    public void setType(int type) {
        this._type = type;
    }
}
