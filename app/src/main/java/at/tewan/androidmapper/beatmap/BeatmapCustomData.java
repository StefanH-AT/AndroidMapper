package at.tewan.androidmapper.beatmap;

import java.util.ArrayList;

public class BeatmapCustomData {

    private ArrayList<Contributer> _contributers;
    private String _customEnvironment;
    private String _customEnvironmentHash;

    public BeatmapCustomData(ArrayList<Contributer> contributers, String customEnvironment, String customEnvironmentHash) {
        this._contributers = contributers;
        this._customEnvironment = customEnvironment;
        this._customEnvironmentHash = customEnvironmentHash;
    }

    public ArrayList<Contributer> getContributers() {
        return _contributers;
    }

    public String getCustomEnvironment() {
        return _customEnvironment;
    }

    public String getCustomEnvironmentHash() {
        return _customEnvironmentHash;
    }

    public void setContributers(ArrayList<Contributer> contributers) {
        this._contributers = contributers;
    }

    public void setCustomEnvironment(String customEnvironment) {
        this._customEnvironment = customEnvironment;
    }

    public void setCustomEnvironmentHash(String customEnvironmentHash) {
        this._customEnvironmentHash = customEnvironmentHash;
    }
}
