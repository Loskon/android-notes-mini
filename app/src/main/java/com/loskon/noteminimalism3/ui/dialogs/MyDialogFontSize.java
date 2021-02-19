package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;

public class MyDialogFontSize{

    private final Activity activity;
    private AlertDialog alertDialog;
    private int fontSizeNotes;
    private TextView textViewSize;

    public MyDialogFontSize(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_font_size);
        alertDialog.show();

        textViewSize = alertDialog.findViewById(R.id.textViewSize);
        Slider slider = alertDialog.findViewById(R.id.slider_font_size2);
        Button btnOk = alertDialog.findViewById(R.id.btn_ok_size);
        Button btnReset = alertDialog.findViewById(R.id.btn_ok_size2);

        // assert
        assert textViewSize != null;
        assert slider != null;
        assert btnOk != null;
        assert btnReset != null;

        int color = MyColor.getColorCustom(activity);
        btnOk.setBackgroundColor(color);
        btnReset.setTextColor(color);
        MyColor.setColorSlider(activity, slider);

        fontSizeNotes = GetSharedPref.getFontSizeNote(activity);

        slider.setValue(fontSizeNotes);
        textViewSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNotes);

        slider.addOnChangeListener((slider2, value, fromUser) -> {
            fontSizeNotes = (int) value;
            setSharedPref(fontSizeNotes);
        });

        btnOk.setOnClickListener(v -> alertDialog.dismiss());

        btnReset.setOnClickListener(view -> {
            int size = 18;
            slider.setValue(size);
            setSharedPref(size);
        });
    }

    private void setSharedPref(int fontSizeNotes) {
        textViewSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNotes);
        MySharedPref.setInt(activity, MyPrefKey.KEY_TITLE_FONT_SIZE_NOTES, fontSizeNotes);
    }
}
