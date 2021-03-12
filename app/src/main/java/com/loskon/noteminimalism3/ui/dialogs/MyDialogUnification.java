package com.loskon.noteminimalism3.ui.dialogs;

import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.ui.activity.MainActivity;

/**
 * Объединение заметок в одну
 */

public class MyDialogUnification {

    private final MainActivity mainActivity;
    private AlertDialog alertDialog;

    public MyDialogUnification(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(mainActivity, R.layout.dialog_unification);
        alertDialog.show();

        Button btnOk = alertDialog.findViewById(R.id.button3678);
        Button btnCancel = alertDialog.findViewById(R.id.button4678);

        assert btnOk != null;
        assert btnCancel != null;

        int color = MyColor.getMyColor(mainActivity);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        btnOk.setOnClickListener(view -> {
            mainActivity.unification();
            alertDialog.dismiss();
        });

        // Click cancel
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
    }
}
