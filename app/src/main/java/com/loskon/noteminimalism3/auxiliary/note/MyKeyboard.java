package com.loskon.noteminimalism3.auxiliary.note;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

/**
 * Помощник для сисмтемной клавиатуры
 */

public class MyKeyboard {

    private static InputMethodManager getInputManager(Activity activity) {
        return (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void showSoftKeyboard(Activity activity, EditText editText) {
        // Показать
        getInputManager(activity).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void showSoftKeyboardInput(Activity activity, TextInputEditText textInputEditText) {
        // Показать
        textInputEditText.postDelayed(() ->
                getInputManager(activity).showSoftInput(textInputEditText, 0),50);

    }

    public static void hideSoftKeyboard(Activity activity, EditText editText) {
        // Скрыть
        getInputManager(activity).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
