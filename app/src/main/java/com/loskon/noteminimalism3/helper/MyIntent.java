package com.loskon.noteminimalism3.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.loskon.noteminimalism3.ui.activity.BackupActivity;
import com.loskon.noteminimalism3.ui.activity.MainActivity;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;
import com.loskon.noteminimalism3.ui.activity.SettingsAppActivity;
import com.loskon.noteminimalism3.ui.activity.settings.SettingsActivity;

public class MyIntent {

    private static final int intentDelay = 50;
    private static final int READ_REQUEST_CODE = 297;

    // Add new note
    public static Intent intentAddNewNote(Context context, int selectedNoteMode) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("selectedNoteMode", selectedNoteMode);
        return intent;
    }

    // Open note for editing
    public static Intent intentOpenNote(Context context, int selNotesCategory, long id) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("selectedNoteMode", selNotesCategory);
        intent.putExtra("id", id);
        return intent;
    }

    // Open SettingsAppActivity
    public static void intentSettingsApp(Context context) {
        Intent intent = new Intent(context, SettingsAppActivity.class);
        (new Handler()).postDelayed(() -> {
            context.startActivity(intent);
        }, intentDelay);
    }

    // Open BackupActivity
    public static void intentBackupActivity(Context context) {
        Intent intent = new Intent(context, BackupActivity.class);
        (new Handler()).postDelayed(() -> {
            context.startActivity(intent);
        }, intentDelay);
    }


    public static Intent goFindFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        return intent;
    }

    public static void goMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void goMainActivityFromNote(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void goSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

}
