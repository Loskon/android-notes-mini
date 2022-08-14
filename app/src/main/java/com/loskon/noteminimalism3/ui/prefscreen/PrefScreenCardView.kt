package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setBackgroundTintColorKtx
import com.loskon.noteminimalism3.managers.setSliderColor
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.fragments.AppearanceSettingsFragment
import com.loskon.noteminimalism3.utils.changeTextSize

/**
 * Preference для изменения размера текста в карточках
 */

class PrefScreenCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.preferenceStyle,
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes),
    AppearanceSettingsFragment.ResetFontSizeCallback {

    init {
        layoutResource = R.layout.pref_layout_card_view
    }

    private lateinit var viewFavorite: View
    private lateinit var titleCategory: TextView
    private lateinit var tvTitleFontSize: TextView
    private lateinit var tvDateFontSize: TextView
    private lateinit var slider: Slider

    private var titleFontSize: Int = 0
    private var fontSizeDate: Int = 0

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        initView(holder)
        configurePreferenceView(holder)
        installCallbacks()
        configureViews()
        establishViewsColor()
        setupViewsListeners()
    }

    private fun initView(holder: PreferenceViewHolder) {
        viewFavorite = holder.findViewById(R.id.view_favorite)
        titleCategory = holder.findViewById(R.id.tv_category_title) as TextView
        tvTitleFontSize = holder.findViewById(R.id.tv_card_note_title) as TextView
        tvDateFontSize = holder.findViewById(R.id.tv_card_note_date) as TextView
        slider = holder.findViewById(R.id.slider_font_size_card_note) as Slider
    }

    private fun configurePreferenceView(holder: PreferenceViewHolder) {
        holder.itemView.isClickable = false
    }

    private fun installCallbacks() {
        AppearanceSettingsFragment.registerResetFontSizeCallback(this)
    }

    private fun configureViews() {
        titleFontSize = AppPreference.getTitleFontSize(context)
        tvTitleFontSize.text = context.getString(R.string.title_card_view)
        tvDateFontSize.text = context.getString(R.string.date_card_view)
        setTextSizes(titleFontSize, getValueDateFontSize())
        slider.value = titleFontSize.toFloat()
        titleCategory.typeface = Typeface.DEFAULT_BOLD
    }

    private fun setTextSizes(fontSizeTitle: Int, fontSizeDate: Int) {
        tvTitleFontSize.changeTextSize(fontSizeTitle)
        tvDateFontSize.changeTextSize(fontSizeDate)
    }

    private fun getValueDateFontSize(): Int {
        return when {
            titleFontSize < 18 -> 12
            titleFontSize <= 22 -> 14
            titleFontSize <= 26 -> 16
            titleFontSize <= 30 -> 18
            titleFontSize <= 34 -> 20
            titleFontSize <= 38 -> 22
            titleFontSize <= 42 -> 24
            else -> 14
        }
    }

    private fun establishViewsColor() {
        val color: Int = AppPreference.getColor(context)
        viewFavorite.setBackgroundTintColorKtx(color)
        titleCategory.setTextColor(color)
        slider.setSliderColor(color)
    }

    private fun setupViewsListeners() {
        slider.addOnChangeListener(Slider.OnChangeListener { _, value: Float, _ ->
            titleFontSize = value.toInt()
            fontSizeDate = getValueDateFontSize()
            performChangeTextSizes()
        })
    }

    private fun performChangeTextSizes() {
        setTextSizes(titleFontSize, fontSizeDate)
        saveAppearanceSettings()
        callback?.onChangeFontSizes(titleFontSize, fontSizeDate)
    }

    private fun saveAppearanceSettings() {
        AppPreference.setTitleFontSize(context, titleFontSize)
        AppPreference.setDateFontSize(context, fontSizeDate)
    }

    override fun onResetFontSize() {
        titleFontSize = context.resources.getInteger(R.integer.title_font_size_int)
        fontSizeDate = context.resources.getInteger(R.integer.date_font_size_int)
        slider.value = titleFontSize.toFloat()
        performChangeTextSizes()
    }

    interface FontsSizesCallback {
        fun onChangeFontSizes(fontSizeTitle: Int, fontSizeDate: Int)
    }

    override fun onDetached() {
        removeCallbacks()
        super.onDetached()
    }

    private fun removeCallbacks() {
        AppearanceSettingsFragment.registerResetFontSizeCallback(null)
    }

    //--- interface --------------------------------------------------------------------------------
    companion object {
        private var callback: FontsSizesCallback? = null

        fun registerFontsSizesCallback(callback: FontsSizesCallback) {
            Companion.callback = callback
        }
    }
}