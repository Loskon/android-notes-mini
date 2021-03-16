package com.loskon.noteminimalism3.ui.dialogs;

import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.ui.activity.MainActivity;
import com.loskon.noteminimalism3.ui.snackbars.SnackbarBuilder;

/**
 * Очитска корзины
 */

public class MyDialogTrash {

    private final DbAdapter dbAdapter;
    private final MainActivity mainActivity;
    private AlertDialog alertDialog;
    private final FloatingActionButton fabMain;
    private final CoordinatorLayout coordinatorLayout;

    public MyDialogTrash(MainActivity mainActivity, DbAdapter dbAdapter,
                         FloatingActionButton fabMain, CoordinatorLayout coordinatorLayout) {
        this.mainActivity = mainActivity;
        this.dbAdapter = dbAdapter;
        this.fabMain = fabMain;
        this.coordinatorLayout = coordinatorLayout;
    }

    public void call(int countNotes) {
        alertDialog = DialogBuilder.buildDialog(mainActivity, R.layout.dialog_trash);
        alertDialog.show();

        Button btnOk = alertDialog.findViewById(R.id.btn_trash_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_trash_cancel);

        int color = MyColor.getMyColor(mainActivity);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        btnOk.setOnClickListener(view -> {
            if (countNotes != 0) {
                onClickOk();
            } else {
                showSnackbar();
            }
            alertDialog.dismiss();
        });

        btnCancel.setOnClickListener(view -> alertDialog.dismiss());
    }

    private void onClickOk() {
        // Удалить все заметки из корзины
        dbAdapter.open();
        dbAdapter.deleteAll();
        dbAdapter.close();
        mainActivity.goUpdateMethod();
    }

    private void showSnackbar() {
        String message = mainActivity.getString(R.string.sb_main_but_empty_trash);
        SnackbarBuilder.makeSnackbar(mainActivity, coordinatorLayout, message, fabMain, false);
    }
}
