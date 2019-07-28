package at.tewan.androidmapper.editor;

import android.util.Log;

import processing.core.PApplet;
import processing.event.MouseEvent;

import static at.tewan.androidmapper.editor.SharedSketchData.*;

public class TrackSketch extends PApplet {

    private static final String LOG_TAG = "Track";

    private int padding;
    private int laneWidth;
    private int baselineY;
    private int subBeatAmount = 4;

    private float bpm;

    public TrackSketch() {

        bpm = info.getBeatsPerMinute();

    }

    @Override
    public void setup() {

        padding = (int) (width * 0.1);
        laneWidth = (width - padding * 2) / lanes;
        baselineY = height - padding * 3;

        Log.i(LOG_TAG, "Canvas Size: " + width + "x" + height);
        Log.i(LOG_TAG, "Lanes: " + lanes);
        Log.i(LOG_TAG, "BPM: " + bpm);
        Log.i(LOG_TAG, "Padding: " + padding);
        Log.i(LOG_TAG, "Lane width: " + laneWidth);
        Log.i(LOG_TAG, "Baseline Y: " + baselineY);

    }

    @Override
    public void draw() {

        background(0);

        // Drawn absolute
        drawVerticalLines();
        drawBaseline();


        translate(0, currentBeat * laneWidth);

        // Stuff that's drawn relative to the current progress
        drawBeats();
        drawNotes();

    }

    private void drawVerticalLines() {

        strokeWeight(1);

        for(int i = 0; i <= lanes; i++) {

            int x = padding + laneWidth * i;

            strokeWeight(1);
            stroke(255);
            line(x, 0, x, height);
        }

    }

    private void drawBaseline() {
        strokeWeight(5);
        line((float) padding / 2, baselineY, width, baselineY);     // Baseline line
        int arrowHeight = 25;

        fill(255);
        triangle(
                0,
                baselineY - arrowHeight,
                0,
                baselineY + arrowHeight,
                (float) padding / 2,
                baselineY);                                     // Baseline Arrow
    }

    private void drawNotes() {

    }

    private void drawBeats() {

        for(float i = 0; i < totalBeats; i += 1 / (float) subBeatAmount) {

            float y = baselineY - i * laneWidth * subBeatAmount;

            if(i == (int) i) { // If i has no decimal places (If whole beat)

                strokeWeight(3);
                textAlign(CENTER);

                float textSize = 24;

                textSize(textSize);
                text((int) i + "", (float) padding / 2, y - textSize / 2);

            } else
                strokeWeight(1);

            line(padding, y, width - padding, y);



        }

    }

    private MouseEvent dragOrigin;

    @Override
    public void mousePressed(MouseEvent event) {
        dragOrigin = event;
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        currentBeat = constrain(Math.round(currentBeat), 0, totalBeats);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        int ox = dragOrigin.getX();
        int oy = dragOrigin.getY();

        int dx = event.getX();
        int dy = event.getY();

        line(ox, oy, dx, dy);

        // Scroll
        if(dx <= padding || dx > width - padding) { // Only scroll if you drag outside the track area

            float scrollAmount = (float) (dy - pmouseY) / laneWidth;

            currentBeat += scrollAmount;

        }
    }

}
