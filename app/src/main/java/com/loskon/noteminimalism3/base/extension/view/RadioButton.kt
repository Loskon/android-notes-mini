package com.loskon.noteminimalism3.base.extension.view

import android.content.res.ColorStateList
import android.widget.RadioButton

fun RadioButton.setButtonTintColorKtx(color: Int) {
    buttonTintList = ColorStateList.valueOf(color)
}