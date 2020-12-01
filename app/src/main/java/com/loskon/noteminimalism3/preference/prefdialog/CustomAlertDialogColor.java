package com.loskon.noteminimalism3.preference.prefdialog;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.loskon.noteminimalism3.R;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

public class CustomAlertDialogColor {

    private static int color;
    private static SharedPreferences sharedPreferences;

    public static void alertDialogShowColor(Context context){
        AlertDialog alertDialog =  new MaterialAlertDialogBuilder(context,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_color_picker)
                .show();

        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);


        ColorPickerView colorPickerView = alertDialog.findViewById(R.id.color_picker_view_dialog);
        Button btnOk = alertDialog.findViewById(R.id.btn_ok_color);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel_color);
        Button btnReset = alertDialog.findViewById(R.id.btn_reset_color);
        ImageView imageView = alertDialog.findViewById(R.id.imageViewColorPicker);

        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
        color = sharedPreferences.getInt("color", -16711872);

        assert imageView != null;
        imageView.setColorFilter(color);

        assert colorPickerView != null;
        colorPickerView.setColor(color, false);
        colorPickerView.addOnColorChangedListener(selectedColor -> {
            color = selectedColor;
            imageView.setColorFilter(color);
        });

        colorPickerView.addOnColorSelectedListener(selectedColor -> {

        });


        assert btnOk != null;
        btnOk.setOnClickListener(v -> {
            sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("color", color);
            editor.apply();
            alertDialog.dismiss();
        });

        assert btnCancel != null;
        btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        assert btnReset != null;
        btnReset.setOnClickListener(v -> {
            color = -16711872;
            imageView.setColorFilter(color);
            colorPickerView.setColor(color, false);
            sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("color", color);
            editor.apply();
        });
    }
}
