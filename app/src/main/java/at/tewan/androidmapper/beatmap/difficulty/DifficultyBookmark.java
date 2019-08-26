package at.tewan.androidmapper.beatmap.difficulty;

public class DifficultyBookmark extends DifficultyItem {

    private String _name;

    DifficultyBookmark(float time, String name) {
        super(time);
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = _name;
    }
}
