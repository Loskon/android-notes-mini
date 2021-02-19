package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.second.BackupSetName;
import com.loskon.noteminimalism3.helper.GetDate;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.ReplaceText;

import java.util.Date;
import java.util.Objects;

public class MyDialogBackup {

    private final Activity activity;

    private AlertDialog alertDialog;

    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;

    public MyDialogBackup(Activity activity) {
        this.activity = activity;
    }

    public void callDialogBackup() {
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
        int color = MyColor.getColorCustom(activity);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

        textInputLayout.setBoxStrokeColor(color);
        textInputLayout.setHintTextColor(ColorStateList.valueOf(color));

        showKeyboard();
        setDateInEditText();

        btnOk.setOnClickListener(v -> {
            clickOk();
        });

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        checkText(textInputLayout);
    }

    private void checkText(TextInputLayout textInputLayout) {
        // Убрать сообщение об ошибке, если введен хотя бы один символ
        Objects.requireNonNull(textInputLayout.
                getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.toString().trim().length() >= 1 ) {
                    textInputLayout.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void showKeyboard() {
        // Показать клавиатуру
        textInputEditText.postDelayed(() -> {
            InputMethodManager keyboard = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(textInputEditText, 0);
        },50);
    }

    private void setDateInEditText() {
        // Установить в editText текущую дату
        textInputEditText.setText(GetDate.getNowDate(new Date()));
        textInputEditText.setSelection(textInputEditText.getEditableText().length());
    }

    private void clickOk() {
        int textLength = textInputEditText.getEditableText().toString().trim().length();



        if (textLength == 0) {
            // Вывести сообщение об ошибке, если editText пуст
            textInputLayout.setError(activity.getString(R.string.dialog_backup_error_message));
            textInputLayout.setErrorEnabled(true);
        } else {

            String titleText = Objects.requireNonNull(textInputEditText.getText()).toString();

            titleText = ReplaceText.replaceForSaveTittle(titleText);

            (new BackupSetName(activity)).callBackup(titleText);

            alertDialog.dismiss();
        }
    }


}
