package com.loskon.noteminimalism3.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.loskon.noteminimalism3.auxiliary.other.MyColor

/**
 *
 */

class GradientDrawableUtils {

    companion object {
        @JvmStatic
        fun getColoredDrawable(
            context: Context,
            isVis: Boolean
        ): Drawable {
            val gradientDrawable = GradientDrawable()

            val color: Int = if (isVis) MyColor.getMyColor(context) else Color.TRANSPARENT
            val radiusStroke: Int = if (isVis) context.getRadiusLinLay() else 0
            val boredStroke: Int = if (isVis) context.getStrokeLinLay() else 0

            gradientDrawable.cornerRadius = radiusStroke.toFloat()
            gradientDrawable.setStroke(boredStroke, color)

            return gradientDrawable
        }
    }
}