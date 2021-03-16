package com.loskon.noteminimalism3.auxiliary.note;

import android.graphics.Color;
import android.widget.EditText;

import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.rv.other.CustomMovementMethod;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogLinks;

public class NoteHelperLinks {

    private final NoteActivity activity;
    private final EditText editText;
    //private LinearLayout linearNote;
    private boolean isEditMode = false;


    public NoteHelperLinks(NoteActivity activity) {
        this.activity = activity;
        editText = activity.getEditText();
        //linearNote = activity.linearNote;
    }

    public void setLinks() {
        editText.setAutoLinkMask(getTypeLinks());
        setColorLinks(MyColor.getMyColor(activity));

        editText.setMovementMethod(new CustomMovementMethod() {
            @Override
            public void onLinkClick(String url) {
                if (!isEditMode) setLinkClick(url);
            }

            @Override
            public void onEmptyClick() {
                activity.onOutClick();
            }
        });

    }

    private void setColorLinks(int color) {
        editText.setLinkTextColor(color);
    }

    private void setLinkClick(String url) {
            delFocusFromLinks();
            (new MyDialogLinks(activity, url)).call();
    }

    private int getTypeLinks() {
        int typeLinks;

        if (activity.getNoteId() == 0) {
            typeLinks = 0;
        } else {
            typeLinks = MyLinkify.Web(activity) | MyLinkify
                    .Mail(activity) | MyLinkify.Phone(activity);
        }

        return typeLinks;
    }

    public void changeColorLinks() {
        if (getTypeLinks() != 0) {
            if (!isEditMode) {
                isEditMode = true;
                setColorLinks(getColorEditableLinks());
            }
        }
    }

    private int getColorEditableLinks() {
        boolean isDarkMode = MyColor.isDarkMode(activity);
        int color;

        if (isDarkMode) {
            color = Color.WHITE;
        } else {
            color = Color.BLACK;
        }

        return color;
    }

    public void delFocusFromLinks() {
        activity.getNoteHelper().showKeyboard(false);
        editText.clearFocus(); // Убираем фокус с editText
        activity.getLinearNote().requestFocus(); // Делаем запрос на фокусированное состояние linearLayout
    }
}
