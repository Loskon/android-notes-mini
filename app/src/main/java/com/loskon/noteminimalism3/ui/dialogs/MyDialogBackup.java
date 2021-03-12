package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.note.MyKeyboard;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyDate;
import com.loskon.noteminimalism3.auxiliary.other.ReplaceText;
import com.loskon.noteminimalism3.backup.second.BackupSetName;

import java.util.Date;

/**
 * Создание имени для файла бэкапа
 */

public class MyDialogBackup {

    private final Activity activity;

    private AlertDialog alertDialog;

    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;

    public MyDialogBackup(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_backup);
        alertDialog.show();

        // initView
        textInputLayout = alertDialog.findViewById(R.id.textInputLayout2);
        textInputEditText = alertDialog.findViewById(R.id.textInputEditText);
        Button btnOk = alertDialog.findViewById(R.id.btn_ok_backup);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel_backup);

        // assert
        assert btnOk != null;
        assert btnCancel != null;

        // color
        int color = MyColor.getMyColor(activity);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);
        textInputLayout.setBoxStrokeColor(color);
        textInputLayout.setHintTextColor(ColorStateList.valueOf(color));

        // settings
        MyKeyboard.showSoftKeyboardInput(activity, textInputEditText);
        checkTextChanged();
        setDateInEditText();

        btnOk.setOnClickListener(v -> clickOk());
        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void checkTextChanged() {
        // Убрать сообщение об ошибке, если введен хотя бы один символ
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.toString().trim().length() >= 1) {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setDateInEditText() {
        // Установить в editText текущую дату
        textInputEditText.setText(MyDate.getNowDate(new Date()));
        textInputEditText.setSelection(textInputEditText.getEditableText().length());
    }

    private void clickOk() {
        int textLength = textInputEditText.getEditableText().toString().trim().length();

        if (textLength == 0) {
            // Вывести сообщение об ошибке, если editText пуст
            textInputLayout.setError(activity.getString(R.string.dg_bp_error));
            textInputLayout.setErrorEnabled(true);
        } else {
            // Создать имя бэкапа
            String backupName = textInputEditText.getText().toString();
            backupName = ReplaceText.replaceForSaveTittle(backupName);
            (new BackupSetName(activity)).callBackup(false, backupName);
            alertDialog.dismiss();
        }
    }
}
