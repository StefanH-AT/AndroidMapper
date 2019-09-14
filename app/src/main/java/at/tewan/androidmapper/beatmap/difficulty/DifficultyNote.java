package at.tewan.androidmapper.beatmap.difficulty;

public class DifficultyNote extends DifficultyTrackItem {

    public static final int TYPE_BOMB = 3;

    private int _lineLayer;
    private int _cutDirection;

    public DifficultyNote(float time, int type, int lineIndex, int lineLayer, int cutDirection) {
        super(time, type, lineIndex);
        this._lineLayer = lineLayer;
        this._cutDirection = cutDirection;
    }

    public int getCutDirection() {
        return _cutDirection;
    }

    public int getLineLayer() {
        return _lineLayer;
    }

    public void setCutDirection(int cutDirection) {
        this._cutDirection = cutDirection;
    }

    public void setLineLayer(int lineLayer) {
        this._lineLayer = lineLayer;
    }

    @Override
    public String toString() {
        return "Time: " + getTime() + ", LI: " + getLineIndex() + ", LL: " + getLineLayer() + ", C: " + getCutDirection() + ", T: " + getType();
    }
}
