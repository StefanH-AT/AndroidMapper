package at.tewan.androidmapper.beatmap.enums;

/**
 * @author Stefan Heinz
 *
 * Enumeration of all recognised beatmap characteristics (game modes).
 * Enum values include characteristic name and which mod it is added by.
 */
public enum Characteristics {

    STANDARD("Standard", "Base Game"),
    NO_ARROWS("NoArrows", "Base Game"),
    ONE_SABER("OneSaber", "Base Game"),
    LAWLESS("Lawless", "SongCore"),
    LIGHTSHOW("Lightshow", "SongCore");



    private String name;
    private String mod;

    Characteristics(String name, String mod) {
        this.name = name;
        this.mod = mod;
    }

    public String getName() {
        return name;
    }

    public String getMod() {
        return mod;
    }
}
