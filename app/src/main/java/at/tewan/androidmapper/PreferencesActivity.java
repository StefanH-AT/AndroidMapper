package at.tewan.androidmapper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import at.tewan.androidmapper.preferences.Preferences;

public class PreferencesActivity extends AppCompatActivity {

    // Widgets

    private CheckBox settingTheme, settingCustomColors;
    private EditText settingDefaultAuthor;

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

        // Put preferences
        settingTheme.setChecked(Preferences.isDarkTheme());
        settingCustomColors.setChecked(Preferences.doesDrawCustomColors());
        settingDefaultAuthor.setText(Preferences.getDefaultAuthor());

        // ==========================================
        // Preference Change Events
        // ==========================================
        settingTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Preferences.setTheme(settingTheme.isChecked());
        });
        settingCustomColors.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Preferences.setDrawCustomColors(settingCustomColors.isChecked());
        });
        settingDefaultAuthor.setOnEditorActionListener((v, actionId, event) -> {
            Preferences.setDefaultAuthor(settingDefaultAuthor.getText().toString());
            return false;
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
