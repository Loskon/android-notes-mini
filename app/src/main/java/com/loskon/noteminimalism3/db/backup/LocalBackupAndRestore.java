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
import com.loskon.noteminimalism3.db.backup.dialog.DialogBackup;
import com.loskon.noteminimalism3.db.backup.dialog.DialogRestore;
import com.loskon.noteminimalism3.ui.Helper.SharedPrefHelper;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import static com.loskon.noteminimalism3.db.backup.Permissions.permissionGranted;

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
    public void performBackup(String path) {

        File folder = new File(path
                + File.separator + activity.getResources().getString(R.string.app_name));

        boolean success = true;

        // exists определяет, существует ли файл или каталог,
        // обозначаемый абстрактным именем файла, или нет.
        if (!folder.exists()) {
            success = folder.mkdirs(); // Создать папку
        }

        if (success) {
            // File name
            String outFileName = path +
                    File.separator + activity.getResources()
                    .getString(R.string.app_name) + File.separator;

            (new DialogBackup(activity)).callDialogBackup(outFileName);
        } else {
            Toast.makeText(activity, "Retry", Toast.LENGTH_SHORT).show();
        }

    }

    // спросите пользователя, какую резервную копию восстановить
    public void performRestore(String path) {

        File folder = new File(path +
                File.separator + activity.getResources().getString(R.string.app_name));

        if (folder.exists()) {
            (new DialogRestore(activity)).callDialogRestore(folder);
        } else {
            Toast.makeText(activity, "Backup folder not present.\n" +
                    "Do a backup before a restore!", Toast.LENGTH_SHORT).show();
        }
    }
}
