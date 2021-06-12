package com.loskon.noteminimalism3.auxiliary.other;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Кастомный toast
 */

public class MyToast {

    private static Toast toast;

    public static void showToast(Activity activity, String message, boolean isSuccess) {
        if (toast != null) toast.cancel();

        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            View view = toast.getView();
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.WHITE);
            text.setGravity(Gravity.CENTER);
            MyColor.setColorToast(activity, view, isSuccess);
        }
        toast.show();
    }
}
