package at.tewan.androidmapper.beatmap;

public class DifficultyCustomData {

    private String _difficultyLabel;
    private float _editorOffset;
    private float _editorOldOffset;
    private DifficultyColor _colorLeft;
    private DifficultyColor _colorRight;
    private String _warnings;
    private String _information;
    private String _suggestions;
    private String _requirements;

    public DifficultyColor getColorLeft() {
        return _colorLeft;
    }

    public DifficultyColor getColorRight() {
        return _colorRight;
    }

    public float getEditorOffset() {
        return _editorOffset;
    }

    public float getEditorOldOffset() {
        return _editorOldOffset;
    }

    public String getDifficultyLabel() {
        return _difficultyLabel;
    }

    public String getInformation() {
        return _information;
    }

    public String getRequirements() {
        return _requirements;
    }

    public String getSuggestions() {
        return _suggestions;
    }

    public String getWarnings() {
        return _warnings;
    }

    public void setColorLeft(DifficultyColor colorLeft) {
        this._colorLeft = colorLeft;
    }

    public void setColorRight(DifficultyColor colorRight) {
        this._colorRight = colorRight;
    }

    public void setDifficultyLabel(String difficultyLabel) {
        this._difficultyLabel = difficultyLabel;
    }

    public void setEditorOffset(float editorOffset) {
        this._editorOffset = editorOffset;
    }

    public void setEditorOldOffset(float editorOldOffset) {
        this._editorOldOffset = editorOldOffset;
    }

    public void setInformation(String information) {
        this._information = information;
    }

    public void setRequirements(String requirements) {
        this._requirements = requirements;
    }

    public void setSuggestions(String suggestions) {
        this._suggestions = suggestions;
    }

    public void setWarnings(String warnings) {
        this._warnings = warnings;
    }

}
