package com.loskon.noteminimalism3.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.ColorUtils
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.slider.Slider

/**
 *
 */

//
fun View.setVisibleView(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

//
fun View.settingsBehavior() {
    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(this.parent as View)
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    behavior.isDraggable = false
    behavior.isHideable = false
}

//
fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
        topMarginDp?.run { params.topMargin = this.dpToPx(context) }
        rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
        bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
        requestLayout()
    }
}

fun Int.dpToPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
}

//
fun View.setLayoutParams() {
    this.layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}

//
fun Slider.setColorSlider(@ColorInt color: Int) {
    thumbTintList = ColorStateList.valueOf(color)
    trackActiveTintList = ColorStateList.valueOf(color)
    tickTintList = ColorStateList.valueOf(color)
    haloTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 70))
    trackInactiveTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 70))
}

//
fun CircularProgressIndicator.setColorProgressIndicator(@ColorInt color: Int) {
    setIndicatorColor(color)
    trackColor = ColorUtils.setAlphaComponent(color, 70)
}

//
fun TextView.setTextSizeInSp(fontSizeNote: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNote.toFloat())
}

//
fun Context.getShortDrawable(@DrawableRes icon: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, icon, null)
}

//
fun Context.getShortColor(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

//
fun BottomAppBar.setNavigationIconColor(@ColorInt color: Int) =
    navigationIcon?.mutate()?.setTint(color)

//
fun Menu.menuIconColor(@ColorInt color: Int) {
    if (size() != 0) {
        for (i in 0 until size()) {
            val drawable: Drawable = getItem(i).icon
            drawable.mutate()
            drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                color,
                BlendModeCompat.SRC_ATOP
            )
        }
    }
}

//
fun FloatingActionButton.setFabColor(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}
