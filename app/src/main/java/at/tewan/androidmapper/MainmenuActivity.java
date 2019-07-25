package at.tewan.androidmapper;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.Beatmap;
import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.mainmenu.BeatmapListAdapter;

public class MainmenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Beatmaps.init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(newBeatmapListener);

        ListView beatmapList = findViewById(R.id.beatmapList);

        ArrayList<Beatmap> beatmaps = new ArrayList<>();

        Beatmap debugBeatmap = new Beatmap();
        debugBeatmap.setSongName("Debug beatmap!");

        Beatmap debugBeatmap2 = new Beatmap();
        debugBeatmap2.setSongName("Debug beatmap2 !");

        Beatmap debugBeatmap3 = new Beatmap();
        debugBeatmap3.setSongName("Debug beatmap 3!");

        beatmaps.add(debugBeatmap);
        beatmaps.add(debugBeatmap2);
        beatmaps.add(debugBeatmap3);

        beatmapList.setAdapter(new BeatmapListAdapter(getApplicationContext(), beatmaps));

        Beatmaps.saveBeatmap(debugBeatmap);
    }

    View.OnClickListener newBeatmapListener = view -> {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("New beatmap");
        dialogBuilder.setView(R.layout.dialog_new_beat_map);

        dialogBuilder.show();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
