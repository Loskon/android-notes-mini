package com.loskon.noteminimalism3.base.widget.preference

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setSliderColor
import com.loskon.noteminimalism3.sharedpref.AppPreference

@SuppressLint("PrivateResource")
class SliderPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    private var onChangeListener: ((Int) -> Unit)? = null
    private var defaultValue: Int = 0
    private var min: Int = 0
    private var max: Int = 100

    init {
        layoutResource = R.layout.preference_slider
        context.theme.obtainStyledAttributes(attrs, androidx.preference.R.styleable.Preference, 0, 0).apply {
            defaultValue = getInt(androidx.preference.R.styleable.Preference_defaultValue, 0)
        }
        context.theme.obtainStyledAttributes(attrs, R.styleable.SliderPreference, 0, 0).apply {
            min = getInt(R.styleable.SliderPreference_sliderMinValue, 0)
            max = getInt(R.styleable.SliderPreference_sliderMaxValue, 100)
        }
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.isClickable = false

        val color = AppPreference.getColor(context)
        val savedValue = sharedPreferences?.getInt(key, defaultValue) ?: 0

        val slider = holder.findViewById(R.id.slider_preference) as Slider
        val tvSliderValue = holder.findViewById(R.id.tv_value_slider_preference) as TextView

        slider.setSliderColor(color)
        slider.value = savedValue.toFloat()
        slider.valueFrom = min.toFloat()
        slider.valueTo = max.toFloat()
        tvSliderValue.text = savedValue.toString()

        slider.addOnChangeListener(
            Slider.OnChangeListener { _, value: Float, _ ->
                val newValue = value.toInt()
                sharedPreferences?.edit()?.putInt(key, newValue)?.apply()
                tvSliderValue.text = newValue.toString()
                onChangeListener?.invoke(newValue)
            }
        )
    }

    fun setOnChangeListener(onChangeListener: ((Int) -> Unit)?) {
        this.onChangeListener = onChangeListener
    }
}