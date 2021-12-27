package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper

/**
 * Preference с imageView
 */

class PrefScreenColor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        widgetLayoutResource = R.layout.pref_widget_color
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val imageView = holder.findViewById(R.id.image_view_color) as ImageView
        imageView.setColorFilter(PrefHelper.getAppColor(context))
    }
}