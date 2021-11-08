package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.fragments.SettingsAppFragment
import com.loskon.noteminimalism3.utils.setBackgroundTintColor
import com.loskon.noteminimalism3.utils.setSliderColor
import com.loskon.noteminimalism3.utils.setTextSizeShort

/**
 * Кастомный элемент настроек для изменения размера текста в списке заметок
 */

class PrefScreenCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.preferenceStyle,
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes),
    SettingsAppFragment.CallbackResetFontSize {

    init {
        layoutResource = R.layout.pref_screen_layout_card_view
    }

    private lateinit var viewFavorite: View
    private lateinit var titleCategory: TextView
    private lateinit var tvFontSize: TextView
    private lateinit var tvDateFontSize: TextView
    private lateinit var slider: Slider
    private var fontSizeTitle: Int = 0
    private var fontSizeDate: Int = 0

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        initView(holder)
        configurePreferenceView(holder)
        installCallbacks()
        configureViews()
        establishColorViews()
        installHandlers()
    }

    private fun initView(holder: PreferenceViewHolder) {
        viewFavorite = holder.findViewById(R.id.view_favorite2)
        titleCategory = holder.findViewById(R.id.tv_category_title) as TextView
        tvFontSize = holder.findViewById(R.id.tv_card_note_title2) as TextView
        tvDateFontSize = holder.findViewById(R.id.tv_card_note_date2) as TextView
        slider = holder.findViewById(R.id.slider_font_size_card_note) as Slider
    }

    private fun configurePreferenceView(holder: PreferenceViewHolder) {
        holder.itemView.isClickable = false
    }

    private fun installCallbacks() {
        SettingsAppFragment.listenerCallbackReset(this)
    }

    private fun configureViews() {
        fontSizeTitle = PrefManager.getFontSize(context)

        tvFontSize.text = context.getString(R.string.title_card_view)
        tvDateFontSize.text = context.getString(R.string.date_card_view)

        setTextSizes(fontSizeTitle, getValueDateFontSize())

        slider.value = fontSizeTitle.toFloat()
    }

    private fun setTextSizes(fontSizeTitle: Int, fontSizeDate: Int) {
        tvFontSize.setTextSizeShort(fontSizeTitle)
        tvDateFontSize.setTextSizeShort(fontSizeDate)
    }

    private fun getValueDateFontSize(): Int {
        return when {
            fontSizeTitle < 18 -> 12
            fontSizeTitle <= 22 -> 14
            fontSizeTitle <= 26 -> 16
            fontSizeTitle <= 30 -> 18
            fontSizeTitle <= 34 -> 20
            fontSizeTitle <= 38 -> 22
            fontSizeTitle <= 42 -> 24
            else -> 14
        }
    }

    private fun establishColorViews() {
        val color = PrefManager.getAppColor(context)
        establishColorViews(color)
    }

    private fun establishColorViews(color: Int) {
        viewFavorite.setBackgroundTintColor(color)
        titleCategory.setTextColor(color)
        slider.setSliderColor(color)
    }

    private fun installHandlers() {
        slider.addOnChangeListener(Slider.OnChangeListener { _, value: Float, _ ->
            fontSizeTitle = value.toInt()
            fontSizeDate = getValueDateFontSize()
            performChangeTextSizes()
        })
    }

    private fun performChangeTextSizes() {
        setTextSizes(fontSizeTitle, fontSizeDate)
        saveAppearanceSettings()
        callback?.onChangeFontSizes(fontSizeTitle, fontSizeDate)
    }

    private fun saveAppearanceSettings() {
        PrefManager.setFontSizeTitle(context, fontSizeTitle)
        PrefManager.setFontSizeDate(context, fontSizeDate)
    }

    override fun onResetFontSize() {
        fontSizeTitle = context.resources.getInteger(R.integer.number_font_size_title)
        fontSizeDate = context.resources.getInteger(R.integer.number_font_size_date)
        slider.value = fontSizeTitle.toFloat()
        performChangeTextSizes()
    }

    interface CallbackFontsSizes {
        fun onChangeFontSizes(fontSizeTitle: Int, fontSizeDate: Int)
    }

    companion object {
        private var callback: CallbackFontsSizes? = null

        fun listenerCallback(callback: CallbackFontsSizes) {
            Companion.callback = callback
        }
    }
}