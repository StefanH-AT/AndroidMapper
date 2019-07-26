package at.tewan.androidmapper.beatmap.enums;

public enum Capabilities {

    MAPPING_EXTENSIONS("Mapping Extensions", "Mapping Extensions"),
    CHROMA("Chroma", "Chroma"),
    CHROMA_LIGHTING("Chroma Lighting Events", "Chroma"),
    CHROMA_SPECIAL("Chroma Special Events", "Chroma"),;

    private String name;
    private String mod;

    Capabilities(String name, String mod) {
        this.name = name;
        this.mod = mod;
    }


    public String getMod() {
        return mod;
    }

    public String getName() {
        return name;
    }
}