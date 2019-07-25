package at.tewan.androidmapper.beatmap;


import java.util.ArrayList;

/**
 * The JSON Data model for info.dat files. Also used internally.
 * Definition: https://github.com/Kylemc1413/SongCore#infodat-explanation
 * */
public class Beatmap {

    private String _version;
    private String _songName;
    private String _songSubName;
    private String _songAuthorName;
    private String _levelAuthorName;
    private float _beatsPerMinute;
    private float _songTimeOffset;
    private float _shuffle;
    private float _shufflePeriod;
    private float _previewStartTime;
    private float _previewDuration;
    private String _songFilename;
    private String _coverImageFilename;
    private String _environmentName;
    private BeatmapCustomData _customData;
    private ArrayList<DifficultySet> _difficultyBeatmapSets;

    public ArrayList<DifficultySet> getDifficultyBeatmapSets() {
        return _difficultyBeatmapSets;
    }

    public BeatmapCustomData getCustomData() {
        return _customData;
    }

    public float getBeatsPerMinute() {
        return _beatsPerMinute;
    }

    public float getPreviewDuration() {
        return _previewDuration;
    }

    public float getPreviewStartTime() {
        return _previewStartTime;
    }

    public float getShuffle() {
        return _shuffle;
    }

    public float getShufflePeriod() {
        return _shufflePeriod;
    }

    public float getSongTimeOffset() {
        return _songTimeOffset;
    }

    public String getCoverImageFilename() {
        return _coverImageFilename;
    }

    public String getEnvironmentName() {
        return _environmentName;
    }

    public String getLevelAuthorName() {
        return _levelAuthorName;
    }

    public String getSongAuthorName() {
        return _songAuthorName;
    }

    public String getSongFilename() {
        return _songFilename;
    }

    public String getSongName() {
        return _songName;
    }

    public String getSongSubName() {
        return _songSubName;
    }

    public String getVersion() {
        return _version;
    }

    public void setCustomData(BeatmapCustomData customData) {
        this._customData = customData;
    }

    public void setBeatsPerMinute(float beatsPerMinute) {
        this._beatsPerMinute = beatsPerMinute;
    }

    public void setCoverImageFilename(String coverImageFilename) {
        this._coverImageFilename = coverImageFilename;
    }

    public void setDifficultyBeatmapSets(ArrayList<DifficultySet> difficultyBeatmapSets) {
        this._difficultyBeatmapSets = difficultyBeatmapSets;
    }

    public void setEnvironmentName(String environmentName) {
        this._environmentName = environmentName;
    }

    public void setLevelAuthorName(String levelAuthorName) {
        this._levelAuthorName = levelAuthorName;
    }

    public void setPreviewDuration(float previewDuration) {
        this._previewDuration = previewDuration;
    }

    public void setPreviewStartTime(float previewStartTime) {
        this._previewStartTime = previewStartTime;
    }

    public void setShuffle(float shuffle) {
        this._shuffle = shuffle;
    }

    public void setShufflePeriod(float shufflePeriod) {
        this._shufflePeriod = shufflePeriod;
    }

    public void setSongAuthorName(String songAuthorName) {
        this._songAuthorName = songAuthorName;
    }

    public void setSongFilename(String songFilename) {
        this._songFilename = songFilename;
    }

    public void setSongName(String songName) {
        this._songName = songName;
    }

    public void setSongSubName(String songSubName) {
        this._songSubName = songSubName;
    }

    public void setSongTimeOffset(float songTimeOffset) {
        this._songTimeOffset = songTimeOffset;
    }

    public void setVersion(String version) {
        this._version = version;
    }
}
