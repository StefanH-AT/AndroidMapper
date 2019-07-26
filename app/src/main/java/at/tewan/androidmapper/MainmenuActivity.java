package at.tewan.androidmapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.mainmenu.BeatmapListAdapter;

public class MainmenuActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Mainmenu";

    private ListView beatmapList;
    private ArrayList<Info> beatmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(newBeatmapListener);



        Beatmaps.init();


        beatmapList = findViewById(R.id.beatmapList);

        beatmaps = Beatmaps.readStoredBeatmapInfos(this);


        beatmapList.setAdapter(new BeatmapListAdapter(getApplicationContext(), beatmaps));

        beatmapList.setOnItemClickListener((parent, view, position, id) -> {

        });

        beatmapList.setOnItemLongClickListener((parent, view, position, id) -> {

            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.beatmap_index_context, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {

                if(item.getItemId() == R.id.delete) {

                    Info selectedItem = beatmaps.get(position);
                    boolean success = Beatmaps.deleteBeatmap(selectedItem);

                    if(success)
                        beatmaps.remove(position); // Remove the list item
                        beatmapList.invalidateViews(); // Refresh the list view

                }

                return true;
            });

            popupMenu.show();

            return true;

        });
    }

    View.OnClickListener newBeatmapListener = view -> {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle(R.string.new_beatmap_title);
        dialogBuilder.setView(R.layout.dialog_new_beat_map);

        Dialog dialog = dialogBuilder.show();

        EditText songName = dialog.findViewById(R.id.songName);
        EditText songAuthor = dialog.findViewById(R.id.songAuthor);
        EditText levelAuthor = dialog.findViewById(R.id.levelAuthor);
        EditText bpm = dialog.findViewById(R.id.bpm);
        Button createButton = dialog.findViewById(R.id.create);


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                boolean songNameEmpty = songName.getText().toString().trim().isEmpty();
                boolean songAuthorEmpty = songAuthor.getText().toString().trim().isEmpty();
                boolean levelAuthorEmpty = levelAuthor.getText().toString().trim().isEmpty();
                boolean bpmEmpty = bpm.getText().toString().trim().isEmpty();

                createButton.setEnabled( !(songNameEmpty && songAuthorEmpty && levelAuthorEmpty && bpmEmpty) );
            }
        };

        songName.addTextChangedListener(watcher);
        songAuthor.addTextChangedListener(watcher);
        levelAuthor.addTextChangedListener(watcher);

        createButton.setOnClickListener(v -> {
            Log.i(LOG_TAG, "Creating new Beatmap");
            Log.i(LOG_TAG, "Song Name: " + songName.getText());
            Log.i(LOG_TAG, "Song Author: " + songAuthor.getText());
            Log.i(LOG_TAG, "Level Author: " + levelAuthor.getText());
            Log.i(LOG_TAG, "BPM: " + bpm.getText());

            Info result = Beatmaps.createBeatmap(this, songName.getText().toString(), songAuthor.getText().toString(), levelAuthor.getText().toString(), Float.parseFloat(bpm.getText().toString()));

            if(result != null) {

                dialog.cancel();
                beatmaps.add(result);

            } else {
                Snackbar bar = Snackbar.make(view, R.string.beatmap_already_exists, Snackbar.LENGTH_LONG);
                bar.show();
            }

        });

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
