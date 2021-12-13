package com.loskon.noteminimalism3.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Управление состоянием клавиатуры
 */

fun EditText.showKeyboard(context: Context) {
    val service: String = Context.INPUT_METHOD_SERVICE
    val inputManager: InputMethodManager = context.getSystemService(service) as InputMethodManager
    requestFocus()
    inputManager.showSoftInput(this, 0)
}

fun EditText.hideKeyboard(context: Context) {
    val service: String = Context.INPUT_METHOD_SERVICE
    val inputManager: InputMethodManager = context.getSystemService(service) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}