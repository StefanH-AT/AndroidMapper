package at.tewan.androidmapper.beatmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import at.tewan.androidmapper.R;
import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.info.InfoDifficulty;
import at.tewan.androidmapper.beatmap.info.InfoDifficultySet;
import at.tewan.androidmapper.beatmap.info.Info;


/**
 * Provides static functions to read and write beatmaps.
 *
 */
public class Beatmaps {

    private static final String LOG_TAG = "Beatmaps";

    private static final String BEATMAP_INFO_FILE = "info.dat";
    private static final String COVER_FILE = "cover.jpg";

    public static final File BEATMAPS_ROOT = Environment.getExternalStoragePublicDirectory("beatmaps");

    private static final Gson gson = new Gson();
    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        Log.i(LOG_TAG, "Beatmap root dir: " + BEATMAPS_ROOT.toString());
        if(BEATMAPS_ROOT.mkdirs()) {
            Log.i(LOG_TAG, "Beatmap root dir created");
        } else {
            Log.i(LOG_TAG, "Beatmap root exists");
        }
    }

    public static Info createBeatmap(Context context, String songName, String songAuthor, String levelAuthor, float bpm) {
        Beatmap beatmap = new Beatmap();
        Info info = new Info();

        info.setSongName(songName);
        info.setSongAuthorName(songAuthor);
        info.setLevelAuthorName(levelAuthor);
        info.setBeatsPerMinute(bpm);

        beatmap.setInfo(info);


        // Using the hash code as the beatmap folder name
        String containerFolderName = songName.hashCode() + "";

        boolean saveSuccessful = saveInfo(context, info, containerFolderName);

        if(saveSuccessful)
            return info;
        else
            return null;

    }

    public static boolean deleteBeatmap(Info info) {
        File beatmapDir = new File(BEATMAPS_ROOT, info.getSongName().hashCode() + "");

        boolean exists = beatmapDir.exists();

        Log.i(LOG_TAG, "Deleting '" + beatmapDir.toString() + "' (" + beatmapDir.exists() + " " + beatmapDir.isDirectory() + ")");

        if(exists) {
            try {
                FileUtils.deleteDirectory(beatmapDir);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return exists;
    }

    public static File getCover(String container, String cover) {
        return new File(Beatmaps.BEATMAPS_ROOT, container + System.getProperty("file.separator") + cover);
    }

    public static boolean saveInfo(Context context, Info info, String containerFolderName) {

        File containerFolder = new File(BEATMAPS_ROOT, containerFolderName);

        if(containerFolder.mkdir()) {
            Log.i(LOG_TAG, "Created beatmap folder for " + info.getSongName() + " (" + containerFolderName + ")");
        } else {
            Log.i(LOG_TAG, "Folder " + containerFolderName + " already exists.");
        }

        // info.dat

        File infoDatFile = new File(containerFolder, BEATMAP_INFO_FILE);

        String infoDatFileContent = gsonPretty.toJson(info);

        try (FileWriter writer = new FileWriter(infoDatFile)) {

            writer.write(infoDatFileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.i(LOG_TAG, "Done writing info.dat file!");

        // cover.jpg

        File coverImageFile = new File(containerFolder, COVER_FILE);
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

        ArrayList<InfoDifficultySet> sets = info.getDifficultyBeatmapSets();

        for(InfoDifficultySet set : sets) {

            ArrayList<InfoDifficulty> difficulties = set.getDifficultyBeatmaps();

            for(InfoDifficulty difficulty : difficulties) {

                String filename = difficulty.getBeatmapFilename();
                File difficultyFile = new File(containerFolder, filename);

                try(FileWriter writer = new FileWriter(difficultyFile)) {

                    Log.i(LOG_TAG, difficultyFile.toString());

                    writer.write(gson.toJson(info)); // Just write the file without context for now

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }


        return true;
    }

    public static Info readStoredBeatmap(String container) {
        File beatmapInfo = new File(BEATMAPS_ROOT, container + System.getProperty("file.separator") + BEATMAP_INFO_FILE);

        if(beatmapInfo.exists()) {

            try {

                FileReader reader = new FileReader(beatmapInfo);

                return gson.fromJson(reader, Info.class);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }
    }

    public static ArrayList<Info> readStoredBeatmapInfos(Context context) {
        File[] beatmapContainers = BEATMAPS_ROOT.listFiles(File::isDirectory);

        ArrayList<Info> infos = new ArrayList<>();

        for(File container : beatmapContainers) {
            File infoFile = new File(container, BEATMAP_INFO_FILE);

            try {

                FileReader reader = new FileReader(infoFile);
                infos.add(gson.fromJson(reader, Info.class));

            } catch (IOException e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        return infos;
    }

    public static Difficulty readDifficulty(String container, String filename) {
        File difficultyFile = new File(BEATMAPS_ROOT, container + System.getProperty("file.separator") + filename);

        Log.i(LOG_TAG, "Loading difficulty '" + difficultyFile.toString() + "'...");

        if(!difficultyFile.exists()) {

            Log.i(LOG_TAG, "Difficulty file could not be found. Creating empty difficulty!");

            // Return empty difficulty when file could not be found
            return new Difficulty();
        }

        Log.i(LOG_TAG, "Loading difficulty from file...");

        try(FileReader reader = new FileReader(difficultyFile)) {

            Difficulty difficulty = gson.fromJson(reader, Difficulty.class);

            Log.i(LOG_TAG, "Done loading difficulty file");

            return difficulty;

        } catch (IOException ex) {
            ex.printStackTrace();

            return null;
        }

    }

    private static boolean isStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
