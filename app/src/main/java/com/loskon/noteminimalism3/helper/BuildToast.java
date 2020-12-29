package com.loskon.noteminimalism3.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class BuildToast {

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context,
                message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
