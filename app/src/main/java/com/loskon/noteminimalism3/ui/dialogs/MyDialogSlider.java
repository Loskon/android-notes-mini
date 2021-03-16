package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.second.BackupLimiter;
import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

import java.io.File;

/**
 * Выбор диапозонов
 */

public class MyDialogSlider {

    private final Activity activity;

    private static CallbackNumOfBackup callbackNumOfBackup;

    public void regCallBackNavIcon(CallbackNumOfBackup callbackNumOfBackup) {
        MyDialogSlider.callbackNumOfBackup = callbackNumOfBackup;
    }

    public MyDialogSlider(Activity activity) {
        this.activity = activity;
    }

    public void call(String keyTitle, int value) {
        AlertDialog alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_slider);
        alertDialog.show();

        File folder = BackupPath.getFolder(activity);

        TextView textTitle = alertDialog.findViewById(R.id.tv_slider_title);
        Slider slider = alertDialog.findViewById(R.id.slider_range);
        Button btnOk = alertDialog.findViewById(R.id.btn_slider_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_slider_cancel);

        textTitle.setText(keyTitle);

        int color = MyColor.getMyColor(activity);
        MyColor.setColorSlider(activity, slider);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        slider.setValue(value);

        btnOk.setOnClickListener(view -> {
            int sliderValue = (int) slider.getValue();
            MySharedPref.setInt(activity, keyTitle, sliderValue);
            BackupLimiter.delExtraFiles(activity, folder); // Удаление лишних файлов
            callbackNumOfBackup.callBack();
            alertDialog.dismiss();
        });

        // Click cancel
        btnCancel.setOnClickListener(view -> alertDialog.dismiss());
    }

    public interface CallbackNumOfBackup{
        void callBack();
    }
}
