package at.tewan.androidmapper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import at.tewan.androidmapper.preferences.Preferences;

public class PreferencesActivity extends AppCompatActivity {

    private boolean saved = false;


    // Widgets

    private CheckBox settingTheme, settingCustomColors;
    private EditText settingDefaultAuthor;

    private Menu actionBarMenu;

    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set dark theme
        if(Preferences.isDarkTheme()) {
            setTheme(R.style.AppTheme_Dark);
        }

        setContentView(R.layout.activity_preferences);

        settingTheme            =   findViewById(R.id.settingTheme);
        settingCustomColors     =   findViewById(R.id.settingCustomColor);
        settingDefaultAuthor    =   findViewById(R.id.settingDefaultAuthor);

        // Put preferences
        settingTheme.setChecked(Preferences.isDarkTheme());
        settingCustomColors.setChecked(Preferences.doesDrawCustomColors());
        settingDefaultAuthor.setText(Preferences.getDefaultAuthor());

        // Unsave events
        settingTheme.setOnCheckedChangeListener((buttonView, isChecked) -> setUnsavedState());
        settingCustomColors.setOnCheckedChangeListener((buttonView, isChecked) -> setUnsavedState());
        settingDefaultAuthor.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                setUnsavedState();
            }
        });

    }

    private void setUnsavedState() {
        saved = false;

        actionBarMenu.findItem(R.id.save).setEnabled(true);

    }

    private void setSavedState() {
        saved = true;

        Preferences.setTheme(settingTheme.isChecked());
        Preferences.setDrawCustomColors(settingCustomColors.isChecked());
        Preferences.setDefaultAuthor(settingDefaultAuthor.getText().toString());

        Preferences.savePreferences(this);

        Toast.makeText(this, R.string.preferences_saved, Toast.LENGTH_SHORT).show();

        actionBarMenu.findItem(R.id.save).setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preferences, menu);

        actionBarMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save) {
            setSavedState();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO: Fix onBackPressed() in PreferencesActivity
        if(!saved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.message_unsaved_changes);
            builder.setTitle(R.string.title_unsaved_changes);

            builder.setPositiveButton(R.string.yes, (dialog, which) -> super.onBackPressed());

        }

    }
}
