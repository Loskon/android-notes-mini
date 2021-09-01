package com.loskon.noteminimalism3.utils

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText

/**
 * Утилиты
 */

// Тост 1
fun Context.showToast(stringId: Int) {
    var toast: Toast? = null

    toast?.cancel()
    toast = makeText(this, getString(stringId), Toast.LENGTH_SHORT)
    toast.show()
}

// Тост 2
fun Context.showToast(message: String) {
    var toast: Toast? = null

    toast?.cancel()
    toast = makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
}


