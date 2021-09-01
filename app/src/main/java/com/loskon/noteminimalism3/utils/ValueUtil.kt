package com.loskon.noteminimalism3.utils

import android.content.Context
import com.loskon.noteminimalism3.R


fun Context.getRadiusLinLay(): Int {
    return resources.getDimension(R.dimen.corner_radius).toInt()
}

fun Context.getStrokeLinLay(): Int {
    return resources.getDimension(R.dimen.border_stroke).toInt()
}
