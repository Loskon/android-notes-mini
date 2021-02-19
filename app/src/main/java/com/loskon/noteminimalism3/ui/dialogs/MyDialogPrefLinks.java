package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;

public class MyDialogPrefLinks {

    private final Activity activity;
    private AlertDialog alertDialog;
    private CheckBox checkBoxWeb, checkBoxMail, checkBoxPhone;

    public MyDialogPrefLinks(Activity activity) {
        this.activity = activity;
    }

    public void callDialog() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_pref_links);
        alertDialog.show();

        checkBoxWeb = alertDialog.findViewById(R.id.checkBoxWeb);
        checkBoxMail = alertDialog.findViewById(R.id.checkBoxMail);
        checkBoxPhone = alertDialog.findViewById(R.id.checkBoxPhone);
        Button button = alertDialog.findViewById(R.id.button);

        int color = MyColor.getColorCustom(activity);
        checkBoxWeb.setButtonTintList(ColorStateList.valueOf(color));
        checkBoxMail.setButtonTintList(ColorStateList.valueOf(color));
        checkBoxPhone.setButtonTintList(ColorStateList.valueOf(color));
        button.setBackgroundColor(color);

        checkBoxWeb.setChecked(GetSharedPref.isWeb(activity));
        checkBoxMail.setChecked(GetSharedPref.isMail(activity));
        checkBoxPhone.setChecked(GetSharedPref.isPhone(activity));

        checkBoxWeb.setOnClickListener(onClickListener);
        checkBoxMail.setOnClickListener(onClickListener);
        checkBoxPhone.setOnClickListener(onClickListener);
        button.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.button) {
                alertDialog.dismiss();
            }

            MySharedPref.setBoolean(activity, MyPrefKey.KEY_WEB, checkBoxWeb.isChecked());
            MySharedPref.setBoolean(activity, MyPrefKey.KEY_MAIL, checkBoxMail.isChecked());
            MySharedPref.setBoolean(activity, MyPrefKey.KEY_PHONE, checkBoxPhone.isChecked());
        }
    };

}
