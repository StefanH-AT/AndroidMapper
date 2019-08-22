package at.tewan.androidmapper.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import at.tewan.androidmapper.R;

public class ErrorPrinter {

    public static void msg(Context context, String text) {
        print(context, context.getString(R.string.dialog_error_title), text);
    }

    public static void notify(Context context, String text) {
        print(context, context.getString(R.string.dialog_notify_title), text);
    }

    public static void msg(Context context, Exception ex) {
        msg(context, ex.getMessage());
    }

    public static void msg(Context context, String text, Exception ex) {
        msg(context, text + System.lineSeparator() + System.lineSeparator() + ex.getMessage());
    }

    private static void print(Context context, String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

}
