package com.loskon.noteminimalism3.app.base.extension.view

import android.content.res.ColorStateList
import android.widget.RadioButton

fun RadioButton.setColorKtx(color: Int) {
    buttonTintList = ColorStateList.valueOf(color)
}