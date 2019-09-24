package at.tewan.androidmapper.editor;

import android.util.Log;

import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import at.tewan.androidmapper.beatmap.enums.CutDirection;
import processing.core.PApplet;
import processing.event.MouseEvent;

import static at.tewan.androidmapper.editor.SharedSketchData.*;

/**
 * This is one of the processing sketches that make up the functional part of the editor.
 * The inspection sketch provides a vertical slice of the current beat, your cursor is placed on (Like a front view)
 *
 * It is used to place notes, bombs and to visualize the map from the front
 */
public class InspectionSketch extends PApplet {

    private static final String LOG_TAG = "Inspection";

    private static final int OBJECT_SIZE = 50;

    private int laneWidth;
    private int halfLaneWidth;
    private int rowHeight;
    private int halfRowHeight;

    @Override
    public void setup() {
        Log.i(LOG_TAG, "Canvas Size: " + width + "x" + height);

        laneWidth = width / lanes;
        rowHeight = height / rows;

        halfLaneWidth = laneWidth / 2;
        halfRowHeight = rowHeight / 2;
    }

    @Override
    public void draw() {
        background(backgroundColor);


        // Grid
        stroke(strokeColor);
        strokeWeight(2);

        noFill();
        rectMode(CORNERS);
        rect(1, 1, width - 2, height - 2);

        for(int x = 0; x < width; x += laneWidth) {
            line(x, 0, x, height);
        }

        for(int y = 0; y < height; y += rowHeight) {
            line(0, y, width, y);
        }

        if(mousePressed) {
            mouseDown();
        }

        drawNotesAndBombs();
    }

    private void drawNote(DifficultyNote note) {
        if(typeAsColor(note.getType()) == RED)
            fill(colorLeftR, colorLeftG, colorLeftB);
        else
            fill(colorRightR, colorRightG, colorRightB);

        rectMode(RADIUS);

        int x = getLaneCoordinate(note.getLineIndex());
        int y = getRowCoordinate(note.getLineLayer());

        CutDirection direction = CutDirection.getType(note.getCutDirection());

        pushMatrix();

        translate(x, y);
        rotate(radians(direction.getDegrees()));

        rect(0, 0, OBJECT_SIZE, OBJECT_SIZE);

        fill(strokeColor);

        int s = OBJECT_SIZE / 2; // Size
        if(direction == CutDirection.ANY) {
            ellipse(0, 0, s, s);
        } else {
            triangle(0, 0, -s, s, -s, -s);
        }

        popMatrix();
    }

    private void drawBomb(DifficultyNote bomb) {
        int x = getLaneCoordinate(bomb.getLineIndex());
        int y = getRowCoordinate(bomb.getLineLayer());

        stroke(strokeColor);
        fill(BOMB_COLOR);
        ellipse(x, y, OBJECT_SIZE, OBJECT_SIZE);
    }

    private void drawNotesAndBombs() {

        // Avoid ConcurrentModificationException
        DifficultyNote[] notes = getNotes().toArray(new DifficultyNote[0]);

        for(DifficultyNote note : notes) {

            if(note.getTime() == currentBeat) {

                if(note.getType() == DifficultyNote.TYPE_BOMB)
                    drawBomb(note);
                else
                    drawNote(note);

            }
        }

    }

    private int originLane;
    private int originRow;

    private void mouseDown() {
        rectMode(RADIUS);

        if(toolMode == ToolMode.NOTE) {

            if (selectedColor == RED)
                fill(200, 0, 0);
            else
                fill(0, 120, 200);

            rectMode(RADIUS);


            pushMatrix();

            translate(getLaneCoordinate(originLane), getRowCoordinate(originRow));
            rotate(getDragAngle(mouseX, mouseY));

            rect(0, 0, OBJECT_SIZE, OBJECT_SIZE);

            int s = OBJECT_SIZE / 2;
            triangle(0, 0, -s, s, -s, -s);

            popMatrix();
        } else if(toolMode == ToolMode.BOMB) {
            DifficultyNote bomb = new DifficultyNote(currentBeat, DifficultyNote.TYPE_BOMB, originLane, originRow, 0);
            addNote(bomb);
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {

        if(toolMode == ToolMode.NOTE) {

            if (event.getX() < 0) return; // Fix cross canvas events

            CutDirection direction;

            int dotNoteRadius = 50;
            if (dist(event.getX(), event.getY(), getLaneCoordinate(originLane), getRowCoordinate(originRow)) > dotNoteRadius) {
                float angle = (float) Math.toDegrees(getDragAngle(event.getX(), event.getY()));

                angle = Math.round(map(angle, 0, 360, 0, 8)) * 45;

                direction = CutDirection.getDirection(angle);
            } else {
                direction = CutDirection.ANY;
            }

            DifficultyNote note = new DifficultyNote(currentBeat, colorAsType(selectedColor), originLane, originRow, direction.ordinal());

            addNote(note);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getX() < 0) return; // Fix cross canvas events

        originLane = e.getX() / laneWidth;
        originRow = e.getY() / rowHeight;

        if(toolMode == ToolMode.REMOVE) {

            // Loop runs backwards because items are being removed
            for(DifficultyNote note : getNotes()) {
                if(note.getTime() == currentBeat && note.getLineLayer() == originRow && note.getLineIndex() == originLane) {
                    disposeNote(note);
                    break; // Better break for loop to avoid array iteration crashes
                }
            }

        }

    }

    private int getLaneCoordinate(int lane) {
        return lane * laneWidth + halfLaneWidth;
    }

    private int getRowCoordinate(int row) {
        return row * rowHeight + halfRowHeight;
    }

    private float getDragAngle(int x, int y) {
        float angle = atan2(y - getRowCoordinate(originRow), x - getLaneCoordinate(originLane));

        // Fixing the angle
        if(angle < 0) {
            angle += TWO_PI;
        }

        return angle;
    }
}
