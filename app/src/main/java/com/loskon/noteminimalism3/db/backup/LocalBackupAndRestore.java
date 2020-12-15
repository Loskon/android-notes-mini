/*
 *   Copyright 2016 Marco Gomiero
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.loskon.noteminimalism3.db.backup;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loskon.noteminimalism3.R;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

/**
 *
 */

public class LocalBackupAndRestore {

    private final Activity activity;

    public LocalBackupAndRestore(Activity activity) {
        this.activity = activity;
    }

    // запросите у пользователя имя для резервного копирования и выполните его.
    // Резервная копия будет сохранена в пользовательской папке.
    public void performBackup(String outFileName) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory()
                + File.separator + activity.getResources().getString(R.string.app_name));

        boolean success = true;
        // exists определяет, существует ли файл или каталог,
        // обозначаемый абстрактным именем файла, или нет.
        if (!folder.exists()) {
            success = folder.mkdirs(); // Создать папку
        }

        if (success) {
            dialogBackup(outFileName);
        } else {
            Toast.makeText(activity, "Retry", Toast.LENGTH_SHORT).show();
        }

    }

    // спросите пользователя, какую резервную копию восстановить
    public void performRestore() {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + activity.getResources().getString(R.string.app_name));

        if (folder.exists()) {
            dialogRestore(folder);
        } else {
            Toast.makeText(activity, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogBackup(String outFileName) {
        AlertDialog alertDialog =  new MaterialAlertDialogBuilder(activity,
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
        TextInputLayout textInputLayout = alertDialog.findViewById(R.id.textInputLayout2);
        TextInputEditText textInputEditText = alertDialog.findViewById(R.id.textInputEditText);
        Button btnInertDate = alertDialog.findViewById(R.id.button2);
        Button btnOk = alertDialog.findViewById(R.id.button3);
        Button btnCancel = alertDialog.findViewById(R.id.button4);

        // assert
        assert textInputLayout != null;
        assert textInputEditText != null;
        assert btnInertDate != null;
        assert btnOk != null;
        assert btnCancel != null;

        // Показать клавиатуру
        textInputEditText.postDelayed(() -> {
            InputMethodManager keyboard = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(textInputEditText, 0);
        },50);

        btnInertDate.setOnClickListener(view -> {
            // Установить в editText текущую дату
            String time = (DateFormat.getDateTimeInstance(
                    DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
            textInputEditText.setText(time);
            textInputEditText.setSelection(textInputEditText.getEditableText().length());
        });

        btnOk.setOnClickListener(v -> {
            int textLength = textInputEditText.getEditableText().toString().trim().length();

            if (textLength == 0) {
                // Вывести сообщение об ошибке, если editText пуст
                textInputLayout.setError(activity.getString(R.string.enter_name));
                textInputLayout.setErrorEnabled(true);
            } else {

                String titleText = Objects.requireNonNull(textInputEditText.getText()).toString();
                titleText = titleText.replace("/", "_");
                titleText = titleText.replace(".", "_");
                titleText = titleText.replace(":", "-");

                String outText = outFileName + titleText + ".db";
                BackupAndRestoreDatabase.backupDatabase(activity, outText);
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        // Убрать сообщение об ошибке, если введен хотя бы один символ
        Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
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

    private void dialogRestore(File folder) {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(activity,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_import)
                .create();

        // View settings
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        alertDialog.show();

        // initView
        ListView listViewFiles = alertDialog.findViewById(R.id.list_view_files);
        TextView txtTitleRestore = alertDialog.findViewById(R.id.txt_title_restore);
        ImageButton btnClearList = alertDialog.findViewById(R.id.img_btn_clear_list);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel_restore);
        CustomArrayAdapter customArrayAdapter;

        // assert
        assert listViewFiles != null;
        assert txtTitleRestore != null;
        assert btnClearList != null;
        assert btnCancel != null;

        File[] files = folder.listFiles();
        ArrayList<String> listFiles = new ArrayList<>();

        for (File file : files) {
            listFiles.add(file.getName());
        }

        Collections.reverse(listFiles);

        if (listFiles.size() == 0) {
            txtTitleRestore.setVisibility(View.VISIBLE);
            btnClearList.setVisibility(View.INVISIBLE);
        } else {
            txtTitleRestore.setVisibility(View.INVISIBLE);
            btnClearList.setVisibility(View.VISIBLE);
        }

        // Устанавливам адаптер
        customArrayAdapter = new CustomArrayAdapter(activity, listFiles);
        listViewFiles.setAdapter(customArrayAdapter);

        listViewFiles.setOnItemClickListener((adapterView, view, position, l) -> {
            try {
                BackupAndRestoreDatabase.restoreDatabase(activity, files[position].getPath());
            } catch (Exception e) {
                Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
            }
        });

        btnClearList.setOnClickListener(view -> {
            customArrayAdapter.notifyDataSetChanged();
            customArrayAdapter.deleteAll(files);
        });

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        // Callback from CustomArrayAdapter
        customArrayAdapter.regArrAdapterEmpty(() -> {
            txtTitleRestore.setVisibility(View.VISIBLE);
            btnClearList.setVisibility(View.INVISIBLE);
        });
    }

}
