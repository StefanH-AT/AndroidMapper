package at.tewan.androidmapper.beatmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private static final String BEATMAP_INFO_FILE = "info.dat";

    private static final File beatmapsRoot = Environment.getExternalStoragePublicDirectory("beatmaps");

    private static final Gson gson = new Gson();
    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        Log.i(LOG_TAG, "Beatmap root dir: " + beatmapsRoot.toString());
        if(beatmapsRoot.mkdirs()) {
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
        File beatmapDir = new File(beatmapsRoot, info.getSongName().hashCode() + "");

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

    public static boolean saveInfo(Context context, Info info, String containerFolderName) {

        File containerFolder = new File(beatmapsRoot, containerFolderName);

        if(containerFolder.mkdir()) {
            Log.i(LOG_TAG, "Created beatmap folder for " + info.getSongName() + " (" + containerFolderName + ")");
        } else {
            Log.i(LOG_TAG, "Folder " + containerFolderName + " already exists.");
            return false;
        }

        // info.dat

        File infoDatFile = new File(containerFolder, BEATMAP_INFO_FILE);

        if(!infoDatFile.exists()) {

            String infoDatFileContent = gsonPretty.toJson(info);

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

    public static ArrayList<Info> readStoredBeatmapInfos(Context context) {
        File[] beatmapContainers = beatmapsRoot.listFiles(File::isDirectory);

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

    private static boolean isStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
