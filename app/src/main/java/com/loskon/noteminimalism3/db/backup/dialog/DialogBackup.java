package com.loskon.noteminimalism3.db.backup.dialog;

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
import com.loskon.noteminimalism3.ui.Helper.BackupHelper;
import com.loskon.noteminimalism3.ui.Helper.DateHelper;

import java.util.Date;
import java.util.Objects;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

public class DialogBackup {

    private final Context context;
    private AlertDialog alertDialog;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private String outFileName;

    public DialogBackup (Context context) {
        this.context = context;
    }

    public void callDialogBackup(String outFileName) {
        this.outFileName = outFileName;

        alertDialog =  new MaterialAlertDialogBuilder(context,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_backup)
                .create();

        // View settings
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.setCancelable(false);

        alertDialog.show();

        // initView
        textInputLayout = alertDialog.findViewById(R.id.textInputLayout2);
        textInputEditText = alertDialog.findViewById(R.id.textInputEditText);
        Button btnOk = alertDialog.findViewById(R.id.button3);
        Button btnCancel = alertDialog.findViewById(R.id.button4);

        // assert
        assert btnOk != null;
        assert btnCancel != null;

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
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(textInputEditText, 0);
        },50);
    }

    private void setDateInEditText() {
        // Установить в editText текущую дату
        textInputEditText.setText(DateHelper.getNowDate(new Date()));
        textInputEditText.setSelection(textInputEditText.getEditableText().length());
    }

    private void clickOk() {
        int textLength = textInputEditText.getEditableText().toString().trim().length();

        if (textLength == 0) {
            // Вывести сообщение об ошибке, если editText пуст
            textInputLayout.setError(context.getString(R.string.enter_name));
            textInputLayout.setErrorEnabled(true);
        } else {

            String titleText = Objects.requireNonNull(textInputEditText.getText()).toString();

            titleText = BackupHelper.replaceTextForSave(titleText);

            String outText = outFileName + titleText + ".db";
            BackupAndRestoreDatabase.backupDatabase(context, outText);

            alertDialog.dismiss();
        }
    }
}
