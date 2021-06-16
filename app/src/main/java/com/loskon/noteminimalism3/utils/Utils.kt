package com.loskon.noteminimalism3.utils

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText

/**
 *
 */

fun Context.showToast(stringIdForToast: Int) {
    val message: String = getString(stringIdForToast)
    var toast: Toast? = null
    toast?.cancel()
    toast = makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
}

