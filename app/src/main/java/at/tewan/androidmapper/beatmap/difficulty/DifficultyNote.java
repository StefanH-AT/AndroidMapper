package at.tewan.androidmapper.beatmap.difficulty;

public class DifficultyNote extends DifficultyItem {

    private int _lineIndex;
    private int _lineLayer;
    private int _cutDirection;

    public DifficultyNote(float time, int type, int lineIndex, int lineLayer, int cutDirection) {
        super(time, type);
        this._lineIndex = lineIndex;
        this._lineLayer = lineLayer;
        this._cutDirection = cutDirection;
    }

    public int getCutDirection() {
        return _cutDirection;
    }

    public int getLineIndex() {
        return _lineIndex;
    }

    public int getLineLayer() {
        return _lineLayer;
    }

    public void setCutDirection(int cutDirection) {
        this._cutDirection = cutDirection;
    }

    public void setLineIndex(int lineIndex) {
        this._lineIndex = lineIndex;
    }

    public void setLineLayer(int lineLayer) {
        this._lineLayer = lineLayer;
    }

}
