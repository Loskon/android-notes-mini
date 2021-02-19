package com.loskon.noteminimalism3.helper;

import android.app.Activity;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.ui.snackbars.SnackbarBuilder;

import java.io.File;
import java.io.FileWriter;

public class GetTextFile {

    private final Activity activity;

    public GetTextFile(Activity activity) {
        this.activity = activity;
    }

    public void createTextFile(String text) {


            boolean isFolderNoteCreated = BackupPath.createNoteFolder(activity);

            if (isFolderNoteCreated) {

                File file = new File(BackupPath.getFolder(activity), "Text Files");

                boolean isFolderTextCreated = BackupPath.createTextFolder(file);

                if (isFolderTextCreated) {
                    try {

                        String saveText = text;
                        text = text.trim().substring(0, Math.min(10, text.trim().length()));

                        File fileName = new File(file, text);
                        FileWriter writer = new FileWriter(fileName);

                        writer.append(saveText);
                        writer.flush();
                        writer.close();

                        SnackbarBuilder.makeSnackbar(activity, activity.findViewById(R.id.coordinatorLayout),
                                activity.getString(R.string.snackbar_note_save_txt), activity.findViewById(R.id.fabNote), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

    }
}
