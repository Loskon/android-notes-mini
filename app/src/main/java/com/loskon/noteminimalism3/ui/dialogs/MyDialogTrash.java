package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.snackbars.MySnackbar;

public class MyDialogTrash {

    private final DbAdapter dbAdapter;
    private final Activity activity;
    private AlertDialog alertDialog;

    private static CallbackTrash callbackTrash;

    public void registerCallBackTrash(CallbackTrash callbackTrash) {
        MyDialogTrash.callbackTrash = callbackTrash;
    }

    public MyDialogTrash(Activity activity, DbAdapter dbAdapter) {
        this.activity = activity;
        this.dbAdapter = dbAdapter;
    }

    public void call(int countNotes) {
        alertDialog = MyDialogBuilder.buildDialog(activity, R.layout.dialog_trash);
        alertDialog.show();

        Button btnOk = alertDialog.findViewById(R.id.button367);
        Button btnCancel = alertDialog.findViewById(R.id.button467);

        String message = activity.getString(R.string.but_empty_trash);

        int color = MyColor.getColorCustom(activity);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        btnOk.setOnClickListener(view -> {
            if (countNotes !=0) {
                dbAdapter.open();
                dbAdapter.deleteAll();
                dbAdapter.close();
                callbackTrash.callingBackTrash();
            } else {
                MySnackbar.makeSnackbar(activity, activity.findViewById(R.id.coord_layout_main),
                        message, activity.findViewById(R.id.fabMain), false);
            }

            alertDialog.dismiss();
        });

        // Click cancel
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    public interface CallbackTrash{
        void callingBackTrash();
    }
}
