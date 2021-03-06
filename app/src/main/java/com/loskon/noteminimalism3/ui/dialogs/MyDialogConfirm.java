package com.loskon.noteminimalism3.ui.dialogs;

import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.backup.prime.BpCloud;
import com.loskon.noteminimalism3.ui.activity.BackupActivity;

/**
 * Очитска корзины
 */

public class MyDialogConfirm {

    private final BackupActivity backupActivity;
    private AlertDialog alertDialog;

    public MyDialogConfirm(BackupActivity backupActivity) {
        this.backupActivity = backupActivity;
    }

    public void call(boolean isBackup) {
        BpCloud bpCloud = backupActivity.getBpCloud();

        alertDialog = DialogBuilder.buildDialog(backupActivity, R.layout.dialog_confirm);
        alertDialog.show();

        Button btnYes = alertDialog.findViewById(R.id.btn_yes_confirm);
        Button btnNo = alertDialog.findViewById(R.id.btn_no_confirm);

        int color = MyColor.getColorCustom(backupActivity);
        btnYes.setBackgroundColor(color);
        btnNo.setTextColor(color);

        btnYes.setOnClickListener(view -> {
            bpCloud.backupAndRestore(isBackup);
            alertDialog.dismiss();
        });

        btnNo.setOnClickListener(view -> alertDialog.dismiss());
    }
}
