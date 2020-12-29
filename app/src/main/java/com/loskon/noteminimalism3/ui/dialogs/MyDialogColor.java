package com.loskon.noteminimalism3.ui.dialogs;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPreference;

import static com.loskon.noteminimalism3.R.style.MaterialAlertDialog_Rounded;

public class MyDialogColor {

    private static int color;
    private static CallbackColor callbackColor;
    private static CallbackColor2 callbackColor2;

    public void registerCallBack(CallbackColor callbackColor){
        MyDialogColor.callbackColor = callbackColor;
    }

    public void registerCallBack2(CallbackColor2 callbackColor2){
        MyDialogColor.callbackColor2 = callbackColor2;
    }

    public static void alertDialogShowColorPicker(Context context){
        AlertDialog alertDialog =  new MaterialAlertDialogBuilder(context,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_holo_picker)
                .create();
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width,height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();


        // Initiation View
        ColorPicker colorPickerView = alertDialog.findViewById(R.id.holo_picker);
        Button btnOk = alertDialog.findViewById(R.id.btn_holo_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_holo_cancel);
        MaterialButton matBtnResetColor = alertDialog.findViewById(R.id.btn_reset_color_picker);

        color = MyColor.getColorCustom(context);

        assert colorPickerView != null;
        colorPickerView.setShowOldCenterColor(false); // выключить показ старого цвета

        // ColorPicker
        colorPickerView.setColor(color); // Set color
        colorPickerView.setOnColorChangedListener(color -> {
            MyDialogColor.color = color;
        });

        // OK
        assert btnOk != null;
        btnOk.setOnClickListener(v -> {
            MySharedPreference.saveInt(context,"color",color);
            callbackColor.callingBackColor(color);
            callbackColor2.callingBackColor2(color);
            alertDialog.dismiss();
        });

        // Cancel
        assert btnCancel != null;
        btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        assert matBtnResetColor != null;
        matBtnResetColor.setOnClickListener(view -> {
            colorPickerView.setColor(-16739862);
        });
    }

    public interface CallbackColor{
        void callingBackColor(int color);
    }

    public interface CallbackColor2{
        void callingBackColor2(int color);
    }

}
