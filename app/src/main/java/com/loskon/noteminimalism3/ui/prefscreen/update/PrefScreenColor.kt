package com.loskon.noteminimalism3.ui.prefscreen.update

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref

class PrefScreenColor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        widgetLayoutResource = R.layout.pref_screen_widget_color
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val imageView = holder.findViewById(R.id.imageViewColor) as ImageView
        imageView.setColorFilter(AppPref.getAppColor(context))
    }
}