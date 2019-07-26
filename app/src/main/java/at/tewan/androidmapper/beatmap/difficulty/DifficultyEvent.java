package at.tewan.androidmapper.beatmap.difficulty;

public class DifficultyEvent extends DifficultyItem {


    private int _value;


    public DifficultyEvent(float time, int type, int value) {
        super(time, type);
        this._value = value;
    }

    public int getValue() {
        return _value;
    }

    public void setValue(int value) {
        this._value = value;
    }
}