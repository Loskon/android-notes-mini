package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.ColorUtils;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;

public class MyDialogProgress {

    private final Activity activity;
    private AlertDialog alertDialog;

    public MyDialogProgress(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_progress);
        alertDialog.setCancelable(false);
        alertDialog.show();

        LinearProgressIndicator linearProgressIndicator =
                alertDialog.findViewById(R.id.LinearProgressIndicator);

        int color = MyColor.getColorCustom(activity);
        linearProgressIndicator.setIndicatorColor(color);
        linearProgressIndicator.setTrackColor(ColorUtils.setAlphaComponent(color, 70));
    }

    public void close() {
        alertDialog.dismiss();
    }
}
