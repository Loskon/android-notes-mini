package com.loskon.noteminimalism3.ui.snackbars;

import android.graphics.Color;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.ui.activities.NoteActivity;

/**
 * Кастомный Snackbar
 */

public class MySnackbarNoteReset {

    private final NoteActivity activity;

    private final ConstraintLayout cstLayNote;
    private final FloatingActionButton fabNote;

    public MySnackbarNoteReset(NoteActivity activity, ConstraintLayout cstLayNote) {
        this.activity = activity;
        this.cstLayNote = cstLayNote;
        fabNote = activity.getFabNote();
    }

    public void show() {
        Snackbar.make(
                cstLayNote,
                activity.getString(R.string.sb_note_in_trash),
                Snackbar.LENGTH_SHORT)
                .setAnchorView(fabNote)
                .setAction(activity.getString(R.string.sb_note_restore), view ->
                        activity.restoreNote())
                .setTextColor(Color.WHITE)
                .setBackgroundTint(MyColor.getColorBackgroundSnackbar(activity))
                .setActionTextColor(MyColor.getMyColor(activity))
                .show();
    }
}
