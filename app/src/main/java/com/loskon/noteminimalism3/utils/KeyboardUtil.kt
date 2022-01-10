package com.loskon.noteminimalism3.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Управление состоянием клавиатуры
 */

fun EditText.showKeyboard(context: Context) {
    requestFocus()
    context.getInputManager().showSoftInput(this, 0)
}

fun EditText.hideKeyboard(context: Context) {
    context.getInputManager().hideSoftInputFromWindow(windowToken, 0)
}

private fun Context.getInputManager(): InputMethodManager {
    return getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}