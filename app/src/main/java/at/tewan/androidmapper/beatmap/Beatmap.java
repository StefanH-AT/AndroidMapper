package at.tewan.androidmapper.beatmap;

import java.util.HashMap;

import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.enums.Characteristics;
import at.tewan.androidmapper.beatmap.info.Info;

/**
 * @author Stefan Heinz
 */
public class Beatmap {

    private Info info;
    private HashMap<Characteristics, Difficulty> difficulties;

    public Info getInfo() {
        return info;
    }

    public HashMap<Characteristics, Difficulty> getDifficulties() {
        return difficulties;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public void setDifficulties(HashMap<Characteristics, Difficulty> difficulties) {
        this.difficulties = difficulties;
    }
}
