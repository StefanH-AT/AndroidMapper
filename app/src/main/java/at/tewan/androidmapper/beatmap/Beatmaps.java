package at.tewan.androidmapper.beatmap;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Provides static functions to read and write beatmaps.
 *
 */
public class Beatmaps {

    private static final String LOG_TAG = "Beatmaps";

    private static final File beatmapsRoot = Environment.getExternalStoragePublicDirectory("beatmaps");

    private static final Gson gson = new Gson();

    public static void init() {
        Log.i(LOG_TAG, "Beatmap root dir: " + beatmapsRoot.toString());
        if(beatmapsRoot.mkdirs()) {
            Log.i(LOG_TAG, "Beatmap root dir created");
        } else {
            Log.i(LOG_TAG, "Beatmap root exists");
        }
    }

    public static boolean saveBeatmap(Beatmap beatmap) {
        String containerFolderName = Math.abs(beatmap.getSongName().hashCode() * beatmap.getClass().hashCode()) + "";

        File containerFolder = new File(beatmapsRoot, containerFolderName);

        if(containerFolder.mkdir()) {
            Log.i(LOG_TAG, "Created beatmap folder for " + beatmap.getSongName() + " (" + containerFolderName + ")");
        } else {
            Log.i(LOG_TAG, "Folder " + containerFolderName + " already exists.");
        }

        File infoDatFile = new File(containerFolder, "info.dat");
        String infoDatFileContent = gson.toJson(beatmap);

        try {
            FileWriter writer = new FileWriter(infoDatFile);
            writer.write(infoDatFileContent);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG, "Done writing info.dat file!");

        return false;
    }

    private static boolean isStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
