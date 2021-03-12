package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.ColorUtils;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;

/**
 * Индикатор прогресса выполнения долгих запросов
 */

public class MyDialogProgress {

    private final Activity activity;
    private AlertDialog alertDialog;

    public MyDialogProgress(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        //if (alertDialog != null && alertDialog.isShowing()) return;
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_progress);
        alertDialog.setCancelable(false);
        alertDialog.show();

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.42);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);

        CircularProgressIndicator linearProgressIndicator =
                alertDialog.findViewById(R.id.circularProgressIndicator);

        int color = MyColor.getMyColor(activity);
        linearProgressIndicator.setIndicatorColor(color);
        linearProgressIndicator.setTrackColor(ColorUtils.setAlphaComponent(color, 70));
    }

    public void close() {
        alertDialog.dismiss();
    }
}
