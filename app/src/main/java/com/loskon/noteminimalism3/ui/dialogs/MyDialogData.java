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

    private MaterialButton btnOk7Yes, btnOk6No, btnOk2;
    private TextView textView;

    public MyDialogData(BackupActivity activity) {
        this.activity = activity;
        bpCloud = activity.getBpCloud();
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_data);
        alertDialog.show();

        Button btnOk1 = alertDialog.findViewById(R.id.button2);
        btnOk2 = alertDialog.findViewById(R.id.button3);
        textView = alertDialog.findViewById(R.id.textView3);
        btnOk7Yes = alertDialog.findViewById(R.id.button7);
        btnOk6No = alertDialog.findViewById(R.id.button6);
        Button btnCancel = alertDialog.findViewById(R.id.button5);

        int color = MyColor.getMyColor(activity);
        btnOk1.setBackgroundColor(color);
        btnOk2.setBackgroundColor(color);
        btnOk7Yes.setTextColor(color);
        btnOk7Yes.setStrokeColor(ColorStateList.valueOf(color));
        btnOk6No.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        setItemVisibility(false);

        btnOk1.setOnClickListener(onClickListener);
        btnOk2.setOnClickListener(onClickListener);
        btnOk7Yes.setOnClickListener(onClickListener);
        btnOk6No.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = view -> {
        int id = view.getId();

        if (activity.getInternetCheck().isConnected()) {
            if (id == R.id.button2) {
                bpCloud.signOut();
                alertDialog.dismiss();
            } else if (id == R.id.button3) {
                setItemVisibility(true);
            } else if (id == R.id.button7) {
                bpCloud.deleteData();
                alertDialog.dismiss();
            } else if (id == R.id.button6) {
                setItemVisibility(false);
            } else if (id == R.id.button5) {
                alertDialog.dismiss();
            }
        } else {
            alertDialog.dismiss();
            if (id != R.id.button5) MySnackbarBackup
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
        btnOk7Yes.setVisibility(typeVisible);
        btnOk6No.setVisibility(typeVisible);

        btnOk2.setEnabled(!isVisible);
    }
}
