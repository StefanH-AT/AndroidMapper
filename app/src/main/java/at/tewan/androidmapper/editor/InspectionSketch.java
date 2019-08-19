package at.tewan.androidmapper.editor;

import android.util.Log;

import at.tewan.androidmapper.beatmap.difficulty.DifficultyNote;
import at.tewan.androidmapper.beatmap.enums.CutDirection;
import processing.core.PApplet;
import processing.event.MouseEvent;

import static at.tewan.androidmapper.editor.SharedSketchData.*;

public class InspectionSketch extends PApplet {

    private static final String LOG_TAG = "Inspection";

    private int laneWidth;
    private int halfLaneWidth;
    private int rowHeight;
    private int halfRowHeight;

    private int dotNoteRadius = 50;

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
        background(0);


        // Grid
        stroke(255);
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

        drawNotes();
    }

    private void drawNotes() {

        for(DifficultyNote note : notes) {

            if(timeAsBeat(note.getTime()) == currentBeat) {

                if(typeAsColor(note.getType()) == RED)
                    fill(200, 0, 0);
                else
                    fill(0, 120, 200);

                rectMode(RADIUS);

                int x = getLaneCoordinate(note.getLineIndex());
                int y = getRowCoordinate(note.getLineLayer());

                CutDirection direction = CutDirection.getType(note.getCutDirection());

                pushMatrix();

                translate(x, y);
                rotate(radians(direction.getDegrees()));

                rect(0, 0, 40, 40);

                fill(255);

                int s = 16; // Size
                if(direction == CutDirection.ANY) {
                    ellipse(0, 0, s, s);
                } else {
                    triangle(0, 0, -s, s, -s, -s);
                }

                popMatrix();
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

            rect(0, 0, 50, 50);

            int s = 10;
            triangle(0, 0, -s, s, -s, -s);

            popMatrix();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {

        if(toolMode == ToolMode.NOTE) {

            if (event.getX() < 0) return; // Fix cross canvas events

            CutDirection direction;

            if (dist(event.getX(), event.getY(), getLaneCoordinate(originLane), getRowCoordinate(originRow)) > dotNoteRadius) {
                float angle = (float) Math.toDegrees(getDragAngle(event.getX(), event.getY()));

                angle = Math.round(map(angle, 0, 360, 0, 8)) * 45;

                direction = CutDirection.getDirection(angle);
            } else {
                direction = CutDirection.ANY;
            }

            DifficultyNote note = new DifficultyNote(beatAsTime(currentBeat), colorAsType(selectedColor), originLane, originRow, direction.ordinal());

            notes.add(note);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getX() < 0) return; // Fix cross canvas events

        originLane = e.getX() / laneWidth;
        originRow = e.getY() / rowHeight;

        if(toolMode == ToolMode.REMOVE) {

            for(DifficultyNote note : notes) {
                if(note.getTime() == beatAsTime(currentBeat) && note.getLineLayer() == originRow && note.getLineIndex() == originLane) {
                    notes.remove(note);
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
