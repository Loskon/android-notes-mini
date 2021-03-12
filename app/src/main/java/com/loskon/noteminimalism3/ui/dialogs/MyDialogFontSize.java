package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

/**
 * Изменение размера шрифта текста внутри заметок
 */

public class MyDialogFontSize{

    private final Activity activity;
    private AlertDialog alertDialog;
    private int fontSizeNote;
    private TextView textView;
    private Slider slider;

    public MyDialogFontSize(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_font_size);
        alertDialog.show();

        textView = alertDialog.findViewById(R.id.txt_size_note);
        slider = alertDialog.findViewById(R.id.slider_font_size_note);
        Button btnCancel = alertDialog.findViewById(R.id.btn_ok_size_note);
        Button btnReset = alertDialog.findViewById(R.id.btn_reset_size_note);

        int color = MyColor.getMyColor(activity);
        btnCancel.setTextColor(color);
        btnReset.setBackgroundColor(color);
        MyColor.setColorSlider(activity, slider);

        fontSizeNote = GetSharedPref.getFontSizeNote(activity);

        slider.setValue(fontSizeNote);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNote);

        slider.addOnChangeListener((slider2, value, fromUser) -> {
            fontSizeNote = (int) value;
            setSharedPref(fontSizeNote);
        });

        btnCancel.setOnClickListener(view -> alertDialog.dismiss());

        btnReset.setOnClickListener(view -> {
            int size = 18;
            slider.setValue(size);
            setSharedPref(size);
        });
    }

    private void setSharedPref(int fontSizeNotes) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNotes);
        MySharedPref.setInt(activity, MyPrefKey.KEY_TITLE_FONT_SIZE_NOTES, fontSizeNotes);
    }
}
