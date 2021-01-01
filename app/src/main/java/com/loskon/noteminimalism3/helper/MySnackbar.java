package com.loskon.noteminimalism3.helper;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.loskon.noteminimalism3.R;

public class MySnackbar {

    public static void makeSnackbar(Activity activity, View layout,
                                    String message, BottomAppBar bar, boolean isSuccess) {
        if (layout != null) {
            Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
            snackbar.setTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundResource(R.drawable.snackbar_round_corner);
            if (isSuccess) {
                snackbarView.setBackgroundTintList(ColorStateList.valueOf(activity
                        .getResources().getColor(R.color.snackbar_completed_light)));

                //snackbarView.setBackgroundTintList(ColorStateList.valueOf(activity
                      //  .getResources().getColor(R.color.snackbar_completed_dark)));
            } else {
                snackbarView.setBackgroundTintList(ColorStateList.valueOf(activity
                        .getResources().getColor(R.color.snackbar_no_completed_light)));
                //snackbarView.setBackgroundTintList(ColorStateList.valueOf(activity
                        //.getResources().getColor(R.color.snackbar_no_completed_dark)));
            }
            snackbar.setAnchorView(bar);

            snackbar.show();
        }
    }
}
