package com.loskon.noteminimalism3.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import com.loskon.noteminimalism3.ui.activity.BackupActivity;
import com.loskon.noteminimalism3.ui.activity.MainActivity;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;
import com.loskon.noteminimalism3.ui.activity.SettingsAppActivity;
import com.loskon.noteminimalism3.ui.activity.SettingsActivity;

import static com.loskon.noteminimalism3.helper.RequestCode.REQUEST_CODE_READ;

public class MyIntent {

    private static final int intentDelay = 50;

    // Add new note
    public static void intentAddNewNote(Context context, int selectedNoteMode) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("selectedNoteMode", selectedNoteMode);
        context.startActivity(intent);
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


    public static void goFindFolder(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        fragment.startActivityForResult(Intent.createChooser(intent,
                "Choose directory"), REQUEST_CODE_READ);
    }

    public static void goMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void goMainActivityFromNote(android.app.Activity activity, boolean isButtonClick) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        (new Handler()).postDelayed(() -> activity
                .startActivity(intent), delayForNote(isButtonClick));

    }

    private static int delayForNote(boolean isButtonClick) {
        // Задержка для того, чтобы клавиатура успела закрыться
        if (isButtonClick) {
            return intentDelay;
        } else {
            return 0;
        }
    }

    public static void goSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

}
