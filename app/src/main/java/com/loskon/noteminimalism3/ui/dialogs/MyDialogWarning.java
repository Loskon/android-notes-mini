package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

/**
 * Дилаог для предупреждения
 */

public class MyDialogWarning {

    private final Activity activity;

    public MyDialogWarning(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        AlertDialog alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_warning);
        alertDialog.show();

        Button btnOk = alertDialog.findViewById(R.id.btn_warning_ok);

        int color = MyColor.getMyColor(activity);
        btnOk.setBackgroundColor(color);

        btnOk.setOnClickListener(view -> {
            MySharedPref.setBoolean(activity, MyPrefKey.KEY_DIALOG_WARNING_SHOW, false);
            alertDialog.dismiss();
        });
    }
}
