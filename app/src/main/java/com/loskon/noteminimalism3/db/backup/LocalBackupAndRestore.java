package com.loskon.noteminimalism3.db.backup;

import android.app.Activity;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogBackup;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogRestore;

import java.io.File;

/**
 *
 */

public class LocalBackupAndRestore {

    private final Activity activity;
    private String typeMessage;

    public LocalBackupAndRestore(Activity activity) {
        this.activity = activity;
    }

    // запросите у пользователя имя для резервного копирования и выполните его.
    // Резервная копия будет сохранена в пользовательской папке.
    public void performBackup(String path) {

        File folder = getFolder(path);

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

            (new MyDialogBackup(activity)).callDialogBackup(outFileName, folder);
        } else {
            typeMessage = SnackbarBackup.MSG_TEXT_ERROR;
            showSnacbar();
        }

    }

    // спросите пользователя, какую резервную копию восстановить
    public void performRestore(String path) {

        File folder = getFolder(path);

        if (folder.exists()) {
            (new MyDialogRestore(activity)).callDialogRestore(folder);
        } else {
            typeMessage = SnackbarBackup.MSG_TEXT_NO_FOLDER;
           showSnacbar();
        }
    }

    private File getFolder(String path) {
        return new File(path +
                File.separator + activity.getResources().getString(R.string.app_name));
    }

    private void showSnacbar() {
        SnackbarBackup.showSnackbar(activity, false, typeMessage);
    }
}
