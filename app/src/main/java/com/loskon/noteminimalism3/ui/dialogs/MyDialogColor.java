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
    private static CallbackColorNavIcon callbackColorNavIcon;
    private static CallbackColorColorSettingsApp callbackColorColorSettingsApp;
    private static CallbackColorNotifyData callbackColorNotifyData;

    public void registerCallBackColorNavIcon(CallbackColorNavIcon callbackColorNavIcon) {
        MyDialogColor.callbackColorNavIcon = callbackColorNavIcon;
    }

    public void registerCallBackColorSettingsApp(CallbackColorColorSettingsApp callbackColorColorSettingsApp) {
        MyDialogColor.callbackColorColorSettingsApp = callbackColorColorSettingsApp;
    }

    public void registerCallBackColorNotifyData(CallbackColorNotifyData callbackColorNotifyData) {
        MyDialogColor.callbackColorNotifyData = callbackColorNotifyData;
    }

    public static void alertDialogShowColorPicker(Context context){
        AlertDialog alertDialog =  new MaterialAlertDialogBuilder(context,
                MaterialAlertDialog_Rounded)
                .setView(R.layout.dialog_color_picker)
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

        // assert
        assert colorPickerView != null;
        assert btnOk != null;
        assert btnCancel != null;
        assert matBtnResetColor != null;

        color = MyColor.getColorCustom(context);

        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);
        MyColor.setColorMaterialBtn(context, matBtnResetColor);

        colorPickerView.setShowOldCenterColor(false); // выключить показ старого цвета

        // ColorPicker
        colorPickerView.setColor(color);
        colorPickerView.setOnColorChangedListener(color -> {
            MyDialogColor.color = color;
        });

        // OK
        btnOk.setOnClickListener(v -> {
            MySharedPreference.saveInt(context,"color",color);
            if (callbackColorNavIcon != null &&
                    callbackColorColorSettingsApp != null && callbackColorNotifyData != null) {
                callbackColorNavIcon.callingBackColorNavIcon(color);
                callbackColorColorSettingsApp.callingBackColorSettingsApp(color);
                callbackColorNotifyData.callingBackColorNotifyData(color);
            }
            alertDialog.dismiss();
        });

        // Cancel
        btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        // Reset
        matBtnResetColor.setOnClickListener(view -> {
            colorPickerView.setColor(context
                    .getResources().getColor(R.color.color_default_light_blue));
        });
    }

    public interface CallbackColorNavIcon {
        void callingBackColorNavIcon(int color);
    }

    public interface CallbackColorColorSettingsApp {
        void callingBackColorSettingsApp(int color);
    }

    public interface CallbackColorNotifyData {
        void callingBackColorNotifyData(int color);
    }

}
