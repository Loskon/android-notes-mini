package com.loskon.noteminimalism3.ui.preference.prefdialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.loskon.noteminimalism3.R;

public class DialogFontSize {

    static int ui_flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;

    public static void alertDialogShowColorPicker2(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.customDialog);
        // set the custom layout
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customLayout = inflater.inflate(R.layout.dialog_holo_picker, null);
        builder.setView(customLayout);

        customLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.dialog_custom));



        AlertDialog dialog = builder.create();

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        dialog.getWindow().getDecorView().setSystemUiVisibility(ui_flags);
        dialog.getWindow().setStatusBarColor(Color.RED);

        dialog.show();
    }

}
