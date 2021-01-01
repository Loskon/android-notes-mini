package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.backup.BackupAndRestoreDatabase;
import com.loskon.noteminimalism3.helper.BackupLimiter;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.ReplaceText;
import com.loskon.noteminimalism3.helper.GetDate;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

public class MyDialogBackup {

    private final Activity activity;
    private AlertDialog alertDialog;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private String outFileName;
    private File folder;

    public MyDialogBackup(Activity activity) {
        this.activity = activity;
    }

    public void callDialogBackup(String outFileName, File folder) {
        this.outFileName = outFileName;
        this.folder = folder;

        alertDialog =  new MaterialAlertDialogBuilder(activity,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_backup)
                .create();

        // View settings
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        alertDialog.show();

        // initView
        textInputLayout = alertDialog.findViewById(R.id.textInputLayout2);
        textInputEditText = alertDialog.findViewById(R.id.textInputEditText);
        Button btnOk = alertDialog.findViewById(R.id.button3);
        Button btnCancel = alertDialog.findViewById(R.id.button4);

        // assert
        assert btnOk != null;
        assert btnCancel != null;

        int color = MyColor.getColorCustom(activity);
        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);

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

            String outText = outFileName + titleText  + ".db";

            BackupAndRestoreDatabase.backupDatabase(activity, outText);

            BackupLimiter.purgeLogFiles(folder); // Удаление лишних файлов

            alertDialog.dismiss();
        }
    }
}
