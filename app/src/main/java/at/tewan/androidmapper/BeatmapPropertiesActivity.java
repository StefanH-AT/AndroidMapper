package at.tewan.androidmapper;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.beatmap.enums.Characteristics;
import at.tewan.androidmapper.beatmap.enums.Difficulties;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.beatmap.info.InfoDifficulty;
import at.tewan.androidmapper.beatmap.info.InfoDifficultySet;
import at.tewan.androidmapper.preferences.Preferences;
import at.tewan.androidmapper.propertiesmenu.DifficultyListAdapter;
import at.tewan.androidmapper.util.ErrorPrinter;

import static at.tewan.androidmapper.util.ActivityArguments.BEATMAP_CONTAINER;

public class BeatmapPropertiesActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Beatmap Properties";

    private static final int AUDIO_REQUEST_CODE = 1111;

    private Spinner characteristicSpinner;
    private RecyclerView difficultyList;

    private ArrayList<InfoDifficulty> currentDifficulties = new ArrayList<>();

    private Info info;
    private String beatmapContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setEnterTransition(new Explode());

        Intent intent = getIntent();

        beatmapContainer = intent.getStringExtra(BEATMAP_CONTAINER);
        Log.i(LOG_TAG, "Beatmap container: " + beatmapContainer);

        // Set dark theme
        if(Preferences.isDarkTheme()) {
            setTheme(R.style.AppTheme_Dark);
        }

        setContentView(R.layout.activity_beatmap_properties);

        info = Beatmaps.readBeatmapInfo(beatmapContainer);

        if(info == null) {
            ErrorPrinter.msg(this, "Beatmap info in container '" + beatmapContainer + "' could not be found!");
        }

        TextView songName = findViewById(R.id.songName);
        TextView songSubName = findViewById(R.id.songSubName);
        TextView songAuthor = findViewById(R.id.songAuthor);
        TextView levelAuthor = findViewById(R.id.levelAuthor);
        TextView bpm = findViewById(R.id.bpm);
        ImageView coverView = findViewById(R.id.cover);

        songName.setText(info.getSongName());
        songSubName.setText(info.getSongSubName());
        songAuthor.setText(info.getSongAuthorName());
        levelAuthor.setText(info.getLevelAuthorName());
        bpm.setText(String.valueOf(info.getBeatsPerMinute()));

        Bitmap bitmap = Beatmaps.getCoverBitmap(this, Beatmaps.getCover(beatmapContainer, info.getCoverImageFilename()));
        coverView.setImageBitmap(bitmap);

        difficultyList = findViewById(R.id.difficultyList);
        difficultyList.setAdapter(new DifficultyListAdapter(this, currentDifficulties, info, beatmapContainer));
        difficultyList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        difficultyList.setItemAnimator(new DefaultItemAnimator());

        characteristicSpinner = findViewById(R.id.characteristicSelector);

        ArrayList<String> characteristics = new ArrayList<>();
        for (Characteristics c : Characteristics.values()) {
            characteristics.add(c.getName());
        }

        characteristicSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, characteristics));

        characteristicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = characteristics.get(position);

                showDifficulties(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beatmap_properties, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.editProperties)
            editProperties();

        return super.onOptionsItemSelected(item);
    }

    private void showDifficulties(String characteristic) {
        for(InfoDifficultySet set : info.getDifficultyBeatmapSets()) {
            if(set.getBeatmapCharacteristicName().equals(characteristic)) {
                currentDifficulties.clear();
                currentDifficulties.addAll(set.getDifficultyBeatmaps());

                Log.i(LOG_TAG, "S: " + currentDifficulties.size());

                difficultyList.getAdapter().notifyDataSetChanged();

                return;
            }
        }

        currentDifficulties.clear();
        Log.i(LOG_TAG, "S: " + 0);
        difficultyList.getAdapter().notifyDataSetChanged();
    }

    public void addDifficulty(View v) {
        String characteristic = (String) characteristicSpinner.getSelectedItem();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_difficulty_title);
        builder.setView(R.layout.dialog_new_difficulty);

        AlertDialog dialog = builder.show();

        Spinner difficultyTypeSpinner = dialog.findViewById(R.id.new_difficulty_type);

        ArrayList<String> difficulties = new ArrayList<>();

        for(Difficulties diff : Difficulties.values())
            difficulties.add(diff.name());

        difficultyTypeSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, difficulties));

        EditText njsEditText = dialog.findViewById(R.id.new_difficulty_njs);
        Button createBtn = dialog.findViewById(R.id.new_difficulty_create);

        createBtn.setOnClickListener(view -> {

            InfoDifficultySet set = null;

            for(InfoDifficultySet set2 : info.getDifficultyBeatmapSets()) {
                if(set2.getBeatmapCharacteristicName().equals(characteristic))
                    set = set2;
            }

            if(set == null) {
                set = new InfoDifficultySet(characteristic);
                info.getDifficultyBeatmapSets().add(set);
            }

            // TODO: Check if difficulty already exists.

            String diffName = (String) difficultyTypeSpinner.getSelectedItem();

            InfoDifficulty difficulty = new InfoDifficulty();
            difficulty.setBeatmapFilename(set.getBeatmapCharacteristicName() + "_" + diffName + ".dat");
            difficulty.setNoteJumpMovementSpeed(Float.parseFloat(njsEditText.getText().toString()));
            difficulty.setDifficulty(diffName);
            difficulty.setDifficultyRank(Difficulties.valueOf(diffName).getRank());

            set.getDifficultyBeatmaps().add(difficulty);


            Beatmaps.saveInfo(this, info, beatmapContainer);
            currentDifficulties.add(difficulty);
            difficultyList.getAdapter().notifyDataSetChanged();

            dialog.cancel();

        });

    }

    public void browseSong(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        browserIntent.setType("audio/*");

        startActivityForResult(browserIntent, AUDIO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == AUDIO_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            File file = new File(uri.toString());
            Log.i(LOG_TAG, "Converting audio file '" + file.toString() + "' to ogg format");

            Toast.makeText(this, R.string.feature_missing, Toast.LENGTH_LONG).show();


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void editProperties() {
        Toast.makeText(this, R.string.feature_missing, Toast.LENGTH_LONG).show();
    }
}
