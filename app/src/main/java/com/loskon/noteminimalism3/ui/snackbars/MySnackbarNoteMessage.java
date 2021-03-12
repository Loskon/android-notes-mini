package com.loskon.noteminimalism3.ui.snackbars;

import android.os.Handler;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;

/**
 * Помощник для отображения различнх уведомлений о действиях пользователя
 * или о возниккших ошибках
 */

public class MySnackbarNoteMessage {

    public static final String MSG_TEXT_NO_PERMISSION_NOTE = "msg_no_permission";
    public static final String MSG_NOTE_IS_EMPTY = "msg_note_is_empty";
    public static final String MSG_SAVE_TXT_COMPLETED = "msg_save_txt_completed";
    public static final String MSG_SAVE_TXT_FAILED = "msg_save_txt_failed";
    public static final String MSG_INVALID_LINK = "msg_invalid_link";
    public static final String MSG_NEED_COPY_TEXT = "msg_need_copy_text";
    public static final String MSG_INVALID_FORMAT = "msg_invalid_format";

    private final NoteActivity noteActivity;

    private final ConstraintLayout cstLayNote;
    private final FloatingActionButton fabNote;

    public MySnackbarNoteMessage(NoteActivity noteActivity, ConstraintLayout cstLayNote) {
        this.noteActivity = noteActivity;
        this.cstLayNote = cstLayNote;
        fabNote = noteActivity.getFabNote();
    }

    public void show(boolean isSuccess, String typeMessage) {
        String message;

        switch (typeMessage) {
            case MSG_TEXT_NO_PERMISSION_NOTE:
                message = noteActivity.getString(R.string.no_permissions);
                break;
            case MSG_NOTE_IS_EMPTY:
                message = noteActivity.getString(R.string.sb_note_is_empty);
                break;
            case MSG_SAVE_TXT_COMPLETED:
                message = noteActivity.getString(R.string.sb_note_save_txt_completed);
                break;
            case MSG_SAVE_TXT_FAILED:
                message = noteActivity.getString(R.string.sb_note_save_txt_failed);
                break;
            case MSG_INVALID_LINK:
                message = noteActivity.getString(R.string.sb_note_invalid_link);
                break;
            case MSG_NEED_COPY_TEXT:
                message = noteActivity.getString(R.string.sb_note_need_copy_text);
                break;
            case MSG_INVALID_FORMAT:
                message = noteActivity.getString(R.string.sb_note_invalid_format);
                break;
            default:
                message = noteActivity.getString(R.string.unknown_error);
                break;
        }

        (new Handler()).postDelayed(() ->
                SnackbarBuilder.makeSnackbar(noteActivity,
                        cstLayNote, message, fabNote, isSuccess), 150);
    }
}
