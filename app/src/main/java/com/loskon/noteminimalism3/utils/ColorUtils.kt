package com.loskon.noteminimalism3.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor

/**
 *
 */

//
fun View.setColorBackgroundSnackbar(context: Context, isSuccess: Boolean) {
    val colorId: Int = context.getShortColor(context.getSuccessColor(isSuccess))
    backgroundTintList = ColorStateList.valueOf(colorId)
}

//
fun Context.getSuccessColor(isSuccess: Boolean): Int {
    return if (MyColor.isDarkMode(this)) {
        if (isSuccess) {
            R.color.snackbar_completed_dark
        } else {
            R.color.snackbar_no_completed_dark
        }
    } else {
        if (isSuccess) {
            R.color.snackbar_completed_light
        } else {
            R.color.snackbar_no_completed_light
        }
    }
}