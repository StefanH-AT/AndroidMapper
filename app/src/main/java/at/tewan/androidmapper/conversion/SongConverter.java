package at.tewan.androidmapper.conversion;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.util.ErrorPrinter;

/**
 * @author Stefan Heinz
 *
 * DEPRICATED!
 *
 * Intent was to automatically convert outdated beatmaps back to the newest version
 */
public class SongConverter {

    private static final String LOG_TAG = "SongConverter";

    private static final String SIMPLE_CONVERTER_FILE_NAME = "simple-converter.exe";
    private static final String internalSimpleConverterFile = SIMPLE_CONVERTER_FILE_NAME;
    private static File externalSimpleConverterFile;

    /**
     * Must be run AFTER {@link Beatmaps#init() Beatmaps::init()} has been called!!
     */
    public static void init(Context context) {
        Log.i(LOG_TAG, "Initializing Song Converter (Using songe-converter by lolpants)");

        externalSimpleConverterFile = new File(Beatmaps.BEATMAPS_ROOT, SIMPLE_CONVERTER_FILE_NAME);

        Log.i(LOG_TAG, "External simple converter file path: '" + externalSimpleConverterFile.toString() + "'");

        if(externalSimpleConverterFile.exists()) {
            Log.i(LOG_TAG, "External simple converter is present.");
        } else {
            Log.i(LOG_TAG, "External simple converter could not be found.");
            Log.i(LOG_TAG, "Starting to copy single converter from app classpath");

            try {
                InputStream input = SongConverter.class.getResourceAsStream(internalSimpleConverterFile);
                FileOutputStream output = new FileOutputStream(externalSimpleConverterFile);

                if(input != null) {
                    IOUtils.copy(input, output);
                } else {
                    ErrorPrinter.msg(context, "Failed to extract " + SIMPLE_CONVERTER_FILE_NAME + ". Report this bug immediately");
                }

            } catch (IOException | NullPointerException ex) {
                ex.printStackTrace();
            }

            Log.i(LOG_TAG, "Done.");
        }


    }

}
