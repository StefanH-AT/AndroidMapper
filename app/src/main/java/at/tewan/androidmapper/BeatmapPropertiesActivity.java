package at.tewan.androidmapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.beatmap.info.Info;

public class BeatmapPropertiesActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Beatmap Properties";

    private String beatmapHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        beatmapHash = intent.getStringExtra("beatmap_hash");
        Log.i(LOG_TAG, "Beatmap hash: " + beatmapHash);

        setContentView(R.layout.content_beatmap_properties);

        Info info = Beatmaps.readStoredBeatmap(beatmapHash);

        TextView songName = findViewById(R.id.songName);
        TextView songSubName = findViewById(R.id.songSubName);
        TextView songAuthor = findViewById(R.id.songAuthor);
        TextView levelAuthor = findViewById(R.id.levelAuthor);
        TextView bpm = findViewById(R.id.bpm);

        songName.setText(info.getSongName());
        songSubName.setText(info.getSongSubName());
        songAuthor.setText(info.getSongAuthorName());
        levelAuthor.setText(info.getLevelAuthorName());
        bpm.setText(String.valueOf(info.getBeatsPerMinute()));

    }

}
