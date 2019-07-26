package at.tewan.androidmapper.beatmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import at.tewan.androidmapper.R;
import at.tewan.androidmapper.beatmap.info.InfoDifficulty;
import at.tewan.androidmapper.beatmap.info.InfoDifficultySet;
import at.tewan.androidmapper.beatmap.info.Info;


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

    public static boolean saveBeatmap(Context context, Info beatmap) {
        String containerFolderName = Math.abs(beatmap.getSongName().hashCode() * beatmap.getClass().hashCode()) + "";

        File containerFolder = new File(beatmapsRoot, containerFolderName);

        if(containerFolder.mkdir()) {
            Log.i(LOG_TAG, "Created beatmap folder for " + beatmap.getSongName() + " (" + containerFolderName + ")");
        } else {
            Log.i(LOG_TAG, "Folder " + containerFolderName + " already exists.");
        }

        // info.dat

        File infoDatFile = new File(containerFolder, "info.dat");

        if(!infoDatFile.exists()) {

            String infoDatFileContent = gson.toJson(beatmap);

            try (FileWriter writer = new FileWriter(infoDatFile)) {

                writer.write(infoDatFileContent);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Log.i(LOG_TAG, "Done writing info.dat file!");

        // cover.jpg

        File coverImageFile = new File(containerFolder, "cover.jpg");
        if(coverImageFile.exists()) {
            Log.i(LOG_TAG, "cover.jpg already exists.");
        } else {

            Bitmap coverImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.android_cat);

            try (FileOutputStream writer = new FileOutputStream(coverImageFile)) {

                coverImage.compress(Bitmap.CompressFormat.JPEG, 80, writer);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // Difficulties

        ArrayList<InfoDifficultySet> sets = beatmap.getDifficultyBeatmapSets();

        for(InfoDifficultySet set : sets) {

            ArrayList<InfoDifficulty> difficulties = set.getDifficultyBeatmaps();

            for(InfoDifficulty difficulty : difficulties) {

                String filename = difficulty.getBeatmapFilename();
                File difficultyFile = new File(containerFolder, filename);

                try(FileWriter writer = new FileWriter(difficultyFile)) {

                    Log.i(LOG_TAG, difficultyFile.toString());

                    writer.write(filename); // Just write the file without context for now

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }




        return false;
    }

    private static boolean isStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
