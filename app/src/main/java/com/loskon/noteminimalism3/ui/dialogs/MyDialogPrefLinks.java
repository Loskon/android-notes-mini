package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

/**
 * Выбор ссылок, которые будут активны
 */

public class MyDialogPrefLinks {

    private final Activity activity;
    private AlertDialog alertDialog;
    private CheckBox checkBoxWeb, checkBoxMail, checkBoxPhone;

    public MyDialogPrefLinks(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_pref_links);
        alertDialog.show();

        checkBoxWeb = alertDialog.findViewById(R.id.checkBoxWeb);
        checkBoxMail = alertDialog.findViewById(R.id.checkBoxMail);
        checkBoxPhone = alertDialog.findViewById(R.id.checkBoxPhone);
        Button btnOk = alertDialog.findViewById(R.id.dialog_btn_ok);

        int color = MyColor.getColorCustom(activity);
        checkBoxWeb.setButtonTintList(ColorStateList.valueOf(color));
        checkBoxMail.setButtonTintList(ColorStateList.valueOf(color));
        checkBoxPhone.setButtonTintList(ColorStateList.valueOf(color));
        btnOk.setBackgroundColor(color);

        checkBoxWeb.setChecked(GetSharedPref.isWeb(activity));
        checkBoxMail.setChecked(GetSharedPref.isMail(activity));
        checkBoxPhone.setChecked(GetSharedPref.isPhone(activity));

        checkBoxWeb.setOnClickListener(onClickListener);
        checkBoxMail.setOnClickListener(onClickListener);
        checkBoxPhone.setOnClickListener(onClickListener);
        btnOk.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.dialog_btn_ok) {
                alertDialog.dismiss();
            }

            MySharedPref.setBoolean(activity, MyPrefKey.KEY_WEB, checkBoxWeb.isChecked());
            MySharedPref.setBoolean(activity, MyPrefKey.KEY_MAIL, checkBoxMail.isChecked());
            MySharedPref.setBoolean(activity, MyPrefKey.KEY_PHONE, checkBoxPhone.isChecked());
        }
    };

}
