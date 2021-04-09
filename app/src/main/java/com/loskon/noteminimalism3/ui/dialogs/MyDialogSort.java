package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

public class MyDialogSort {

    private final Activity activity;
    private AlertDialog alertDialog;
    private RadioButton radioButtonCreate, radioButtonMod;
    private int checkedNumber;

    private static CallbackSort callbackSort;

    public static void regCallBackNavIcon(CallbackSort callbackSort) {
        MyDialogSort.callbackSort = callbackSort;
    }

    public MyDialogSort(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_pref_sorting);
        alertDialog.show();

        RadioGroup radioGroup = alertDialog.findViewById(R.id.rg_sort);
        radioButtonCreate = alertDialog.findViewById(R.id.rb_sort_creation);
        radioButtonMod = alertDialog.findViewById(R.id.rb_sort_modification);
        Button btnOk = alertDialog.findViewById(R.id.btn_sort_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_sort_cancel);

        checkedNumber = GetSharedPref.getSort(activity);

        if (checkedNumber == 1) {
            radioGroup.check(R.id.rb_sort_modification);
        } else {
            radioGroup.check(R.id.rb_sort_creation);
        }

        int color = MyColor.getMyColor(activity);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);
        radioButtonCreate.setButtonTintList(ColorStateList.valueOf(color));
        radioButtonMod.setButtonTintList(ColorStateList.valueOf(color));

        radioGroup.setOnCheckedChangeListener((radioGroup1, radioButtonId) -> {
            if (radioButtonId == R.id.rb_sort_modification) {
                checkedNumber = 1;
            } else {
                checkedNumber = 0;
            }
        });

        btnOk.setOnClickListener(view -> {
            MySharedPref.setInt(activity, MyPrefKey.KEY_SORT, checkedNumber);
            if (callbackSort != null) callbackSort.onCallBack();
            alertDialog.dismiss();
        });

        btnCancel.setOnClickListener(view -> alertDialog.dismiss());
    }

    public interface CallbackSort {
        void onCallBack();
    }
}
