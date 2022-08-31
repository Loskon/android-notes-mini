package com.loskon.noteminimalism3.app.base.extension.context

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.color.MaterialColors

fun Context.getColorKtx(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

fun Context.getFontKtx(@FontRes fontId: Int): Typeface? {
    return ResourcesCompat.getFont(this, fontId)
}

fun Context.getDrawableKtx(@DrawableRes drawableId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableId)
}

fun Context.getDimensKtx(@DimenRes dimenId: Int): Int {
    return resources.getDimension(dimenId).toInt()
}

fun Context.getMaterialColorKtx(@AttrRes attrRes: Int): Int {
    return MaterialColors.getColor(this, attrRes, 0)
}

fun Context.getColorControlHighlightKtx(): Int {
    return getMaterialColorKtx(android.R.attr.colorControlHighlight)
}

fun Context.getColorPrimaryKtx(): Int {
    return getMaterialColorKtx(android.R.attr.colorPrimary)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}