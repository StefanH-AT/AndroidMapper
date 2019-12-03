package at.tewan.androidmapper.beatmap.difficulty;

/**
 * @author Stefan Heinz
 */
public class DifficultyItem {

    private float _time;

    DifficultyItem(float time) {
        this._time = time;
    }

    public float getTime() {
        return _time;
    }

    public void setTime(float time) {
        this._time = time;
    }

}
