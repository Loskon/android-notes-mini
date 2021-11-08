package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreference
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.ValueUtil

/**
 * Кастомный switch
 */

class PrefScreenSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.preferenceStyle,
    defStyleRes: Int = 0
) : SwitchPreference(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var buttonGroup: SegmentedButtonGroup
    private var color: Int = 0

    init {
        widgetLayoutResource = R.layout.pref_screen_layout_switch
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        buttonGroup = holder.findViewById(R.id.segmented_buton_group) as SegmentedButtonGroup
        establishColorSwitch()
        configureButtonGroup()
        toggleSwitchPosition()
    }

    private fun configureButtonGroup() {
        val borderGroup: Int = ValueUtil.getBorderWidgetSwitch(context)
        buttonGroup.apply {
            setSelectedBackgroundColor(color)
            setBorder(borderGroup, color, 0, 0)
            isClickable = false
        }
    }

    private fun establishColorSwitch() {
        color = if (isEnabled) {
            PrefManager.getAppColor(context)
        } else {
            Color.GRAY
        }
    }

    private fun toggleSwitchPosition() {
        val isChecked = PrefManager.load(context, key, false)

        val position: Int = if (isChecked) {
            1
        } else {
            0
        }

        buttonGroup.setPosition(position, false)
    }
}