package at.tewan.androidmapper.beatmap;

/**
 * The color used for the left and right sabers as part of @see at.tewan.androidmapper.beatmap.DifficultyCustomData
 *
 * Range 0-1 !!
 */
public class DifficultyColor {

    private float r;
    private float g;
    private float b;

    public DifficultyColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getB() {
        return b;
    }

    public float getG() {
        return g;
    }

    public float getR() {
        return r;
    }

    public void setB(float b) {
        this.b = b;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setR(float r) {
        this.r = r;
    }
}
