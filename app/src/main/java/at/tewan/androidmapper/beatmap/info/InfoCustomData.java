package at.tewan.androidmapper.beatmap.info;

import java.util.ArrayList;

public class InfoCustomData {

    private ArrayList<InfoCustomDataContributer> _contributers;
    private String _customEnvironment;
    private String _customEnvironmentHash;

    public InfoCustomData(ArrayList<InfoCustomDataContributer> contributers, String customEnvironment, String customEnvironmentHash) {
        this._contributers = contributers;
        this._customEnvironment = customEnvironment;
        this._customEnvironmentHash = customEnvironmentHash;
    }

    public ArrayList<InfoCustomDataContributer> getContributers() {
        return _contributers;
    }

    public String getCustomEnvironment() {
        return _customEnvironment;
    }

    public String getCustomEnvironmentHash() {
        return _customEnvironmentHash;
    }

    public void setContributers(ArrayList<InfoCustomDataContributer> contributers) {
        this._contributers = contributers;
    }

    public void setCustomEnvironment(String customEnvironment) {
        this._customEnvironment = customEnvironment;
    }

    public void setCustomEnvironmentHash(String customEnvironmentHash) {
        this._customEnvironmentHash = customEnvironmentHash;
    }
}
