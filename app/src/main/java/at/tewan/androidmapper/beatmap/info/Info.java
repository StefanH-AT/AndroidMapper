package at.tewan.androidmapper.beatmap.info;


import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.enums.Environments;

/**
 * The JSON Data model for info.dat files. Also used internally.
 * Definition: https://github.com/Kylemc1413/SongCore#infodat-explanation
 * */
public class Info {

    private String _version = "2.0";
    private String _songName;
    private String _songSubName = "Created with AndroidMapper";
    private String _songAuthorName;
    private String _levelAuthorName;
    private float _beatsPerMinute = 150;
    private float _songTimeOffset = 0;
    private float _shuffle = 0;
    private float _shufflePeriod = 0.5f;
    private float _previewStartTime = 12;
    private float _previewDuration = 10;
    private String _songFilename = "song.egg";
    private String _coverImageFilename = "cover.jpg";
    private String _environmentName = Environments.DefaultEnvironment.name();
    private InfoCustomData _customData;
    private ArrayList<InfoDifficultySet> _difficultyBeatmapSets = new ArrayList<>();

    public ArrayList<InfoDifficultySet> getDifficultyBeatmapSets() {
        return _difficultyBeatmapSets;
    }

    public InfoCustomData getCustomData() {
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

    public void setCustomData(InfoCustomData customData) {
        this._customData = customData;
    }

    public void setBeatsPerMinute(float beatsPerMinute) {
        this._beatsPerMinute = beatsPerMinute;
    }

    public void setCoverImageFilename(String coverImageFilename) {
        this._coverImageFilename = coverImageFilename;
    }

    public void setDifficultyBeatmapSets(ArrayList<InfoDifficultySet> difficultyBeatmapSets) {
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
