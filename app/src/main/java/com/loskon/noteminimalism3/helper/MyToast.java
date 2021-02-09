package com.loskon.noteminimalism3.helper;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {

    public static void showToast(Activity activity, String message, boolean isSuccess) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);

        MyColor.setColorToast(activity, view, isSuccess);

        toast.show();
    }
}
