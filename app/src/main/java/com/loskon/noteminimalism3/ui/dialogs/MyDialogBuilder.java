package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

public class MyDialogBuilder {

    public static AlertDialog buildDialog(Context context, int layout) {
        AlertDialog alertDialog =  new MaterialAlertDialogBuilder(context,
                MaterialAlertDialog_Rounded)
                .setView(layout)
                .create();
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width,height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        return alertDialog;
    }

}
