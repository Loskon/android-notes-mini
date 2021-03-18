package com.loskon.noteminimalism3.ui.sheet;

import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.backup.prime.BpCloud;
import com.loskon.noteminimalism3.ui.activity.BackupActivity;

public class MySheetConfirm {

    private final BackupActivity activity;

    public MySheetConfirm(BackupActivity activity) {
        this.activity = activity;
    }

    public void call(boolean isBackup) {
        BpCloud bpCloud = activity.getBpCloud();

        BottomSheetDialog bottomSheetDialog  = new BottomSheetDialog(activity,
                R.style.BottomSheetBackground);
        View inflate = View.inflate(activity, R.layout.bottom_sheet, null);

        Button btnConfirm = inflate.findViewById(R.id.btn_sheet_confirm);

        int color = MyColor.getMyColor(activity);
        btnConfirm.setBackgroundColor(color);

        btnConfirm.setOnClickListener(v -> {
            bpCloud.backupAndRestore(isBackup);
            bottomSheetDialog.dismiss();
        });
        
        bottomSheetDialog.setContentView(inflate);
        bottomSheetDialog.show();
    }
}
