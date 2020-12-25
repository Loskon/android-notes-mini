package com.loskon.noteminimalism3.ui.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class NoteHelperTwo {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
}
