package at.tewan.androidmapper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

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

        settingTheme            =   findViewById(R.id.settingTheme);
        settingCustomColors     =   findViewById(R.id.settingCustomColor);
        settingDefaultAuthor    =   findViewById(R.id.settingDefaultAuthor);

        settingTheme.setChecked(Preferences.isDarkTheme());
        settingCustomColors.setChecked(Preferences.doesDrawCustomColors());
        settingDefaultAuthor.setText(Preferences.getDefaultAuthor());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save) {

            Preferences.setTheme(settingTheme.isChecked());
            Preferences.setDrawCustomColors(settingCustomColors.isChecked());
            Preferences.setDefaultAuthor(settingDefaultAuthor.getText().toString());

            Preferences.savePreferences(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
