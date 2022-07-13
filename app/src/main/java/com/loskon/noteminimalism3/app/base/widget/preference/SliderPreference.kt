package com.loskon.githubapi.app.base.widget.preference

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R

/**
 * Preference with slider
 */
class SliderPreference constructor(
    context: Context,
    attrs: AttributeSet
) : Preference(context, attrs) {

    private var onChangeListener: ((Int) -> Unit)? = null
    private var defaultValue: Int = 0

    init {
        layoutResource = R.layout.preference_slider
        context.theme.obtainStyledAttributes(attrs, androidx.preference.R.styleable.Preference, 0, 0).apply {
            defaultValue = getInt(androidx.preference.R.styleable.Preference_defaultValue, 0)
        }
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.isClickable = false

        val slider = holder.findViewById(R.id.slider_preference) as Slider
        val tvSliderValue = holder.findViewById(R.id.tv_value_slider_preference) as TextView

        val savedValue = sharedPreferences?.getInt(key, defaultValue) ?: 0
        slider.value = savedValue.toFloat()
        tvSliderValue.text = savedValue.toString()

        slider.addOnChangeListener(
            Slider.OnChangeListener { _, value: Float, _ ->
                val currentValue = value.toInt()
                sharedPreferences?.edit()?.putInt(key, currentValue)?.apply()
                tvSliderValue.text = currentValue.toString()
                onChangeListener?.invoke(currentValue)
            }
        )
    }

    fun setOnChangeListenerListener(onChangeListener: ((Int) -> Unit)?) {
        this.onChangeListener = onChangeListener
    }
}