package at.tewan.androidmapper.editor;

import android.util.Log;

import processing.core.PApplet;

import static at.tewan.androidmapper.editor.SharedSketchData.*;

public class InspectionSketch extends PApplet {

    private static final String LOG_TAG = "Inspection";

    @Override
    public void setup() {
        Log.i(LOG_TAG, "Canvas Size: " + width + "x" + height);
    }

    @Override
    public void draw() {
        background(0);

        fill(255);
        textAlign(CENTER);

        textSize(40);
        text("Inspection Canvas", width / 2, height / 2);

        // Frame
        stroke(255);
        noFill();
        rect(1, 1, width - 2, height - 2);

        // Grid
        int xgap = width / lanes;
        int ygap = height / rows;

        for(int x = xgap; x < width; x += xgap) {
            line(x, 0, x, height);
        }

        for(int y = ygap; y < height; y += ygap) {
            line(0, y, width, y);
        }
    }
}
