package com.loskon.noteminimalism3.preference.prefdialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.loskon.noteminimalism3.R;

import static android.content.Context.MODE_PRIVATE;
import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

public class CustomAlertDialogColorPicker {

    private static int color;
    private static SharedPreferences sharedPreferences;
    private static CallbackColor callbackColor;

    public void registerCallBack(CallbackColor callbackColor){
        CustomAlertDialogColorPicker.callbackColor = callbackColor;
    }

    public static void alertDialogShowColorPicker(Context context){
        AlertDialog alertDialog =  new MaterialAlertDialogBuilder(context,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_holo_picker)
                .show();

        // Initiation View
        ColorPicker colorPickerView = alertDialog.findViewById(R.id.holo_picker);
        Button btnOk = alertDialog.findViewById(R.id.btn_holo_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_holo_cancel);
        MaterialButton matBtnResetColor = alertDialog.findViewById(R.id.btn_reset_color_picker);

        loadColor(context); // Load color

        colorPickerView.setShowOldCenterColor(false); // выключить показ старого цвета

        // ColorPicker
        assert colorPickerView != null;
        colorPickerView.setColor(color); // Set color
        colorPickerView.setOnColorChangedListener(color -> {
            CustomAlertDialogColorPicker.color = color;
        });

        // OK
        assert btnOk != null;
        btnOk.setOnClickListener(v -> {
            saveColor(context);
            callbackColor.callingBackColor(color);
            alertDialog.dismiss();
        });

        // Cancel
        assert btnCancel != null;
        btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        matBtnResetColor.setOnClickListener(view -> {
            colorPickerView.setColor(-16739862);
        });
    }

    private static void loadColor(Context context) {
        sharedPreferences =  context.getSharedPreferences("saveColorPicker", MODE_PRIVATE);
        color = sharedPreferences.getInt("color",-16739862);
    }

    private static void saveColor(Context context) {
        sharedPreferences = context.getSharedPreferences("saveColorPicker", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("color", color);
        editor.apply();
    }

    public interface CallbackColor{
        void callingBackColor(int color);
    }
}
