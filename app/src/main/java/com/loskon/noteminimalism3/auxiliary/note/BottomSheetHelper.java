package com.loskon.noteminimalism3.auxiliary.note;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsStorage;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteMessage;

public class BottomSheetHelper {

    private final NoteActivity activity;

    private FloatingActionButton fabNote;
    private EditText editText;

    private View bottomSheet;
    private BottomSheetBehavior<View> sheetBehavior;

    public BottomSheetHelper(NoteActivity activity) {
        this.activity = activity;
        getSettings();
        callback();
        handlerItem();
    }

    private void getSettings() {
        fabNote = activity.getFabNote();
        editText = activity.getEditText();
        bottomSheet = activity.findViewById(R.id.layout_bottomSheetBehavior);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        sheetBehavior.setHideable(false);
        sheetBehavior.setPeekHeight(0);
    }

    private void callback() {
        // настройка колбэков при изменениях
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {}

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float difference = 1 - slideOffset;
                fabNote.animate().scaleX(difference).scaleY(difference).setDuration(0).start();
            }
        });
    }

    private void handlerItem() {
        TextView tvPaste= bottomSheet.findViewById(R.id.tv_sheet_paste);
        TextView tvCopyAll= bottomSheet.findViewById(R.id.tv_sheet_copy_all_text);
        TextView tvSave = bottomSheet.findViewById(R.id.tv_sheet_save_txt);
        TextView tvShare = bottomSheet.findViewById(R.id.tv_sheet_share);
        TextView tvClose = bottomSheet.findViewById(R.id.tv_sheet_close);

        tvPaste.setOnClickListener(onClickListener);
        tvCopyAll.setOnClickListener(onClickListener);
        tvSave.setOnClickListener(onClickListener);
        tvShare.setOnClickListener(onClickListener);
        tvClose.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            hideBottomSheet();

            if (id == R.id.tv_sheet_copy_all_text) {
                copyText();
            } else if (id == R.id.tv_sheet_paste) {
                pasteText();
            } else if (id == R.id.tv_sheet_save_txt) {
                saveTxtFile();
            } else if (id == R.id.tv_sheet_share) {
                sendText(editText.getText().toString());
            }
        }
    };

    private void copyText() {
        if (!editText.getText().toString().isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", editText.getText().toString());
            clipboard.setPrimaryClip(clip);
        }
    }

    private void saveTxtFile() {
        boolean isPermissions = PermissionsStorage
                .verify(activity, null, true);
        if (isPermissions) {
            createTextFile();
        }
    }

    private void pasteText() {
        String string = editText.getText().toString();
        ClipboardManager clipboard = (ClipboardManager)
                activity.getSystemService(Context.CLIPBOARD_SERVICE);

        if ((clipboard.hasPrimaryClip())) {
            try {
                CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();

                if (string.isEmpty()) {
                    string = (String) textToPaste;
                } else {
                    string = string + "\n\n" + textToPaste;
                }

                editText.setText(string.trim());
                editText.setSelection(string.length());
            } catch (Exception exception) {
                exception.printStackTrace();
                showSnackbar(MySnackbarNoteMessage.MSG_INVALID_FORMAT);
            }
        } else {
            showSnackbar(MySnackbarNoteMessage.MSG_NEED_COPY_TEXT);
        }
    }

    public void createTextFile() {
        String string = editText.getText().toString();
        if (!string.trim().isEmpty()) {
            (new TextFile(activity)).createTextFile(string);
        } else {
            showSnackbar(MySnackbarNoteMessage.MSG_NOTE_IS_EMPTY);
        }
    }

    private void sendText(String string) {
        if (!string.trim().isEmpty()) {
            try {
                MyIntent.sendIntent(activity, editText);
            } catch (Exception exception) {
                exception.printStackTrace();
                showSnackbar("");
            }
        } else {
            showSnackbar(MySnackbarNoteMessage.MSG_NOTE_IS_EMPTY);
        }
    }

    public void showSnackbar(String message) {
        activity.getMySnackbarNoteMessage().show(false, message);
    }

    public void showBottomSheet() {
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        editText.setSelection(editText.getSelectionEnd());
    }

    public void hideBottomSheet() {
        sheetBehavior.setHideable(true);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public boolean visibleBottomSheet() {
        return sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }
}
