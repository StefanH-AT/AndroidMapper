package at.tewan.androidmapper.editor;

import android.util.Log;

import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import processing.core.PApplet;
import processing.event.MouseEvent;

import static at.tewan.androidmapper.editor.SharedSketchData.*;

public class TrackSketch extends PApplet {

    private static final String LOG_TAG = "Track";

    private int padding;
    private int laneWidth;
    private int baselineY;


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

        background(backgroundColor);

        // Drawn absolute

        textSize(38);
        fill(255);
        text("CB: " + currentBeat, 126, height - 100);

        drawVerticalLines();
        drawBaseline();

        textAlign(RIGHT);
        text("fps: " + (int) frameRate, width - padding, 100);
        textAlign(LEFT);

        translate(0, currentBeat * subBeatAmount * laneWidth);

        // Stuff that's drawn relative to the current progress
        drawBeats();
        drawNotes();

        if(mousePressed)
            mouseDown();

    }

    private void drawVerticalLines() {

        strokeWeight(1);

        for(int i = 0; i <= lanes; i++) {

            int x = padding + laneWidth * i;

            strokeWeight(1);
            stroke(strokeColor);
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

        for(DifficultyNote note : getNotes()) {

            rectMode(RADIUS);

            if(typeAsColor(note.getType()) == RED)
                fill(200, 0, 0);
            else
                fill(0, 120, 200);

            rect(getLaneCoordinate(note.getLineIndex() + 1), getBeatCoordinate(timeAsBeat(note.getTime())), 40, 40);
            noFill();

        }

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
    private int originLane;
    private float originBeat;

    @Override
    public void mousePressed(MouseEvent event) {
        dragOrigin = event;

        if(isMouseInTrack(event.getX())) {
            originLane = (int) Math.floor((float) (event.getX() + padding) / laneWidth);
            originBeat = (Math.round((float) (baselineY - event.getY()) / laneWidth) + currentBeat);
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        currentBeat = constrain(Math.round(currentBeat * subBeatAmount), 0, totalBeats) / subBeatAmount;
/*
        if(isMouseInTrack(event.getX())) {

            // Check for overlapping notes
            for(DifficultyNote note : notes) {
                if(note.getLineIndex() == originLane && note.getTime() == beatAsTime(originBeat))
                    return;
            }

            // Note placing
            DifficultyNote note = new DifficultyNote(beatAsTime(originBeat), colorAsType(selectedColor), originLane, 0, CutDirection.ANY.ordinal());
            notes.add(note);

            Log.i(LOG_TAG, "Placed note: " + note.toString());

        }*/
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        int ox = dragOrigin.getX();
        int oy = dragOrigin.getY();

        int dx = event.getX();
        int dy = event.getY();

        // Scroll
        if(!(isMouseInTrack(ox) || isMouseInTrack(dx))) { // Only scroll if you drag outside the track area

            float scrollAmount = (float) (dy - pmouseY) / laneWidth / subBeatAmount;

            currentBeat += scrollAmount;

        } else { // Note placing preview

            stroke(255);
            int notePlaceDirectionRadius = 240;
            ellipse(getLaneCoordinate(originLane), getBeatCoordinate(originBeat), notePlaceDirectionRadius, notePlaceDirectionRadius);

            if(dist(ox, oy, dx, dy) > notePlaceDirectionRadius) {

            }
        }
    }

    private void mouseDown() {
        if(isMouseInTrack(mouseX)) {
            /*
            int ox = dragOrigin.getX();
            int oy = dragOrigin.getY();

            // XY snapped to grid
            int sx = originLane * laneWidth;
            int sy = (int) (originBeat * subBeatAmount) * -laneWidth + baselineY;

            noFill();
            stroke(255);
            ellipse(sx, sy, notePlaceDirectionRadius, notePlaceDirectionRadius);

            if(dist(sx, sy, mouseX, mouseY) > notePlaceDirectionRadius) {

                float angle = getDragAngle(sx, sy);
                // Snapping angle to 45 degrees
                angle = Math.round(angle / PI / 4) * PI / 4;

                text("fa: " + Math.toDegrees(angle) + ", a: " + Math.toDegrees(getDragAngle(sx, sy)), 300, 200);


                fill(0, 140, 140);

            }
*/
        }
    }

    private float getDragAngle(int x, int y) {
        float angle = atan2(y - dragOrigin.getY(), x - dragOrigin.getX());

        // Fixing the angle
        if(angle < 0) {
            angle += TWO_PI;
        }

        return angle;
    }

    private boolean isMouseInTrack(int x) {
        return x > padding && x < width - padding;
    }

    private float getBeatCoordinate(float beat) {
        return (beat * subBeatAmount * -laneWidth + baselineY);
    }

    private float getLaneCoordinate(int lane) {
        return lane * laneWidth;
    }

}
