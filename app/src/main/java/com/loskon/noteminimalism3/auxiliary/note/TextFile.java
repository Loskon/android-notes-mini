package com.loskon.noteminimalism3.auxiliary.note;

import com.loskon.noteminimalism3.backup.second.AppFolder;
import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteMessage;

import java.io.File;
import java.io.FileWriter;

/**
 * Создание папки и сохранние текстового файла
 */

public class TextFile {

    private final NoteActivity activity;

    private boolean isSuccess = false;
    private String message;

    public TextFile(NoteActivity activity) {
        this.activity = activity;
    }

    public void createTextFile(String title) {

        boolean isFolderNoteCreated = AppFolder.createBackupFolder(activity);

        if (isFolderNoteCreated) {

            File file = new File(BackupPath.getFolder(activity), "Text Files");

            boolean isFolderTextCreated = AppFolder.createTextFilesFolder(file);

            if (isFolderTextCreated) {
                try {
                    String saveText = title;
                    title = title.trim().substring(0, Math.min(16, title.trim().length()));
                    title = title.replace("https://", "").replace("http://", "");

                    File fileName = new File(file, title);
                    FileWriter writer = new FileWriter(fileName);

                    writer.append(saveText);
                    writer.flush();
                    writer.close();

                    isSuccess = true;
                    message = MySnackbarNoteMessage.MSG_SAVE_TXT_COMPLETED;
                } catch (Exception exception) {
                    exception.printStackTrace();
                    message = MySnackbarNoteMessage.MSG_SAVE_TXT_FAILED;
                }
            }
            activity.getMySnackbarNoteMessage().show(isSuccess, message);
        }
    }
}
