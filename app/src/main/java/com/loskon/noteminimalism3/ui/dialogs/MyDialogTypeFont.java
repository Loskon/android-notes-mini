package com.loskon.noteminimalism3.ui.dialogs;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.loskon.noteminimalism3.auxiliary.other.AppFontManager;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

/**
 * Дилог для просмотра примера шрифта
 */

public class MyDialogTypeFont {

    private final Activity activity;

    private static CallbackTypeFont callbackTypeFont;

    public static void regCallBackTypeFont(CallbackTypeFont callbackTypeFont) {
        MyDialogTypeFont.callbackTypeFont = callbackTypeFont;
    }

    public MyDialogTypeFont(Activity activity) {
        this.activity = activity;
    }

    public void call(int checkedId) {
        AlertDialog alertDialog = DialogBuilder.buildDialog(activity, R.layout.dialog_type_font);
        alertDialog.show();

        TextView textView = alertDialog.findViewById(R.id.tv_font_example);
        Button btnOk = alertDialog.findViewById(R.id.btn_type_font_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_type_font_cancel);

        Typeface typeface = new AppFontManager(activity).setFontText(checkedId);

        int color = MyColor.getMyColor(activity);
        btnCancel.setTextColor(color);
        btnOk.setBackgroundColor(color);

        textView.setTypeface(typeface);

        btnOk.setOnClickListener(view -> {
            MySharedPref.setInt(activity, MyPrefKey.KEY_TYPE_FONT, checkedId);
            if (callbackTypeFont != null) callbackTypeFont.onCallBack();
            MyIntent.goSettingsActivityClear(activity);
        });

        btnCancel.setOnClickListener(view -> alertDialog.dismiss());
    }

    public interface CallbackTypeFont {
        void onCallBack();
    }
}
