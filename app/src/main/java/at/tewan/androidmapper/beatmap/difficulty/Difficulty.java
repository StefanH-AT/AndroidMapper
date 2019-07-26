package at.tewan.androidmapper.beatmap.difficulty;

import java.util.ArrayList;

public class Difficulty {

    private String _version;
    private ArrayList<DifficultyEvent> _events;
    private ArrayList<DifficultyObstacle> _obstacles;
    private ArrayList<DifficultyNote> _notes;

    public String getVersion() {
        return _version;
    }

    public ArrayList<DifficultyEvent> getEvents() {
        return _events;
    }

    public ArrayList<DifficultyNote> getNotes() {
        return _notes;
    }

    public ArrayList<DifficultyObstacle> getObstacles() {
        return _obstacles;
    }

    public void setVersion(String version) {
        this._version = version;
    }

    public void setEvents(ArrayList<DifficultyEvent> events) {
        this._events = events;
    }

    public void setNotes(ArrayList<DifficultyNote> notes) {
        this._notes = notes;
    }

    public void setObstacles(ArrayList<DifficultyObstacle> obstacles) {
        this._obstacles = obstacles;
    }
}
