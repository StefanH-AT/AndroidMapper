package at.tewan.androidmapper.editor;

import android.util.Log;

import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import at.tewan.androidmapper.beatmap.enums.CutDirection;
import at.tewan.androidmapper.preferences.Preferences;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

import static at.tewan.androidmapper.editor.SharedSketchData.*;

/**
 * This is one of the processing sketches that make up the functional part of the mapper.
 * The Track Editor is a top down view of the beatmap and is primarily used to have an overview of the map
 * and to place walls.
 *
 */
public class TrackSketch extends PApplet {

    private static final String LOG_TAG = "Track";

    private final int cameraDistance = 100;
    private final float cameraPanSensitivity = 0.002f;

    private final int trackWidth = 100;
    private final int baseHeight = 20;

    private final int padding = (int) (trackWidth * 0.1);
    private final int laneWidth = (trackWidth - padding * 2) / lanes;
    private final int baselineY = padding * 3;
    private final int beatHeight = laneWidth * lanes;
    private final int trackLength = (int) timeAsBeat(songDuration) * beatHeight;
    private final int objectSize = trackWidth / (lanes * 2);

    private static final float CAMERA_DEFAULT_ROTATION_Z = (float) 0;
    private PVector cameraOrigin = new PVector(0, 1, 0.2f);

    private float fov = (int) PI / 2;
    private int farZ = 10000;

    private MouseEvent dragOrigin;
    private int originLane;
    private float originBeat;

    @Override
    public void settings() {
        size(-1, -1, P3D);
    }

    @Override
    public void setup() {

        Log.i(LOG_TAG, "Canvas Size: " + width + "x" + height);
        Log.i(LOG_TAG, "Lanes: " + lanes);
        Log.i(LOG_TAG, "BPM: " + bpm);
        Log.i(LOG_TAG, "Padding: " + padding);
        Log.i(LOG_TAG, "Lane width: " + laneWidth);
        Log.i(LOG_TAG, "Baseline Y: " + baselineY);
        Log.i(LOG_TAG, "Sub Beat Amount: " + subBeatAmount);

        rotateCameraZ(CAMERA_DEFAULT_ROTATION_Z);

    }

    @Override
    public void draw() {

        background(backgroundColor);

        camera(cameraOrigin.x * cameraDistance, cameraOrigin.y * cameraDistance, cameraOrigin.z * cameraDistance, 0, getBeatCoordinate(currentBeat), cameraOrigin.z, 0, 0, -1);
        perspective(fov, (float) width / height, 1, farZ);

        // Drawn absolute
        drawBase();
        drawBaseline();

        // Translate to current progress
        //translate(0, currentBeat * beatHeight, 0);

        // Stuff that's drawn relative to the current progress
        drawNotesAndBombs();
        drawBeats();

        if(Preferences.isDebug()) {
            drawPivot();
        }

    }

    private void drawBase() {

        pushMatrix();
        translate(0, 0, -baseHeight / 2);
        noStroke();
        fill(baseColor);
        box(trackWidth, trackLength, baseHeight);
        popMatrix();
        /*
        for(int i = 0; i < lanes; i++) {

            pushMatrix();
            float x = getLaneCoordinate(i);

            strokeWeight(1);
            stroke(debugColorR, debugColorG, debugColorB);
            translate(-trackWidth * 0.5f + x, 0, 0);
            line(x, 0, x, -height);
            popMatrix();

        }*/

    }

    private void drawBaseline() {

        pushMatrix();
        strokeWeight(5);
        stroke(strokeColor);
        translate(-trackWidth / 2, baselineY, 1);
        line(0, 0, trackWidth, 0);     // Baseline line
        int arrowHeight = 5;

        fill(strokeColor);
        triangle(0, -arrowHeight,0, arrowHeight, (float) padding / 2, 0);  // Baseline Arrow

        popMatrix();
    }

    private void drawBomb(DifficultyNote bomb) {
        float x = getLaneCoordinate(bomb.getLineIndex() + 1);
        float y = getBeatCoordinate(bomb.getTime());

        pushMatrix();
        stroke(strokeColor);
        strokeWeight(1);
        fill(BOMB_COLOR);
        ellipse(x, y, objectSize, objectSize);
        popMatrix();
    }

    private void drawNote(DifficultyNote note) {

        if(typeAsColor(note.getType()) == RED)
            fill(colorLeftR, colorLeftG, colorLeftB);
        else
            fill(colorRightR, colorRightG, colorRightB);

        int layer = Math.abs(2 - note.getLineLayer());

        pushMatrix();
        translate(getLaneCoordinate(note.getLineIndex() + 1) - (float) trackWidth / 2,
                getBeatCoordinate(note.getTime()),
                (float) (layer * objectSize + objectSize * 0.5 + layer * 5));
        rotateY(CutDirection.getType(note.getCutDirection()).getRadians());
        stroke(strokeColor);
        box(objectSize);

        translate(0, objectSize / 2 + 1, 0);
        rotateX(PI / 2);

        fill(strokeColor);
        int s = objectSize / 3; // Size
        if(CutDirection.getType(note.getCutDirection()) == CutDirection.ANY) {
            ellipse(0, 0, s, s);
        } else {
            triangle(0, 0, -s, s, -s, -s);
        }

        noFill();
        popMatrix();
    }

    private void drawNotesAndBombs() {

        // Avoid ConcurrentModificationException
        DifficultyNote[] notes = getNotes().toArray(new DifficultyNote[0]);

        for(DifficultyNote note : notes) {

            if(note.getType() == DifficultyNote.TYPE_BOMB)
                drawBomb(note);
            else
                drawNote(note);
        }

    }

    private void drawBeats() {

        for(float i = 0; i < totalBeats; i += 1 / (float) subBeatAmount) {

            float y = baselineY - i * beatHeight;

            pushMatrix();
            translate(-trackWidth / 2, getBeatCoordinate(i));

            if(i == (int) i) { // If i has no decimal places (If whole beat)

                strokeWeight(3);
                textAlign(CENTER);

                float textSize = 4;

                textSize(textSize);
                text((int) i + "", 0, 0);

            } else
                strokeWeight(1);


            line(0, 0, trackWidth, 0);
            popMatrix();


        }

    }

    private void drawPivot() {
        pushMatrix();

        noStroke();

        fill(200, 0, 0);

        pushMatrix();
        translate(4, 0 ,0);
        box(10, 1.5f, 1.5f);
        popMatrix();

        fill(0, 200, 0);
        pushMatrix();
        translate(0, 4, 0);
        box(1.5f, 10, 1.5f);
        popMatrix();

        fill(0, 0, 200);
        pushMatrix();
        translate(0, 0, 4);
        box(1.5f, 1.5f, 10);
        popMatrix();

        popMatrix();
    }

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
        currentBeat = constrain(Math.round(currentBeat * subBeatAmount), 0, totalBeats * subBeatAmount) / subBeatAmount;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        int ox = dragOrigin.getX();
        int oy = dragOrigin.getY();

        int dx = event.getX();
        int dy = event.getY();

        // Scroll
        if(!(isMouseInTrack(ox) || isMouseInTrack(dx))) { // Only scroll if you drag outside the track area

            float scrollAmount = (float) (dy - pmouseY) / (float) (beatHeight);

            currentBeat += scrollAmount;

        }/* else { // Note placing preview

            stroke(strokeColor);
            int notePlaceDirectionRadius = 240;

            if(dist(ox, oy, dx, dy) > notePlaceDirectionRadius) {

            }
        }*/

        // Camera panning
        rotateCameraZ((mouseX - pmouseX) * cameraPanSensitivity);


    }

    private float getDragAngle(int x, int y) {
        float angle = atan2(y - dragOrigin.getY(), x - dragOrigin.getX());

        // Fixing the angle
        if(angle < 0) {
            angle += TWO_PI;
        }

        return angle;
    }

    /*
     * CAUTION! Vector rotation matrices ahead.
     */

    private void rotateCameraX(float angle) {
        float ny = (cameraOrigin.y * cos(angle) + cameraOrigin.z * -sin(angle));
        float nz = (cameraOrigin.y * sin(angle) + cameraOrigin.z * cos(angle));
        cameraOrigin.y = ny;
        cameraOrigin.z = nz;
    }

    private void rotateCameraY(float angle) {
        float nx = (cameraOrigin.x * cos(angle) + cameraOrigin.z * sin(angle));
        float nz = (cameraOrigin.x * -sin(angle) + cameraOrigin.z * cos(angle));
        cameraOrigin.x = nx;
        cameraOrigin.z = nz;
    }

    private void rotateCameraZ(float angle) {
        float nx = (cameraOrigin.x * cos(angle) + cameraOrigin.y * -sin(angle));
        float ny = (cameraOrigin.x * sin(angle) + cameraOrigin.y * cos(angle));
        cameraOrigin.x = nx;
        cameraOrigin.y = ny;
    }

    private boolean isMouseInTrack(int x) {
        return x > padding && x < width - padding;
    }

    private float getBeatCoordinate(float beat) {
        return (beat * -beatHeight + baselineY);
    }

    private float getLaneCoordinate(int lane) {
        return lane * laneWidth;
    }

}
