package com.loskon.noteminimalism3.auxiliary.note;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;

/**
 * Помощник для управления элементами в NoteActivity
 */

public class NoteHelper {

    private final NoteActivity activity;
    private final EditText editText;
    private final LinearLayout linearNote;

    public NoteHelper(NoteActivity activity) {
        this.activity = activity;
        editText = activity.getEditText();
        linearNote = activity.getLinearNote();
    }

    public void favoriteStatus(boolean isFavItem, MaterialButton button) {
        int icon;

        if (isFavItem) {
            icon = R.drawable.baseline_star_black_24;
        } else {
            icon = R.drawable.baseline_star_border_black_24;
        }

        button.setIcon(ResourcesCompat.getDrawable(activity.getResources(), icon, null));
    }

    public void handlerLongEditTextClick() {
        editText.setOnLongClickListener(view -> {
            activity.goClick();
            return false;
        });
    }

    public void showKeyboard(boolean isShowKeyboard) {
        if (isShowKeyboard) {
            MyKeyboard.showSoftKeyboard(activity, editText);
        } else {
            MyKeyboard.hideSoftKeyboard(activity, editText);
        }

        editText.setCursorVisible(isShowKeyboard);
        setFocusableLayout(!isShowKeyboard);
    }

    public void setFocusableLayout(boolean isFocusable) {
        linearNote.setFocusable(isFocusable);
        linearNote.setFocusableInTouchMode(isFocusable);
    }

    public void handlerEditTextClick() {
        int selNotesCategory = activity.getSelNotesCategory();

        editText.setOnClickListener(view -> {
            if (selNotesCategory == 2) {
                activity.getNoteSbAdapter().show();
            }
        });
    }

    public void redrawingForTrash() {
        // Изменение вида заметки для мусорки
        editText.setClickable(true);
        editText.setCursorVisible(false);
        editText.setFocusable(false);

        activity.getBtnFav().setVisibility(View.INVISIBLE);
        activity.getBtnMore().setVisibility(View.INVISIBLE);

        activity.getFabNote().setImageResource(R.drawable.baseline_restore_from_trash_black_24);
        activity.getBtnDel().setIcon(ResourcesCompat.getDrawable(activity.getResources(),
                R.drawable.baseline_delete_forever_black_24, null));
    }
}
