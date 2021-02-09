package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.graphics.Color;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;

public class MyDialogColor {

    private static int color;
    private static CallbackColorNavIcon callbackColorNavIcon;
    private static CallbackColorColorSettingsApp callbackColorColorSettingsApp;
    private static CallbackColorNotifyData callbackColorNotifyData;
    private static CallbackColorMain callbackColorMain;

    public void registerCallBackColorNavIcon(CallbackColorNavIcon callbackColorNavIcon) {
        MyDialogColor.callbackColorNavIcon = callbackColorNavIcon;
    }

    public void registerCallBackColorSettingsApp(CallbackColorColorSettingsApp callbackColorColorSettingsApp) {
        MyDialogColor.callbackColorColorSettingsApp = callbackColorColorSettingsApp;
    }

    public void registerCallBackColorNotifyData(CallbackColorNotifyData callbackColorNotifyData) {
        MyDialogColor.callbackColorNotifyData = callbackColorNotifyData;
    }

    public void registerCallBackColorMain(CallbackColorMain callbackColorMain) {
        MyDialogColor.callbackColorMain = callbackColorMain;
    }

    public static void alertDialogShowColorPicker(Activity activity){
        AlertDialog alertDialog = MyDialogBuilder.buildDialog(activity, R.layout.dialog_color_picker);
        alertDialog.show();

        // Initiation View
        ColorPicker colorPickerView = alertDialog.findViewById(R.id.holo_picker);
        SVBar svBar =  alertDialog.findViewById(R.id.svbar);
        Button btnOk = alertDialog.findViewById(R.id.btn_holo_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_holo_cancel);
        MaterialButton matBtnResetColor = alertDialog.findViewById(R.id.btn_reset_color_picker);

        // assert
        assert colorPickerView != null;
        assert btnOk != null;
        assert btnCancel != null;
        assert matBtnResetColor != null;
        assert svBar != null;

        color = MyColor.getColorCustom(activity);
        boolean isDarkModeOn = MyColor.isDarkMode(activity);
        colorPickerView.addSVBar(svBar);


        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);
        MyColor.setColorMaterialBtn(activity, matBtnResetColor);

        colorPickerView.setShowOldCenterColor(false); // выключить показ старого цвета

        // ColorPicker
        colorPickerView.setColor(color);
        colorPickerView.setOnColorChangedListener(color -> {
            MyDialogColor.color = color;
        });

        // OK
        btnOk.setOnClickListener(v -> {

            if ((isDarkModeOn && color == -16777216) || (!isDarkModeOn && color == -1)) {
                color = Color.GRAY;
            }

            MySharedPref.setInt(activity, MyPrefKey.KEY_COLOR, color);

            if (callbackColorNavIcon != null && callbackColorColorSettingsApp != null
                    && callbackColorNotifyData != null && callbackColorMain != null) {
                callbackColorNavIcon.callingBackColorNavIcon(color);
                callbackColorColorSettingsApp.callingBackColorSettingsApp(color);
                callbackColorNotifyData.callingBackColorNotifyData();
                callbackColorMain.callingBackColorMain(color);
            }

            alertDialog.dismiss();
        });

        // Cancel
        btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        // Reset
        matBtnResetColor.setOnClickListener(view -> {
            colorPickerView.setColor(activity
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
        void callingBackColorNotifyData();
    }

    public interface CallbackColorMain {
        void callingBackColorMain(int color);
    }

}
