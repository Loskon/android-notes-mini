package com.loskon.noteminimalism3.ui.Helper;

import android.content.Context;
import android.content.Intent;

import com.loskon.noteminimalism3.ui.activity.NoteActivity;

public class BackupHelper {

    public static String replaceTextForSave(String titleText) {
        titleText = titleText.replace("/", "_");
        titleText = titleText.replace(".", "_");
        titleText = titleText.replace(":", "-");
        return titleText;
    }

}
