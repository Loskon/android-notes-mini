package com.loskon.noteminimalism3.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.loskon.noteminimalism3.auxiliary.other.MyColor

/**
 *
 */

class GradientDrawableUtils2 {

    companion object {
        @JvmStatic
        fun Context.getColoredDrawable2(
            gradientDrawable: GradientDrawable,
            isVis: Boolean
        ): Drawable {
            val color: Int = if (isVis) MyColor.getMyColor(this) else Color.TRANSPARENT
            val radiusStroke: Int = if (isVis) this.getRadiusLinLay() else 0
            val boredStroke: Int = if (isVis) this.getStrokeLinLay() else 0

            gradientDrawable.cornerRadius = radiusStroke.toFloat()
            gradientDrawable.setStroke(boredStroke, color)

            return gradientDrawable
        }
    }
}