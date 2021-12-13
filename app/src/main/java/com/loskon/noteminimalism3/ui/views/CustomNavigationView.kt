package com.loskon.noteminimalism3.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationView
import com.loskon.noteminimalism3.managers.FontManager

/**
 * Смена шрифта для элементов меню NavigationView
 */

class CustomNavigationView constructor(
    context: Context,
    attrs: AttributeSet
) : NavigationView(context, attrs) {

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val navMenuView: ViewGroup = getChildAt(0) as ViewGroup
        val navMenuItemsCount: Int = navMenuView.childCount
        var itemView: ViewGroup

        for (i in 0 until navMenuItemsCount) {
            itemView = navMenuView.getChildAt(i) as ViewGroup

            if (itemView is NavigationMenuItemView) {
                val checkedTextView = itemView.getChildAt(0) as CheckedTextView
                val typeface = FontManager.getAppFont(context)
                checkedTextView.typeface = typeface
            }
        }
    }
}