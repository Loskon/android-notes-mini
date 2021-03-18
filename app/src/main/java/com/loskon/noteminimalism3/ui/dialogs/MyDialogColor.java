package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyToast;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

/**
 * Выбор цвета с помощью ColorPicker
 */

public class MyDialogColor {

    private final Activity activity;

    private int color;

    private static CallbackNavIcon callbackNavIcon;
    private static CallbackSettingsApp callbackSettingsApp;
    private static CallbackNotifyData callbackNotifyData;
    private static CallbackColorMain callbackColorMain;

    public static void regCallBackNavIcon(CallbackNavIcon callbackNavIcon) {
        MyDialogColor.callbackNavIcon = callbackNavIcon;
    }

    public static void regCallBackSettingsApp(CallbackSettingsApp callbackSettingsApp) {
        MyDialogColor.callbackSettingsApp = callbackSettingsApp;
    }

    public static void regCallBackNotifyData(CallbackNotifyData callbackNotifyData) {
        MyDialogColor.callbackNotifyData = callbackNotifyData;
    }

    public static void regCallbackMain(CallbackColorMain callbackColorMain) {
        MyDialogColor.callbackColorMain = callbackColorMain;
    }

    public MyDialogColor(Activity activity) {
        this.activity = activity;
    }

    public void call() {
        AlertDialog alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_pref_color_picker);
        alertDialog.show();

        // Initiation View
        ColorPicker colorPickerView = alertDialog.findViewById(R.id.holo_picker);
        SVBar svBar = alertDialog.findViewById(R.id.svbar);
        Button btnOk = alertDialog.findViewById(R.id.btn_color_picker_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_color_picker_cancel);
        MaterialButton btnReset = alertDialog.findViewById(R.id.btn_color_picker_reset);

        color = MyColor.getMyColor(activity);
        //boolean isDarkModeOn = MyColor.isDarkMode(activity);
        colorPickerView.addSVBar(svBar);


        btnOk.setBackgroundColor(color);
        btnCancel.setTextColor(color);
        MyColor.setColorMaterialBtn(activity, btnReset);

        colorPickerView.setShowOldCenterColor(false); // выключить показ старого цвета

        // ColorPicker
        colorPickerView.setColor(color);
        colorPickerView.setOnColorChangedListener(color -> this.color = color);

        // OK
        btnOk.setOnClickListener(v -> {

            if (color == -16777216 || color == -1) {
                String message = activity.getString(R.string.bad_idea);
                MyToast.showToast(activity, message,false);
                color = activity.getResources()
                        .getColor(R.color.light_blue);
            }

            MySharedPref.setInt(activity, MyPrefKey.KEY_COLOR, color);

            callbackNavIcon.onCallBackNavIcon(color);
            callbackSettingsApp.onCallBackSettingsApp(color);
            callbackNotifyData.onCallBackNotifyData();
            callbackColorMain.onCallBackMain(color);

            alertDialog.dismiss();
        });

        // Reset
        btnReset.setOnClickListener(view -> colorPickerView.setColor(activity
                .getResources().getColor(R.color.light_blue)));

        // Cancel
        btnCancel.setOnClickListener(view -> alertDialog.dismiss());
    }

    public interface CallbackNavIcon {
        void onCallBackNavIcon(int color);
    }

    public interface CallbackSettingsApp {
        void onCallBackSettingsApp(int color);
    }

    public interface CallbackNotifyData {
        void onCallBackNotifyData();
    }

    public interface CallbackColorMain {
        void onCallBackMain(int color);
    }
}
