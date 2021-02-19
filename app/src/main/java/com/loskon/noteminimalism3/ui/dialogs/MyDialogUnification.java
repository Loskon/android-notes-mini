package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;

public class MyDialogUnification {

    private final Activity activity;
    private AlertDialog alertDialog;

    private static CallbackUni callbackUni;

    public void registerCallBackUni(CallbackUni callbackUni) {
        MyDialogUnification.callbackUni = callbackUni;
    }

    public MyDialogUnification(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_unification);
        alertDialog.show();

        Button btnOk = alertDialog.findViewById(R.id.button3678);
        Button btnCancel = alertDialog.findViewById(R.id.button4678);

        assert btnOk != null;
        assert btnCancel != null;

        int color = MyColor.getColorCustom(activity);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        btnOk.setOnClickListener(view -> {
            callbackUni.callingBackUni();
            alertDialog.dismiss();
        });

        // Click cancel
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    public interface CallbackUni{
        void callingBackUni();
    }
}
