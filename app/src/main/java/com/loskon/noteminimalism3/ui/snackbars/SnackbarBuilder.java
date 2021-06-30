package com.loskon.noteminimalism3.ui.snackbars;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;

/**
 * Конструктор для Snackbar
 */

public class SnackbarBuilder {

    private static Snackbar snackbar;

    public static void makeSnackbar(Context context, View layout,
                                    String message, View anchorView, boolean isSuccess) {

        close();

        snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);

        snackbar.setTextColor(Color.WHITE);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(R.drawable.snackbar_background);
        MyColor.setColorSnackbar(context, snackbarView, isSuccess);

        snackbarView.setOnClickListener(v -> snackbar.dismiss());

        snackbar.setAnchorView(anchorView);
        snackbar.show();
    }

    public static void close() {
        if (snackbar != null) snackbar.dismiss();
    }
}