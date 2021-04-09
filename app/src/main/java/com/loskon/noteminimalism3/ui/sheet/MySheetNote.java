package com.loskon.noteminimalism3.ui.sheet;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.note.TextAssistant;
import com.loskon.noteminimalism3.ui.activity.NoteActivity;

/**
 * Нижнее диалоговое окно для работы с заметкой
 */

public class MySheetNote {

    private final NoteActivity activity;
    private TextAssistant textAssistant;
    private TextView tvDateMod;

    private View inflate;
    private BottomSheetDialog bottomSheetDialog;

    public MySheetNote(NoteActivity activity) {
        this.activity = activity;
        textAssistant = new TextAssistant(activity);
    }

    public void call(String textDateMod) {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) return;
        bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetStatusBar);
        inflate = View.inflate(activity, R.layout.inc_note_bottom_sheet, null);

        initItems();
        setTextDateMod(textDateMod);
        hideTextViewDate();

        bottomSheetDialog.setContentView(inflate);
        bottomSheetDialog.show();
    }

    private void initItems() {
        tvDateMod = inflate.findViewById(R.id.textView);
        TextView tvPaste = inflate.findViewById(R.id.tv_sheet_paste);
        TextView tvCopyAll = inflate.findViewById(R.id.tv_sheet_copy_all_text);
        TextView tvSave = inflate.findViewById(R.id.tv_sheet_save_txt);
        TextView tvShare = inflate.findViewById(R.id.tv_sheet_share);
        TextView tvClose = inflate.findViewById(R.id.tv_sheet_close);

        tvPaste.setOnClickListener(onClickListener);
        tvCopyAll.setOnClickListener(onClickListener);
        tvSave.setOnClickListener(onClickListener);
        tvShare.setOnClickListener(onClickListener);
        tvClose.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = view -> {
        int id = view.getId();

        bottomSheetDialog.dismiss();

        if (id == R.id.tv_sheet_paste) {
            textAssistant.pasteText();
        } else if (id == R.id.tv_sheet_copy_all_text) {
            textAssistant.copyText();
        } else if (id == R.id.tv_sheet_save_txt) {
            textAssistant.saveTextFile();
        } else if (id == R.id.tv_sheet_share) {
            textAssistant.sendText();
        }
    };

    private void hideTextViewDate() {
        if (activity.getNoteId() == 0) tvDateMod.setVisibility(View.GONE);
    }

    private void setTextDateMod(String textDateMod) {
        String text = activity.getString(R.string.bs_last_modified);
        text = text + ": " + textDateMod;
        tvDateMod.setText(text);
    }
}
