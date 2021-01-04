package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.backup.BackupPath;
import com.loskon.noteminimalism3.db.backup.BackupLimiter;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;

import java.io.File;

public class MyDialogNumOfBackup {

    private final Activity activity;

    private static CallbackNumOfBackup callbackNumOfBackup;

    public void registerCallBackColorNavIcon(CallbackNumOfBackup callbackNumOfBackup) {
        MyDialogNumOfBackup.callbackNumOfBackup = callbackNumOfBackup;
    }

    public MyDialogNumOfBackup(Activity activity) {
        this.activity = activity;
    }

    public void callDialogNumOfBackup(String key, int numOfBackup) {
        AlertDialog alertDialog = MyDialogBuilder.buildDialog(activity, R.layout.dialog_slider);
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

        String titleDialog = null;

        if (key.equals(MyPrefKey.KEY_NUM_OF_BACKUP)) {
            titleDialog = activity.getString(R.string.num_of_backup);
        }

        textTitle.setText(titleDialog);

        int color = MyColor.getColorCustom(activity);
        MyColor.setColorSlider(activity, slider);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        slider.setValue(numOfBackup);

        btnOk.setOnClickListener(view -> {
            int numOfBackupSave = (int) slider.getValue();
            MySharedPref.setInt(activity, key, numOfBackupSave);
            BackupLimiter.delExtraFiles(activity, folder); // Удаление лишних файлов
            callbackNumOfBackup.callingBackNumOfBackup(numOfBackupSave);
            alertDialog.dismiss();
        });

        // Click cancel
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    public interface CallbackNumOfBackup{
        void callingBackNumOfBackup(int numOfBackup);
    }
}
