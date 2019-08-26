package at.tewan.androidmapper.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import at.tewan.androidmapper.R;

public class ErrorPrinter {

    public static AlertDialog msg(Context context, String text) {
        return print(context, context.getString(R.string.dialog_error_title), text);
    }

    public static AlertDialog notify(Context context, String text) {
        return print(context, context.getString(R.string.dialog_notify_title), text);
    }

    public static AlertDialog msg(Context context, Exception ex) {
        return msg(context, ex.getMessage());
    }

    public static AlertDialog msg(Context context, String text, Exception ex) {
        return msg(context, text + System.lineSeparator() + System.lineSeparator() + ex.getMessage());
    }

    private static AlertDialog print(Context context, String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(text);

        builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }

}
