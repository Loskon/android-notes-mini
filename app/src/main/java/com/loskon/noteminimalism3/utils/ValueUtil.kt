package com.loskon.noteminimalism3.utils

import android.content.Context
import com.loskon.noteminimalism3.R

/**
 * Получение значений
 */

class ValueUtil {

    companion object {

        fun getRadiusLinLay(context: Context): Int {
            return context.resources.getDimension(R.dimen.corner_radius_lin_layout).toInt()
        }

        fun getStrokeLinLay(context: Context): Int {
            return context.resources.getDimension(R.dimen.border_stroke_lin_layout).toInt()
        }

        fun getBorderWidgetSwitch(context: Context): Int {
            return context.resources.getDimension(R.dimen.border_width_switch).toInt()
        }
    }
}
