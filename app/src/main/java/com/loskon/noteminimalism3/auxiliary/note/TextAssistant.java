package com.loskon.noteminimalism3.auxiliary.note;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.EditText;

import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsInActivity;
import com.loskon.noteminimalism3.ui.activities.NoteActivity;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteMessage;

/**
 * Помощник для работы с текстом
 */

public class TextAssistant {

    private final NoteActivity activity;
    private final EditText editText;

    public TextAssistant(NoteActivity activity) {
        this.activity = activity;
        editText = activity.getEditText();
    }

    public void pasteText() {
        // Вставить текст
        ClipboardManager clipboard = (ClipboardManager)
                activity.getSystemService(Context.CLIPBOARD_SERVICE);

        if ((clipboard.hasPrimaryClip())) {
            goPasteText(clipboard);
        } else {
            showSnackbarNeedCopy();
        }
    }

    private void showSnackbarNeedCopy() {
        showSnackbar(false, MySnackbarNoteMessage.MSG_NEED_COPY_TEXT);
    }

    private void goPasteText(ClipboardManager clipboard) {
        String title = getTitleTrim();
        String pasteText;

        try {
            CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();

            if (!textToPaste.toString().trim().isEmpty()) {
                if (title.isEmpty()) {
                    title = (String) textToPaste;
                } else {
                    title = title + "\n\n" + textToPaste;
                }

                pasteText = title.trim();

                editText.setText(pasteText);
                editText.setSelection(pasteText.length());
            } else {
                showSnackbarNeedCopy();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            showSnackbar(false, MySnackbarNoteMessage.MSG_INVALID_FORMAT);
        }
    }

    private String getTitleTrim() {
        return editText.getText().toString().trim();
    }

    private void showSnackbar(boolean isSuccess, String message) {
        activity.getMySnackbarNoteMessage().show(isSuccess, message);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void copyText() {
        String title = getTitleTrim();

        if (!title.isEmpty()) {
            goCopyText();
        } else {
            showSnackbar(false, MySnackbarNoteMessage.MSG_NOTE_IS_EMPTY);
        }
    }

    private void goCopyText() {
        try {
            ClipboardManager clipboard = (ClipboardManager)
                    activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", editText.getText().toString());
            clipboard.setPrimaryClip(clip);
            showSnackbar(true, MySnackbarNoteMessage.MSG_NOTE_TEXT_COPIED);
        } catch (Exception exception) {
            exception.printStackTrace();
            showSnackbar(false,"ERROR");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void saveTextFile() {
        boolean isAccess = new PermissionsInActivity().isAccess(activity);

        if (isAccess) goSaveTextFile();
    }

    public void goSaveTextFile() {
        String string = getTitleTrim();

        if (!string.isEmpty()) {
            (new TextFile(activity)).createTextFile(string);
        } else {
            showSnackbar(false, MySnackbarNoteMessage.MSG_NOTE_IS_EMPTY);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void sendText() {
        String title = getTitleTrim();

        if (!title.isEmpty()) {
            goSendText();
        } else {
            showSnackbar(false, MySnackbarNoteMessage.MSG_NOTE_IS_EMPTY);
        }
    }

    private void goSendText() {
        try {
            MyIntent.startShareText(activity, editText);
        } catch (Exception exception) {
            exception.printStackTrace();
            showSnackbar(false, "ERROR");
        }
    }
}
