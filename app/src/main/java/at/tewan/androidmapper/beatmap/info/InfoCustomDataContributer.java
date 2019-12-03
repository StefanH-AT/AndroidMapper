package at.tewan.androidmapper.beatmap.info;

/**
 * @author Stefan Heinz
 */
public class InfoCustomDataContributer {

    private String _role;
    private String _name;
    private String _iconPath;

    public InfoCustomDataContributer(String role, String name, String iconPath) {
        this._role = role;
        this._name = name;
        this._iconPath = iconPath;
    }

    public String get_iconPath() {
        return _iconPath;
    }

    public String get_name() {
        return _name;
    }

    public String get_role() {
        return _role;
    }

    public void set_iconPath(String iconPath) {
        this._iconPath = iconPath;
    }

    public void set_name(String name) {
        this._name = name;
    }

    public void set_role(String _role) {
        this._role = _role;
    }
}
