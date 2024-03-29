package at.tewan.androidmapper.beatmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import at.tewan.androidmapper.R;
import at.tewan.androidmapper.beatmap.difficulty.Difficulty;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.util.ErrorPrinter;


/**
 * Provides static functions to read and write beatmaps.
 *
 * @author Stefan Heinz
 */
public class Beatmaps {

    private static final String LOG_TAG = "Beatmap IO";

    public static final String BEATMAP_INFO_FILE = "info.dat";
    public static final String BEATMAP_INFO_FILE_OLD = "info.json";
    private static final int COVER_QUALITY = 80;

    private static final String SEPARATOR = System.getProperty("file.separator");

    public static final File BEATMAPS_ROOT = Environment.getExternalStoragePublicDirectory("beatmaps");

    private static final Gson gson = new Gson();
    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    public static void init() throws IOException{
        log("Initializing beatmap IO");

        if(isStorageWritable()) {
            log("Storage writable");
        } else {
            log("Storage not writable!! Aborting");
            throw new IOException();
        }

        log("Beatmap root dir: " + BEATMAPS_ROOT.toString());
        if(BEATMAPS_ROOT.mkdirs()) {
            log("Beatmap root dir created");
        } else {
            log("Beatmap root exists");
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


        // Folder name used for beatmap
        String containerFolderName = levelAuthor + "_" + songName + "_" + songAuthor + "_" + (int)bpm;

        boolean saveSuccessful = saveInfo(context, info, containerFolderName);

        if(saveSuccessful)
            return info;
        else
            return null;

    }

    public static boolean deleteBeatmap(Info info) {
        File beatmapDir = new File(BEATMAPS_ROOT, info.getSongName().hashCode() + "");

        boolean exists = beatmapDir.exists();

        Log.i(LOG_TAG, "Deleting '" + beatmapDir.toString() + "' | Exists: (" + exists + ")");

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

    /**
     * @param container Beatmap container name
     * @param cover Name of the cover image. ONLY pass over the '_coverImageFilename' from info.dat!!!!
     * @return {@link java.io.File File} object pointing to the cover image
     */
    public static File getCover(String container, String cover) {
        return new File(BEATMAPS_ROOT, container + SEPARATOR + cover);
    }

    public static Bitmap getCoverBitmap(Context context, File coverFile) {

        if(coverFile.exists()) {
            return BitmapFactory.decodeFile(coverFile.toString());
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.no_cover);
        }

    }

    /**
     * @param container Beatmap container folder name
     * @param songFile File name of the song file (Example: "song.egg")
     * @return {@link java.io.File File} that points to the song file
     */
    public static File getSong(String container, String songFile) {
        return new File(BEATMAPS_ROOT, container + SEPARATOR + songFile);
    }

    /**
     * @return {@link java.io.File File} array of all folders found in the beatmaps root directory
     */
    public static File[] getContainers() {

        File[] allContainers = BEATMAPS_ROOT.listFiles(File::isDirectory);
        ArrayList<File> interestingContainers = new ArrayList<>();

        for(File f : allContainers) {
            File infoFile = new File(f, BEATMAP_INFO_FILE);

            if(infoFile.exists()) {
                interestingContainers.add(f);
            }
        }

        return interestingContainers.toArray(new File[0]);
    }

    /**
     * @return Returns whether or not all files have been saved successfully
     */
    public static boolean saveInfo(Context context, Info info, String containerFolderName) {

        File containerFolder = new File(BEATMAPS_ROOT, containerFolderName);

        if(containerFolder.mkdir()) {
            Log.i(LOG_TAG, "Created beatmap folder for " + info.getSongName() + " (" + containerFolderName + ")");
        } else {
            Log.i(LOG_TAG, "Folder '" + containerFolderName + "' already exists.");
        }

        // info.dat

        File infoDatFile = new File(containerFolder, BEATMAP_INFO_FILE);

        String infoDatFileContent = gsonPretty.toJson(info);

        try (FileWriter writer = new FileWriter(infoDatFile)) {

            writer.write(infoDatFileContent);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        Log.i(LOG_TAG, "Done writing info.dat file!");

        // cover.jpg

        File coverImageFile = new File(containerFolder, info.getCoverImageFilename());
        if(coverImageFile.exists()) {
            log("Cover already exists.");
        } else {
            log("Cover does not exist. Creating default cover");

            Bitmap coverImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.android_cat);

            try (FileOutputStream writer = new FileOutputStream(coverImageFile)) {

                coverImage.compress(Bitmap.CompressFormat.JPEG, COVER_QUALITY, writer);

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

        // Difficulties
        /*          // This code has been moved to Beatmaps.saveDifficulty(). Difficulties will now be saved each on their own and not in bulk
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

        }*/


        return true;
    }

    /**
     * @param container Beatmap container folder name
     * @return Reads info.dat file present in the beatmap container file, parses the json data and returns it as {@link at.tewan.androidmapper.beatmap.info.Info Info}. Null if not found
     */
    public static Info readBeatmapInfo(String container) {
        File beatmapInfo = new File(BEATMAPS_ROOT, container + SEPARATOR + BEATMAP_INFO_FILE);

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

    /**
     * @return {@link java.util.ArrayList<Info> ArrayList<Info>} of all beatmap info files that could be found
     */
    public static ArrayList<Info> readAllBeatmapInfos(Context context) throws IOException {
        File[] beatmapContainers = getContainers();

        ArrayList<Info> infos = new ArrayList<>();

        for(File container : beatmapContainers) {
            File infoFile = new File(container, BEATMAP_INFO_FILE);

            FileReader reader = new FileReader(infoFile);

            try {

                Info info = gson.fromJson(reader, Info.class);
                infos.add(info);

            } catch (JsonSyntaxException ex) {
                ErrorPrinter.msg(context, infoFile.toString() + " contains invalid json data", ex);
            }


        }

        return infos;
    }

    /**
     * @param container The beatmap container name
     * @param filename The difficulty file name (Must be grabbed from info.dat to avoid complications)
     * @return Parses json and returns it as {@link at.tewan.androidmapper.beatmap.difficulty.Difficulty Difficulty}
     */
    public static Difficulty readDifficulty(String container, String filename) throws IOException {
        File difficultyFile = new File(BEATMAPS_ROOT, container + System.getProperty("file.separator") + filename);

        log("Loading difficulty '" + difficultyFile.toString() + "'...");

        if(!difficultyFile.exists()) {

            Log.i(LOG_TAG, "Difficulty file could not be found. Creating empty difficulty!");

            // Return empty difficulty when file could not be found
            return new Difficulty();
        }

        log("Loading difficulty from file...");

        FileReader reader = new FileReader(difficultyFile);

        Difficulty difficulty = gson.fromJson(reader, Difficulty.class);
        reader.close();

        log("Done loading difficulty file");

        return difficulty;

    }

    /**
     * @param difficulty The {@link at.tewan.androidmapper.beatmap.difficulty.Difficulty Difficulty} object to save
     * @param container Container folder name
     * @param filename Name under which the file should be saved. (Must be read from info.dat to link up)
     * @return true if saved successfully, false if not
     */
    public static boolean saveDifficulty(Difficulty difficulty, String container, String filename) {
        File difficultyFile = new File(BEATMAPS_ROOT, container + System.getProperty("file.separator") + filename);

        if(difficultyFile.exists()) {
            log("Difficulty file '" + difficultyFile.toString() + "' exists. Overriding.");
        } else {
            log("Difficulty file '" + difficultyFile.toString() + "' does not exist. Creating.");
        }

        try (FileWriter writer = new FileWriter(difficultyFile)) {
            writer.write(gson.toJson(difficulty));
        } catch (IOException ex) {
            ex.printStackTrace();

            return false;
        }

        log("Done writing file");
        return true;

    }

    private static void log(String message) {
        Log.i(LOG_TAG, message);
    }

    private static boolean isStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
