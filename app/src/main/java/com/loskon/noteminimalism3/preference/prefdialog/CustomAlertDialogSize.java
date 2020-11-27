package com.loskon.noteminimalism3.preference.prefdialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.loskon.noteminimalism3.R;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

public class CustomAlertDialogSize {

    private static int fontSize;
    private static SharedPreferences sharedPreferences;

    public static void alertDialogShowSize(Context context){
        AlertDialog alertDialog =  new MaterialAlertDialogBuilder(context,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_font_size)
                .show();

        TextView textViewSize = alertDialog.findViewById(R.id.textViewSize);
        SeekBar seekBarSize = alertDialog.findViewById(R.id.seekBarSize);
        Button btnOk = alertDialog.findViewById(R.id.btn_ok_size);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel_size);

        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
        fontSize = sharedPreferences.getInt("fontSize", 18);

        assert textViewSize != null;
        textViewSize.setTextSize(TypedValue.COMPLEX_UNIT_SP,fontSize);

        assert seekBarSize != null;
        // Установка минимального значения 14
        seekBarSize.setProgress(fontSize - 14);

        // Установка макисмального значения (14+26=40)
        seekBarSize.setMax(26);

        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Установка минимального значения 14
                fontSize = progress + 14;
                textViewSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            }
            public void onStartTrackingTouch(SeekBar seekBar) { }
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        assert btnOk != null;
        btnOk.setOnClickListener(v -> {
            sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("fontSize", fontSize);
            editor.apply();
            alertDialog.dismiss();
        });

        assert btnCancel != null;
        btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
    }
}
