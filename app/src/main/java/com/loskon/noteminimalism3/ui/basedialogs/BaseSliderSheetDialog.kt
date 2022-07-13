package com.loskon.noteminimalism3.ui.basedialogs

import android.content.Context
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.managers.setSliderColor

/**
 *
 */

abstract class BaseSliderSheetDialog(
    sheetContext: Context,
    private val stringId: Int,
    private val sliderValue: Int
) :
    BaseSheetDialog(sheetContext, R.layout.sheet_slider) {

    private val slider: Slider = view.findViewById(R.id.slider_range)

    init {
        configureDialogParameters()
        establishViewsColor()
        configureInsertedViews()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(stringId)
    }

    private fun establishViewsColor() {
        slider.setSliderColor(color)
    }

    private fun configureInsertedViews() {
        slider.value = sliderValue.toFloat()
    }

    private fun setupViewsListeners() {
        btnOk.setDebounceClickListener { onOkBtnClick() }
    }

    abstract fun onOkBtnClick()

    val prefKey: String get() = context.getString(stringId)
    val currentSliderValue: Int get() = slider.value.toInt()
}