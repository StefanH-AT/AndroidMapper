package at.tewan.androidmapper.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferences {

    private static final String LOG_TAG = "Preferences";

    private static final String SHARED_PREFERENCES_FILE = "settings";

    // Constants

    public static final boolean THEME_LIGHT = false;
    public static final boolean THEME_DARK = true;

    private static final String THEME_KEY = "theme";
    private static final String CUSTOM_COLORS_KEY = "draw_custom_colors";
    private static final String DEFAULT_AUTHOR = "default_author";


    // Values

    private static boolean theme;
    private static boolean drawCustomColors;
    private static String defaultAuthor;


    // READ

    public static void loadPreferences(Activity activity) {

        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        Log.i(LOG_TAG, "Loading preferences...");

        theme = preferences.getBoolean(THEME_KEY, THEME_LIGHT);
        drawCustomColors = preferences.getBoolean(CUSTOM_COLORS_KEY, false);
        defaultAuthor = preferences.getString(DEFAULT_AUTHOR, "");

        Log.i(LOG_TAG, "Dark theme: " + theme);
        Log.i(LOG_TAG, "Draw Custom Colors: " + drawCustomColors);
        Log.i(LOG_TAG, "Default Author name: '" + defaultAuthor + "'");

        Log.i(LOG_TAG, "Loaded preferences");

    }


    // WRITE

    public static void savePreferences(Activity activity) {

        SharedPreferences.Editor edit = activity.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit();

        edit.putBoolean(THEME_KEY, isDarkTheme());
        edit.putBoolean(CUSTOM_COLORS_KEY, doesDrawCustomColors());
        edit.putString(DEFAULT_AUTHOR, getDefaultAuthor());

        edit.apply();

        Log.i(LOG_TAG, "Saved preferences");

    }


    // GETTER SETTER


    public static boolean isDarkTheme() {
        return theme;
    }

    public static void setTheme(boolean theme) {
        Preferences.theme = theme;
    }

    public static boolean doesDrawCustomColors() {
        return drawCustomColors;
    }

    public static void setDrawCustomColors(boolean drawCustomColors) {
        Preferences.drawCustomColors = drawCustomColors;
    }

    public static String getDefaultAuthor() {
        return defaultAuthor;
    }

    public static void setDefaultAuthor(String defaultAuthor) {
        Preferences.defaultAuthor = defaultAuthor;
    }
}
