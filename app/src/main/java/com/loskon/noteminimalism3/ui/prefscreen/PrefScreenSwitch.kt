package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreference
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.ValueUtil

/**
 * SwitchPreference со своим switch
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
        widgetLayoutResource = R.layout.pref_layout_switch
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
        buttonGroup.setSelectedBackgroundColor(color)
        buttonGroup.setBorder(borderGroup, color, 0, 0)
        buttonGroup.isClickable = false
    }

    private fun establishColorSwitch() {
        color = if (isEnabled) {
            PrefHelper.getAppColor(context)
        } else {
            Color.GRAY
        }
    }

    private fun toggleSwitchPosition() {
        val isChecked: Boolean = PrefHelper.load(context, key, false)

        val position: Int = if (isChecked) {
            1
        } else {
            0
        }

        buttonGroup.setPosition(position, false)
    }
}