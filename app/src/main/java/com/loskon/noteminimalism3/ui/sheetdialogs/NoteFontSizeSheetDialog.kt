package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setSliderColor
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.utils.changeTextSize
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Изменение размер текста заметок
 */

class NoteFontSizeSheetDialog(sheetContext: Context) :
    BaseSheetDialog(sheetContext, R.layout.sheet_note_font_size) {

    private val textView: TextView = view.findViewById(R.id.tv_sheet_font_size)
    private val slider: Slider = view.findViewById(R.id.slider_sheet_font_size)
    private val btnReset: MaterialButton = view.findViewById(R.id.btn_sheet_font_size_reset)

    private var fontSizeNote: Int = 0

    init {
        configureDialogParameters()
        establishViewsColor()
        setStateChecked()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_font_size_title)
    }

    private fun establishViewsColor() {
        slider.setSliderColor(color)
    }

    private fun setStateChecked() {
        fontSizeNote = PrefHelper.getNoteFontSize(context)
        slider.value = fontSizeNote.toFloat()
        changeTextSize()
    }

    private fun setupViewsListeners() {
        slider.addOnChangeListener { _, value: Float, _ -> onSliderChange(value) }
        btnReset.setOnSingleClickListener { onResetBtnClick() }
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun onSliderChange(value: Float) {
        fontSizeNote = value.toInt()
        changeTextSize()
    }

    private fun changeTextSize() = textView.changeTextSize(fontSizeNote)

    private fun onResetBtnClick() {
        fontSizeNote = 18
        changeTextSize()
        slider.value = fontSizeNote.toFloat()
    }

    private fun onOkBtnClick() {
        dismiss()
        saveResult()
    }

    private fun saveResult() {
        PrefHelper.setNoteFontSize(context, fontSizeNote)
    }
}
