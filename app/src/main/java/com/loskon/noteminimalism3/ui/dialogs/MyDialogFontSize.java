package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
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
    private int fontSizeNote;
    private TextView textView;
    private Slider slider;

    public MyDialogFontSize(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        AlertDialog alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_font_size);
        alertDialog.show();

        textView = alertDialog.findViewById(R.id.tv_font_size_text);
        slider = alertDialog.findViewById(R.id.slider_font_size_note);
        MaterialButton btnReset = alertDialog.findViewById(R.id.btn_font_size_reset);
        Button btnOk = alertDialog.findViewById(R.id.btn_font_size_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_font_size_cancel);


        int color = MyColor.getMyColor(activity);
        btnReset.setTextColor(color);
        btnReset.setStrokeColor(ColorStateList.valueOf(color));
        btnCancel.setTextColor(color);
        btnOk.setBackgroundColor(color);
        MyColor.setColorSlider(activity, slider);

        fontSizeNote = GetSharedPref.getFontSizeNote(activity);

        slider.setValue(fontSizeNote);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNote);

        slider.addOnChangeListener((slider2, value, fromUser) -> {
            fontSizeNote = (int) value;
            setText(fontSizeNote);
        });

        btnOk.setOnClickListener(view -> {
            alertDialog.dismiss();
            setSharedPref(fontSizeNote);
        });

        btnReset.setOnClickListener(view -> {
            int size = 18;
            setText(size);
            slider.setValue(size);
        });

        btnCancel.setOnClickListener(view -> alertDialog.dismiss());
    }

    private void setText(int fontSizeNotes) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNotes);
    }

    private void setSharedPref(int fontSizeNotes) {
        MySharedPref.setInt(activity, MyPrefKey.KEY_TITLE_FONT_SIZE_NOTES, fontSizeNotes);
    }
}
