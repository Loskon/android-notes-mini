package com.loskon.noteminimalism3.db.backup;

import android.app.Activity;

import java.io.File;

public class BackupSetName {

    private final Activity activity;

    public BackupSetName(Activity activity) {
        this.activity = activity;
    }

    public void callBackup(String titleText) {
        File folder = BackupPath.getFolder(activity);
        String outFileName = BackupPath.getPathToFiles(activity);

        String outText = outFileName + titleText  + ".db";
        BackupDb.backupDatabase(activity, outText);
        BackupLimiter.delExtraFiles(activity, folder);
    }
}
