package com.loskon.noteminimalism3.ui.snackbars;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;

public class MySnackbarNote {

    private final Activity activity;
    private final View layout;
    private final View anchorView;

    private static CallbackRestoreNote callbackRestoreNote;

    public void registerCallBackNote(CallbackRestoreNote callbackRestoreNote) {
        MySnackbarNote.callbackRestoreNote = callbackRestoreNote;
    }

    public MySnackbarNote(Activity activity, View layout, View anchorView) {
        this.activity = activity;
        this.layout = layout;
        this.anchorView = anchorView;
    }

    public void showSnackbarReset() {
        Snackbar.make(
                layout,
                activity.getString(R.string.snackbar_note_text_note_in_trash),
                Snackbar.LENGTH_SHORT)
                .setAnchorView(anchorView)
                .setAction(activity.getString(R.string.snackbar_note_btn_action), view -> {
                    if (callbackRestoreNote != null) {
                        callbackRestoreNote.callingBackRestoreNote();
                    }
                })
                .setTextColor(Color.WHITE)
                .setBackgroundTint(MyColor.getColorBackgroundSnackbar(activity))
                .setActionTextColor(MyColor.getColorCustom(activity))
                .show();
    }

    public void showSnackbarErrors(boolean isPermissionGranted) {
        String message;

        if (isPermissionGranted) {
            message = activity.getString(R.string.snackbar_note_text_note_is_empty);
        } else {
            message = activity.getString(R.string.no_permissions);
        }

        SnackbarBuilder.makeSnackbar(activity, layout, message, anchorView, false);
    }

    public interface CallbackRestoreNote {
        void callingBackRestoreNote();
    }
}
