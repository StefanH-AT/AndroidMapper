package at.tewan.androidmapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.enums.Characteristics;
import at.tewan.androidmapper.beatmap.enums.Difficulties;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.beatmap.info.InfoDifficulty;
import at.tewan.androidmapper.beatmap.info.InfoDifficultySet;
import at.tewan.androidmapper.propertiesmenu.DifficultyListAdapter;

public class BeatmapPropertiesActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Beatmap Properties";

    private Spinner characteristicSpinner;
    private RecyclerView difficultyList;

    private ArrayList<InfoDifficulty> currentDifficulties = new ArrayList<>();

    private Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setEnterTransition(new Explode());


        Intent intent = getIntent();

        String beatmapHash = intent.getStringExtra("beatmap_hash");
        Log.i(LOG_TAG, "Beatmap hash: " + beatmapHash);

        setContentView(R.layout.activity_beatmap_properties);

        info = Beatmaps.readStoredBeatmap(beatmapHash);

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

        difficultyList = findViewById(R.id.difficultyList);
        difficultyList.setAdapter(new DifficultyListAdapter(this, currentDifficulties, info));
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
            difficulty.setBeatmapFilename(diffName + ".dat");
            difficulty.setNoteJumpMovementSpeed(Float.parseFloat(njsEditText.getText().toString()));
            difficulty.setDifficulty(diffName);
            difficulty.setDifficultyRank(Difficulties.valueOf(diffName).getRank());

            set.getDifficultyBeatmaps().add(difficulty);


            Beatmaps.saveInfo(this, info, info.getSongName().hashCode() + "");
            currentDifficulties.add(difficulty);
            difficultyList.getAdapter().notifyDataSetChanged();

            dialog.cancel();

        });

    }

}
