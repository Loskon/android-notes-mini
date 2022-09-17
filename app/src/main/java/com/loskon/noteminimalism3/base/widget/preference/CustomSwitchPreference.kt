package com.loskon.noteminimalism3.base.widget.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.graphics.ColorUtils
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreference
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.AppPreference

@SuppressLint("WrongViewCast")
class CustomSwitchPreference(
    context: Context,
    attrs: AttributeSet
) : SwitchPreference(context, attrs) {

    init {
        widgetLayoutResource = R.layout.preference_switch
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val toggleGroup = holder.findViewById(R.id.toggle_group_preference) as MaterialButtonToggleGroup
        val btnOff = holder.findViewById(R.id.btn_toggle_preference_off) as MaterialButton
        val btnOn = holder.findViewById(R.id.btn_toggle_preference_on) as MaterialButton

        if (isEnabled) {
            val color = AppPreference.getColor(context)
            val isChecked = AppPreference.get(context, key, false)

            btnOff.strokeColor = ColorStateList.valueOf(color)
            btnOn.strokeColor = ColorStateList.valueOf(color)

            if (isChecked) {
                btnOff.setTextColor(Color.GRAY)
                btnOff.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                btnOn.setTextColor(color)
                btnOn.backgroundTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 40))
            } else {
                btnOff.setTextColor(color)
                btnOff.backgroundTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 40))
                btnOn.setTextColor(Color.GRAY)
                btnOn.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            }

            val checkedButton = if (isChecked) R.id.btn_toggle_preference_on else R.id.btn_toggle_preference_off
            toggleGroup.check(checkedButton)
        }
    }
}