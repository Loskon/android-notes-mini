package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.second.BackupLimiter;
import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;

import java.io.File;

public class MyDialogSlider {

    private final Activity activity;

    private static CallbackNumOfBackup callbackNumOfBackup;

    public void registerCallBackColorNavIcon(CallbackNumOfBackup callbackNumOfBackup) {
        MyDialogSlider.callbackNumOfBackup = callbackNumOfBackup;
    }

    public MyDialogSlider(Activity activity) {
        this.activity = activity;
    }

    public void callDialog(String key, int value) {
        AlertDialog alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_slider);
        alertDialog.show();

        File folder = BackupPath.getFolder(activity);

        TextView textTitle = alertDialog.findViewById(R.id.textView4);
        Slider slider = alertDialog.findViewById(R.id.slider);
        Button btnOk = alertDialog.findViewById(R.id.button36);
        Button btnCancel = alertDialog.findViewById(R.id.button46);

        // assert
        assert slider != null;
        assert btnOk != null;
        assert btnCancel != null;
        assert textTitle != null;

        textTitle.setText(key);

        int color = MyColor.getColorCustom(activity);
        MyColor.setColorSlider(activity, slider);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        slider.setValue(value);

        btnOk.setOnClickListener(view -> {
            int sliderValue = (int) slider.getValue();
            MySharedPref.setInt(activity, key, sliderValue);
            BackupLimiter.delExtraFiles(activity, folder); // Удаление лишних файлов
            callbackNumOfBackup.callingBackNumOfBackup();
            alertDialog.dismiss();
        });

        // Click cancel
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    public interface CallbackNumOfBackup{
        void callingBackNumOfBackup();
    }
}
