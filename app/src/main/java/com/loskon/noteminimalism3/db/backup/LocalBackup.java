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
import android.os.Environment;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.R;

import java.io.File;

public class LocalBackup {

    private final Activity activity;

    public LocalBackup(Activity activity) {
        this.activity = activity;
    }

    // запросите у пользователя имя для резервного копирования и выполните его.
    // Резервная копия будет сохранена в пользовательской папке.
    public void performBackup(final String outFileName) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));

        boolean success = true;
        if (!folder.exists())
            success = folder.mkdirs();
        if (success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Backup Name");
            final EditText input = new EditText(activity);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Save", (dialog, which) -> {
                String m_Text = input.getText().toString();
                String out = outFileName + m_Text + ".db";
                BackupAndRestoreDatabase.backupDatabase(activity, out);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        } else
            Toast.makeText(activity, "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();
    }

    //ask to the user what backup to restore
    public void performRestore() {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
            builderSingle.setTitle("Restore:");
            builderSingle.setNegativeButton(
                    "cancel",
                    (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(
                    arrayAdapter,
                    (dialog, which) -> {
                        try {
                            BackupAndRestoreDatabase.restoreDatabase(activity, files[which].getPath());
                        } catch (Exception e) {
                            Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
        } else
            Toast.makeText(activity, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }

}
