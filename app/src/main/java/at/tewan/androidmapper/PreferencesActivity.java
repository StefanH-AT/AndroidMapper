package at.tewan.androidmapper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

import at.tewan.androidmapper.preferences.Preferences;

import static at.tewan.androidmapper.preferences.Preferences.*;

public class PreferencesActivity extends AppCompatActivity {

    // Widgets

    private CheckBox settingTheme, settingCustomColors, settingDebug;
    private EditText settingDefaultAuthor, settingSubBeat;

    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set dark theme
        if(Preferences.isDarkTheme()) {
            setTheme(R.style.AppTheme_Dark);
        }

        setContentView(R.layout.activity_preferences);

        getSupportActionBar().setHomeButtonEnabled(true); // <== Top Left back button

        settingTheme            =   findViewById(R.id.settingTheme);
        settingCustomColors     =   findViewById(R.id.settingCustomColor);
        settingDefaultAuthor    =   findViewById(R.id.settingDefaultAuthor);
        settingSubBeat          =   findViewById(R.id.settingSubBeat);
        settingDebug            =   findViewById(R.id.settingDebug);

        // Put preferences
        settingTheme.setChecked(isDarkTheme());
        settingCustomColors.setChecked(doesDrawCustomColors());
        settingDefaultAuthor.setText(getDefaultAuthorKey());
        settingSubBeat.setText(String.valueOf(getSubBeatCountKey()));
        settingDebug.setChecked(isDebug());

        // ==========================================
        // Preference Change Events
        // ==========================================
        settingTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Preferences.setDarkTheme(isChecked);
        });
        settingCustomColors.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDrawCustomColors(settingCustomColors.isChecked());
        });
        settingDefaultAuthor.setOnEditorActionListener((v, actionId, event) -> {
            setDefaultAuthorKey(settingDefaultAuthor.getText().toString());
            return false;
        });
        settingSubBeat.setOnEditorActionListener((v, actionId, event) -> {
            setSubBeatCountKey(Integer.parseInt(settingSubBeat.getText().toString()));
            return false;
        });
        settingDebug.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDebug(isChecked);
        });

    }

    @Override
    protected void onDestroy() {

        Preferences.savePreferences(this);

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        Preferences.savePreferences(this);

        super.onBackPressed();
    }
}
