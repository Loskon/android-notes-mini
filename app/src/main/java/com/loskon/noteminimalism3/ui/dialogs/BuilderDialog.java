package com.loskon.noteminimalism3.ui.dialogs;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialogBackground;

/**
 * Конструктор для создания кастомного AlertDialog
 */

public class BuilderDialog {

    public static AlertDialog buildDialog(Context context, int layout) {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(context,
                MaterialAlertDialogBackground)
                .setView(layout)
                .create();

        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        return alertDialog;
    }
}
