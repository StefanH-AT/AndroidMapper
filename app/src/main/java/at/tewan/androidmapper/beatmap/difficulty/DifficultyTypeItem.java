package at.tewan.androidmapper.beatmap.difficulty;

public abstract class DifficultyTypeItem extends DifficultyItem {

    private int _type;

    DifficultyTypeItem(float time, int type) {
        super(time);
        this._type = type;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }
}
