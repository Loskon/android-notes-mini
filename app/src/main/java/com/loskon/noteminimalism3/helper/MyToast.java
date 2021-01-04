package com.loskon.noteminimalism3.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loskon.noteminimalism3.R;

public class MyToast {

    public static void showToast(Activity activity, String message, boolean isSuccess) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);

        if (isSuccess) {
            view.getBackground().setColorFilter(activity.getResources().getColor(R.color.snackbar_completed_light), PorterDuff.Mode.SRC_IN);
            //snackbarView.setBackgroundTintList(ColorStateList.valueOf(activity
            //  .getResources().getColor(R.color.snackbar_completed_dark)));
        } else {
            view.getBackground().setColorFilter(activity.getResources().getColor(R.color.snackbar_no_completed_light), PorterDuff.Mode.SRC_IN);
            //snackbarView.setBackgroundTintList(ColorStateList.valueOf(activity
            //.getResources().getColor(R.color.snackbar_no_completed_dark)));
        }

        toast.show();
    }
}
