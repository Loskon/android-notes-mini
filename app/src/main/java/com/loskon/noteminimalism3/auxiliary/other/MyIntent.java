package com.loskon.noteminimalism3.auxiliary.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.loskon.noteminimalism3.ui.activity.BackupActivity;
import com.loskon.noteminimalism3.ui.activity.MainActivity;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;
import com.loskon.noteminimalism3.ui.activity.SettingsActivity;
import com.loskon.noteminimalism3.ui.activity.SettingsAppActivity;

import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_READ;

/**
 * Переходы между активностями, откыите различных лаунчеров
 */

public class MyIntent {

    private static final int intentDelay = 50;
    public static final String PUT_EXTRA_SEL_NOTE_CATEGORY = "selNotesCategory";
    public static final String PUT_EXTRA_ID = "id";

    public static void AddNewNote(Context context, int selNotesCategory) {
        // Добавление новой заметки
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(PUT_EXTRA_SEL_NOTE_CATEGORY, selNotesCategory);
        context.startActivity(intent);
    }

    public static void OpenNote(Context context, int selNotesCategory, long id) {
        // Открытие заметки для редактирования
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(PUT_EXTRA_SEL_NOTE_CATEGORY, selNotesCategory);
        intent.putExtra(PUT_EXTRA_ID, id);
        context.startActivity(intent);
    }

    public static void OpenSettings(Context context) {
        // Открытие SettingsActivity
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static void SettingsApp(Context context) {
        // Open SettingsAppActivity
        Intent intent = new Intent(context, SettingsAppActivity.class);
        (new Handler()).postDelayed(() -> {
            context.startActivity(intent);
        }, intentDelay);
    }

    public static void BackupActivity(Context context) {
        // Open BackupActivity
        Intent intent = new Intent(context, BackupActivity.class);
        (new Handler()).postDelayed(() -> context.startActivity(intent), intentDelay);
    }

    public static void openFindFolder(Fragment fragment) {
        // Open documents
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        fragment.startActivityForResult(Intent.createChooser(intent,
                "Choose directory"), REQUEST_CODE_READ);
    }

    public static void goMainActivity(Context context) {
        // Return in MainActivity from SettingsActivity
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void goMainActivityFromNote(Activity activity, boolean isButtonClick) {
        // Return in MainActivity from NoteActivity
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        (new Handler()).postDelayed(() -> activity
                .startActivity(intent), delayForNote(isButtonClick));

    }

    public static void goSettingsActivity(Context context) {
        // Return in SettingsActivity from BackupActivity/SettingsAppActivity
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void sendIntent(Context context, EditText editTitleText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, editTitleText.getText().toString().trim());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent,"share"));
    }

    private static int delayForNote(boolean isButtonClick) {
        // Задержка для того, чтобы клавиатура успела закрыться
        if (isButtonClick) {
            return intentDelay;
        } else {
            return 0;
        }
    }

}
