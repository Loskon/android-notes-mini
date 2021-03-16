package com.loskon.noteminimalism3.ui.dialogs;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.backup.prime.BpCloud;
import com.loskon.noteminimalism3.ui.activity.BackupActivity;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;

public class MyDialogData {

    private BackupActivity activity;
    private BpCloud bpCloud;

    private AlertDialog alertDialog;

    private MaterialButton btnYes, btnNo, btnDelete;
    private TextView textView;

    public MyDialogData(BackupActivity activity) {
        this.activity = activity;
        bpCloud = activity.getBpCloud();
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_data);
        alertDialog.show();

        Button btnLogout = alertDialog.findViewById(R.id.btn_data_logout);
        btnDelete = alertDialog.findViewById(R.id.btn_data_delete);
        textView = alertDialog.findViewById(R.id.tv_data_warning);
        btnYes = alertDialog.findViewById(R.id.btn_data_yes);
        btnNo = alertDialog.findViewById(R.id.btn_data_no);
        Button btnCancel = alertDialog.findViewById(R.id.btn_data_cancel);

        int color = MyColor.getMyColor(activity);
        btnLogout.setBackgroundColor(color);
        btnDelete.setBackgroundColor(color);
        btnYes.setTextColor(color);
        btnYes.setStrokeColor(ColorStateList.valueOf(color));
        btnNo.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        setItemVisibility(false);

        btnLogout.setOnClickListener(onClickListener);
        btnDelete.setOnClickListener(onClickListener);
        btnYes.setOnClickListener(onClickListener);
        btnNo.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = view -> {
        int id = view.getId();

        if (activity.getInternetCheck().isConnected()) {
            if (id == R.id.btn_data_logout) {
                bpCloud.signOut();
                alertDialog.dismiss();
            } else if (id == R.id.btn_data_delete) {
                setItemVisibility(true);
            } else if (id == R.id.btn_data_yes) {
                bpCloud.deleteData();
                alertDialog.dismiss();
            } else if (id == R.id.btn_data_no) {
                setItemVisibility(false);
            } else if (id == R.id.btn_data_cancel) {
                alertDialog.dismiss();
            }
        } else {
            alertDialog.dismiss();
            if (id != R.id.btn_data_cancel) MySnackbarBackup
                    .showSnackbar(activity, false, MySnackbarBackup.MSG_TEXT_NO_INTERNET);
        }
    };

    private void setItemVisibility(boolean isVisible) {
        int typeVisible;

        if (isVisible) {
            typeVisible = View.VISIBLE;
        } else {
            typeVisible = View.GONE;
            textView.clearAnimation();
        }

        textView.setVisibility(typeVisible);
        btnYes.setVisibility(typeVisible);
        btnNo.setVisibility(typeVisible);

        btnDelete.setEnabled(!isVisible);
    }
}
