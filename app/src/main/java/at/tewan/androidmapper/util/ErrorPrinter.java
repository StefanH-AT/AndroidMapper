package at.tewan.androidmapper.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class ErrorPrinter {

    private static AlertDialog dialog;

    public static void msg(Context context, String text) {
        dialog = new AlertDialog.Builder(context).create();

        dialog.setTitle("An error occurred");
        dialog.setMessage(text);
        dialog.show();
    }

    public static void msg(Context context, Exception ex) {
        msg(context, ex.getMessage());
    }

    public static void msg(Context context, String text, Exception ex) {
        msg(context, text + System.lineSeparator() + System.lineSeparator() + ex.getMessage());
    }

}
