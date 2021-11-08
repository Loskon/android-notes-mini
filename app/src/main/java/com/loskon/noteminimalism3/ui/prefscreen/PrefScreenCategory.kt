package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager

/**
 * Позволяет установить свой цвет заголовка
 */

class PrefScreenCategory @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.preferenceCategoryStyle,
    defStyleRes: Int = 0
) : PreferenceCategory(context, attrs, defStyleAttr, defStyleRes) {

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val title: TextView = holder.findViewById(android.R.id.title) as TextView
        title.setTextColor(PrefManager.getAppColor(context))
    }
}