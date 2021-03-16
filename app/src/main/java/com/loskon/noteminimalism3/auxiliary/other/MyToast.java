package com.loskon.noteminimalism3.auxiliary.other;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Кастомный toast
 */

public class MyToast {

    public static void showToast(Activity activity, String message, boolean isSuccess) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        text.setGravity(Gravity.CENTER);
        MyColor.setColorToast(activity, view, isSuccess);
        toast.show();
    }
}
