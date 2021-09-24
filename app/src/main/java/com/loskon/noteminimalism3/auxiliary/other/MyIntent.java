package com.loskon.noteminimalism3.auxiliary.other;

import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_READ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.model.Note2;
import com.loskon.noteminimalism3.ui.activities.BackupActivity;
import com.loskon.noteminimalism3.ui.activities.FontsActivity;
import com.loskon.noteminimalism3.ui.activities.MainActivity;
import com.loskon.noteminimalism3.ui.activities.NoteActivity;
import com.loskon.noteminimalism3.ui.activities.NoteActivityKt;
import com.loskon.noteminimalism3.ui.activities.SettingsActivity;
import com.loskon.noteminimalism3.ui.activities.SettingsAppActivity;
import com.loskon.noteminimalism3.ui.activities.update.NoteActivityUpdate;

/**
 * Переходы между активностями, откыите различных лаунчеров
 */

public class MyIntent {

    public static final String PUT_EXTRA_SEL_NOTE_CATEGORY = "selNotesCategory";
    public static final String PUT_EXTRA_ID = "id";
    public static final String PUT_EXTRA_POSITION = "postion";
    public static final String PUT_IS_WIDGET = "isWidget";

    public static final String PUT_EXTRA_NOTE = "put_extra_note";
    public static final String PUT_EXTRA_CATEGORY = "put_extra_category";

    private static final int intentDelay = 50;

    public static void addNewNote(Context context, int selNotesCategory) {
        // Добавление новой заметки
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(PUT_EXTRA_SEL_NOTE_CATEGORY, selNotesCategory);
        context.startActivity(intent);
    }

    public static void addNewNote2(Context context, Note2 note, String category) {
        Intent intent = new Intent(context, NoteActivityKt.class);
        intent.putExtra(PUT_EXTRA_NOTE, note);
        intent.putExtra(PUT_EXTRA_CATEGORY, category);
        context.startActivity(intent);
    }

    public static void addNewNoteUpdate(Context context, Note2 note, String category) {
        Intent intent = new Intent(context, NoteActivityUpdate.class);
        intent.putExtra(PUT_EXTRA_NOTE, note);
        intent.putExtra(PUT_EXTRA_CATEGORY, category);
        context.startActivity(intent);
    }

    public static void openNote(Context context, int selNotesCategory, long id, int position) {
        // Открытие заметки для редактирования
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(PUT_EXTRA_SEL_NOTE_CATEGORY, selNotesCategory);
        intent.putExtra(PUT_EXTRA_ID, id);
        intent.putExtra(PUT_EXTRA_POSITION, position);
        context.startActivity(intent);
    }

    public static void openSettings(Context context) {
        // Открытие SettingsActivity
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static void openSettingsApp(Context context) {
        // Open SettingsAppActivity
        Intent intent = new Intent(context, SettingsAppActivity.class);
        (new Handler()).postDelayed(() -> context.startActivity(intent), intentDelay);
    }

    public static void openBackupActivity(Context context) {
        // Open BackupActivity
        Intent intent = new Intent(context, BackupActivity.class);
        (new Handler()).postDelayed(() -> context.startActivity(intent), intentDelay);
    }

    public static void openFonts(Context context) {
        // Открытие SettingsActivity
        Intent intent = new Intent(context, FontsActivity.class);
        context.startActivity(intent);
    }

    public static void startFindFolder(Fragment fragment) {
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

    public static void goSettingsActivityClear(Context context) {
        // Return in SettingsActivity from BackupActivity/SettingsAppActivity
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void startShareText(Context context, EditText editTitleText) {
        // Share text from Note
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, editTitleText.getText().toString().trim());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "share"));
    }

    public static void startMailClient(Context context) {
        // Open email client
        String email = context.getString(R.string.developer_email);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback));
        context.startActivity(intent);
    }

    public static void addNewNoteFromWidget(Context context) {
        // Добавление новой заметки
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(PUT_EXTRA_SEL_NOTE_CATEGORY, 0);
        intent.putExtra(PUT_IS_WIDGET, true);
        intent.putExtra(PUT_EXTRA_ID, 0);
        context.startActivity(intent);
    }

    private static int delayForNote(boolean isButtonClick) {
        // Задержка для того, чтобы клавиатура успела закрыться
        if (isButtonClick) {
            return 100;
        } else {
            return 0;
        }
    }
}
