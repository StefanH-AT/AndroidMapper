package at.tewan.androidmapper.beatmap.difficulty;

import java.util.ArrayList;

public class Difficulty {

    private String _version = "2.0.0"; // Default version number
    private ArrayList<DifficultyEvent> _events = new ArrayList<>();
    private ArrayList<DifficultyObstacle> _obstacles = new ArrayList<>();
    private ArrayList<DifficultyNote> _notes = new ArrayList<>();
    private ArrayList<DifficultyBookmark> _bookmarks = new ArrayList<>();

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

    public ArrayList<DifficultyBookmark> getBookmarks() {
        return _bookmarks;
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

    public void setBookmarks(ArrayList<DifficultyBookmark> bookmarks) {
        this._bookmarks = bookmarks;
    }
}
