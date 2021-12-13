package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setSliderColor

/**
 * Кастомный элемент настроек со слайдером
 */

class PrefScreenNumberLines @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.preferenceStyle,
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        layoutResource = R.layout.pref_screen_layout_number_lines
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.isClickable = false

        context.apply {

            val prefKey: String = getString(R.string.num_of_lines_key)
            val slider: Slider = holder.findViewById(R.id.slider_number_of_lines) as Slider

            val color: Int = PrefManager.getAppColor(this)
            slider.setSliderColor(color)

            val numberLines: Int = PrefManager.getNumberLines(this)
            slider.value = numberLines.toFloat()

            slider.addOnChangeListener(Slider.OnChangeListener { _, value: Float, _ ->
                val number: Int = value.toInt()
                PrefManager.save(this, prefKey, number)
                callback?.onChangeNumberLines(number)
            })
        }
    }

    interface CallbackNumberLines {
        fun onChangeNumberLines(numberLines: Int)
    }

    companion object {
        private var callback: CallbackNumberLines? = null

        fun listenerCallback(callback: CallbackNumberLines) {
            Companion.callback = callback
        }
    }
}